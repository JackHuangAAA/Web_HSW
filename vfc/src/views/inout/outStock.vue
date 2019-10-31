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
                <img src="/static/img/back.png" class="back" @click="back()">
                <p class="headP">请将出库疫苗扫码</p>
                <img src="/static/img/outCabineSetp1.png" class="vaccineIn">
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
                    <div class="coordinate">
                        是否失效
                    </div>
                    <div class="count">
                        出库数量
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
                            {{item.expiry}}
                        </div>
                        <div class="coordinate">
                            {{item.invalid=='异常'?'异常':'正常'}}
                        </div>
                        <div class="count">
                            <input type="text" class="countInput" readonly="readonly" v-model="item.count">
                        </div>
                    </div>
                </div>
                <div class="button">
                    <div class="finish" @click="finish()">
                        出库完成
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import {mapGetters} from 'vuex';
    import uuid from 'uuid/v1';
    import moment from 'moment';

    export default {
        data() {
            return {
                tableDatas: [],
                clickIndex: 0,
                ifTip: false,
                commonData: null,
                batchId:'',
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
            back(){
                this.$router.push('/main');
            },
            closeTip(){
                this.ifTip = false;
            },
            async queryVaccineByCondition(param){
                let res = await this.$api.get("/vaccine/queryVaccineByCondition",param);
                return res.data[0];
            },
            async removeVaccineById(params){
                await this.$api.post("/vaccine/removeVaccineById", params);
            },
            async modifyVaccineNum(params){
                await this.$api.post("/vaccine/modifyVaccineNum", params);
            },
            async saveInout(params){
                await this.$api.post("/inout/saveInout", params);
            },
            async queryExceptionVaccine(){
                return await this.$api.get("/zcy/queryExceptionVaccine");
            },
            //扫描枪扫码数量减少后，自动保存
            async scanOut(){
                // this.$device.subscribe('SCANNER_RESULT', (data) => {
                //     console.log("这里是扫码枪的内容 result:" + JSON.stringify(data));
                    let result= {code: 'ym991',name:'卡介苗',batchNo:'B-20191001', expiry:moment('2019-12-24').format('YYYY-MM-DD HH:mm:ss'), product:'上海生物制药有限公司'};// 模拟扫描枪返回结果 todo
                    //查询疫苗数据
                    let vaccine = await this.queryVaccineByCondition({
                        'device': this.device._id,
                        'code': result.code
                    });
                    //使用1支，若剩余数量=0，从删除疫苗记录
                    if(vaccine.surplus-1 == 0){
                        //删除疫苗记录
                        await this.removeVaccineById({
                            id: vaccine._id
                        });
                    }else{
                        //疫苗数量减少
                        await this.modifyVaccineNum({
                            id: vaccine._id,
                            total: 0, //出库总记录不变
                            surplus: -1
                        });
                    }
                    //增加出库记录
                    await this.saveInout({
                        batchId: this.batchId,
                        ...this.commonData,
                        code: result.code,
                        name: result.name,
                        total: vaccine.total,
                        surplus: vaccine.surplus-1,
                        product: vaccine.product,
                        use: 1
                    });
                    vaccine.invalid='正常';
                    //检查是否异常疫苗
                    //判断有效期是否过期
                    if(moment().isAfter(vaccine.expiry)){
                        this.exName = result.name;
                        this.exReason = "疫苗过期";
                        vaccine.invalid='异常';
                        this.ifTip = true; //提示框显示
                    }
                    let ex = await this.queryExceptionVaccine();
                    //检查异常条件待接口完善后，需要修改 todo
                    if(result.batchNo == ex.batchNo){
                        this.exName = result.name;
                        this.exReason = "报废或失效"; // todo
                        vaccine.invalid='异常';
                        this.ifTip = true; //提示框显示
                    }
                    //页面数据更新
                    await this.freshTableDatas(vaccine);
                // });
            },
            freshTableDatas(obj){
                let array = this.tableDatas, flag = true;
                if(_.isEmpty(array)){
                    obj.count = 1;
                    obj.clickIndex = 0;
                    array.push(obj);
                }else{
                    for(let z=0;z<array.length;z++){
                        if(obj.code == array[z].code){
                            array[z].count = parseInt(array[z].count)+1;
                            array[z].clickIndex = z;
                            flag = false;
                            break;
                        }else{
                            array[z].clickIndex = null;
                        }
                    }
                    if(flag){
                        obj.count = 1;
                        obj.clickIndex = 0;
                        array.unshift(obj);
                    }
                }
            },
            finish(){
                this.$router.push({ path: '/inout/outStockEnd', query: { datas: this.tableDatas }});
            },
        },
        mounted() {
            //监听扫描枪事件
            this.scanOut();
            this.batchId = uuid();
            this.commonData = {
                type: 2, //2:出库
                user: this.user._id,
                device: this.device._id,
                deviceType: 2, //2:冷藏柜
                unitCode: this.device.unitCode,
                unitName: this.device.unitName
            };
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/outStock.less";
</style>
<style>
    .ivu-input{
        height: 2.5rem;
    }
</style>
