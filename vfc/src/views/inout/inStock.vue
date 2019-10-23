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
    import moment from 'moment'
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
            closeTip(){
                this.ifTip = false;
            },
            async queryVaccineByCondition(param){
                let res = await this.$api.get("/vaccine/queryVaccineByCondition",param);
                return res.data[0];
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
            //扫描枪扫码数量增加后，自动保存
            async scanIn(){
                //this.$device.subscribe('SCAN_ADD_VACCINE', (data) => {
                console.log('SERVER_PUSH==>SCAN_ADD_VACCINE');
                let result= {code: '1',name:'y1',batchNo:'1', expiry:new Date()};// 模拟扫描枪返回结果 todo
                //检查是否异常疫苗
                await this.checkException(result);
                //数据入库
                //查询疫苗数据
                let vaccine = await this.queryVaccineByCondition({
                    'device': this.device._id,
                    'code': result.code,
                    'batchNo': result.batchNo
                });
                //若没找到疫苗信息，提示去区域划分增加疫苗类型
                if(_.isEmpty(vaccine)){
                    this.$Message.info({
                        top: 300,
                        content: '该疫苗在冷藏柜中未找到对应的位置，请到区域划分设置',
                        duration: 10,
                        closable: true
                    });
                    return false;
                }else{
                    //疫苗数量增加
                    await this.modifyVaccineNum({
                        id: vaccine._id,
                        total: 1,
                        surplus: 1
                    });
                }
                //增加入库记录
                await this.saveInout({
                    batchId: this.batchId,
                    ...this.commonData,
                    code: result.code,
                    name: result.name,
                    total: vaccine.total+1,
                    surplus:vaccine.surplus+1
                });
                //页面数据更新
                await this.freshTableDatas(vaccine);
                //});
            },
            async checkException(result){
                //判断有效期是否过期
                if(moment().isAfter(result.expiry)){
                    this.exReason = "过期失效"; // todo
                    this.exName = result.name;
                    this.ifTip = true; //提示框显示
                    return false;
                }
                let ex = await this.queryExceptionVaccine();
                //检查异常条件待接口完善后，需要修改 todo
                if(result.batchNo == ex.batchNo){
                    this.exReason = "报废失效"; // todo
                    this.exName = result.name;
                    this.ifTip = true; //提示框显示
                    return false;
                }
            },
            freshTableDatas(obj){
                let array = this.tableDatas, flag = true;
                if(_.isEmpty(array)){
                    obj.count = 1;
                    obj.clickIndex = 0;
                    array.push(obj);
                }else{
                    for(let z=0;z<array.length;z++){
                        if(obj.code == array[z].code && obj.batchNo == array[z].batchNo && obj.invalid == array[z].invalid){
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
            this.batchId = uuid();
            this.commonData = {
                type: 1, //1:入库
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
    @import "~@/style/inStock.less";
</style>
<style>
    .ivu-input{
        height: 2.5rem;
    }
</style>
