<template>
    <div style="position:relative">
        <div class="block" v-if="ifTip">
            <div class="tips" v-if="ifTip">
                <img src="/static/img/close.png" class="close" @click="closeTip()">
                <div class="tipsTitle">
                    <img src="/static/img/info.png" class="info">
                    <p class="tipsTitleP">{{exName}}</p>
                </div>
                <div class="tipP">
                    {{exReason}}
                </div>
                <div class="tipsYes" @click="closeTip()">
                    确定
                </div>
            </div>
        </div>
        <div class="container">
            <div class="inStockTitle">
                <!--<img src="/static/img/back.png" class="back" @click="back()">-->
                <p class="headP">请将入库疫苗扫码</p>
                <img src="/static/img/inCabinet1.png" class="vaccineIn">
            </div>
            <div class="main">
                <div class="title">
                    <div class="index">
                        序号
                    </div>
                    <div class="vaccineName">
                        疫苗名称
                    </div>
                    <div class="code">
                        批次号
                    </div>
                    <div class="code2">
                        有效期
                    </div>
                    <div class="count">
                        入库数量
                    </div>
                </div>
                <div class="table">
                    <div v-for="(item,index) in tableDatas" class="tableData" :class="{clicked: clickIndex==index}">
                        <div class="index">
                            <span v-if="index>8">{{index+1}}</span>
                            <span v-if="index<8 || index ==8">0{{index+1}}</span>
                        </div>
                        <div class="vaccineName">
                            {{item.name}}
                        </div>
                        <div class="code">
                            {{item.batchNo}}
                        </div>
                        <div class="code2">
                            {{dateformat(item.expiry)}}
                        </div>
                        <div class="count">
                            <input type="text" class="countInput" readonly="readonly" v-model="item.count">
                        </div>
                    </div>
                </div>
                <div class="button">
                    <div class="finish" @click="finish()">
                        入库完成
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import {mapGetters} from 'vuex';
    import moment from 'moment';

    export default {
        data() {
            return {
                tableDatas: [],
                clickIndex: 0,
                ifTip: false,
                exName: '',
                exReason:''
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device',
            })
        },
        components:{},
        methods: {
            closeTip(){
                this.ifTip = false;
            },
            async queryExceptionVaccine(){
                return await this.$api.get("/zcy/queryExceptionVaccine");
            },
            //扫描枪扫码数量增加后，自动保存
            async scanIn(){
                // this.$device.subscribe('SCANNER_RESULT', (data) => {
                //     console.log("这里是扫码枪的内容 result:" + JSON.stringify(data))
                    let result= {code: '6',name:'y6',batchNo:'6', expiry:this.dateformat('2019-12-30'), product:'武汉生物制药有限公司'};// 模拟扫描枪返回结果 todo
                    //检查是否异常疫苗
                    await this.checkException(result);
                    //页面数据更新
                    await this.freshTableDatas(result);
                // });
            },
            freshTableDatas(obj){
                let array = this.tableDatas, flag = true;
                /*for(let i=0;i<obj.length;i++){
                    obj[i].device = this.device._id;
                    if(_.isEmpty(array)){
                        obj[i].count = 1;
                        obj[i].total = 1;
                        obj[i].surplus = 1;
                        obj[i].clickIndex = 0;
                        array.push(obj[i]);
                    }else{
                        for(let z=0;z<array.length;z++){
                            if(obj[i].code == array[z].code){
                                array[z].count = parseInt(array[z].count)+1;
                                array[z].total = array[z].count;
                                array[z].surplus = array[z].count;
                                array[z].clickIndex = z;
                                flag = false;
                                break;
                            }else{
                                array[z].clickIndex = null;
                            }
                        }
                        //已扫描疫苗中没有的新疫苗
                        if(flag){
                            obj[i].count = 1;
                            obj[i].total = 1;
                            obj[i].surplus = 1;
                            obj[i].clickIndex = 0;
                            array.unshift(obj[i]);
                        }
                    }
                }*/
                obj.device = this.device._id;
                if(_.isEmpty(array)){
                    obj.count = 1;
                    obj.total = 1;
                    obj.surplus = 1;
                    obj.clickIndex = 0;
                    array.push(obj);
                }else{
                    for(let z=0;z<array.length;z++){
                        if(obj.code == array[z].code){
                            array[z].count = parseInt(array[z].count)+1;
                            array[z].total = array[z].count;
                            array[z].surplus = array[z].count;
                            array[z].clickIndex = z;
                            flag = false;
                            break;
                        }else{
                            array[z].clickIndex = null;
                        }
                    }
                    //已扫描疫苗中没有的新疫苗
                    if(flag){
                        obj.count = 1;
                        obj.total = 1;
                        obj.surplus = 1;
                        obj.clickIndex = 0;
                        array.unshift(obj);
                    }
                }
            },
            async checkException(result){
                //判断有效期是否过期
                if(moment().isAfter(result.expiry)){
                    this.exReason = "过期失效"; // todo
                    this.exName = result.name;
                    this.ifTip = true; //提示框显示
                    return false;
                }
                //获取政采云的疫苗异常标准数据
                let ex = await this.queryExceptionVaccine();
                //检查异常条件待接口完善后，需要修改 todo
                if(result.batchNo == ex.batchNo){
                    this.exReason = "报废失效"; // todo
                    this.exName = result.name;
                    this.ifTip = true; //提示框显示
                    return false;
                }
            },
            dateformat(val){
                return moment(val).format('YYYY-MM-DD HH:mm:ss');
            },
            finish(){
                this.$router.push({ path: '/inout/inPosition', query: { datas: this.tableDatas }});
            },
        },
        mounted() {
            //监听扫描枪事件
            this.scanIn();
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/inStock.less";
</style>
<style>
    .ivu-input{
        height: 2.5rem;
    }
</style>
