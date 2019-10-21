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
                    <p class="headP">请将入库疫苗扫码</p>
                    <img src="/static/img/vaccineInTwo.png" class="vaccineIn">
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
                    <div class="coordinate">
                        位置信息
                    </div>
                    <div class="count">
                        入库数量
                    </div>
                </div>
                <div class="table">
                    <div v-for="(item,index) in tableDatas" @click="selectVaccine(index)" class="tableData">
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
                        <div class="coordinate">
                            ({{item.x}},{{item.x}})
                        </div>
                        <div class="count">
                            <input type="text" class="countInput" v-model="item.count">
                        </div>
                    </div>
                </div>
                <div class="button">
                    <div class="buttonBack" @click="back()">
                        继续入库
                    </div>
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
    import uuid from 'uuid/v1';

    export default {
        data() {
            return {
                tableDatas: [],
                clickIndex: 0,
                ifTip: false,
                drawerId:'',
                commonData: null,
                batchId:'',
                map: null,
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
                this.$router.push('/inout/inStock');
            },
            closeTip(){
                this.ifTip = false;
            },
            selectVaccine(index){
                this.clickIndex = index;
                // this.ifTip = true;
            },
            async modifyDrawerById(params){
                await this.$api.post("/drawer/modifyDrawerById", params);
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
            async setCash(){
                this.map = new Map();
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    id: this.drawerId
                });
                let array = res.data;
                let vaccines = array[0].vaccine;
                for (let k = 0; k < vaccines.length; k++) {
                    let  temp = {};
                    temp.drawerId = array[0]._id;
                    temp.vaccineId = vaccines[k]._id;
                    temp.code = vaccines[k].code;
                    temp.name = vaccines[k].name;
                    temp.batchNo = vaccines[k].batchNo;
                    temp.x = array[0].x;
                    temp.y = array[0].y;console.log('kk===='+vaccines[k].code+'-'+vaccines[k].batchNo)
                    this.map.set(vaccines[k].code+'-'+vaccines[k].batchNo,temp); //缓存本抽屉疫苗数据，在变更数量时使用
                }
            },
            async queryDrawerByCondition(){
                this.tableDatas = [];
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    id: this.drawerId
                });
                let array = res.data;
                this.commonData.x = array[0].x;
                this.commonData.y = array[0].y;
                let vaccines = array[0].vaccine;
                for (let k = 0; k < vaccines.length; k++) {
                    let  temp = {};
                    temp.id = array[0]._id;
                    temp.vaccineId = vaccines[k]._id;
                    temp.code = vaccines[k].code;
                    temp.name = vaccines[k].name;
                    temp.batchNo = vaccines[k].batchNo;
                    temp.x = array[0].x;
                    temp.y = array[0].y;
                    temp.count = 0;
                    this.tableDatas.push(temp);
                }
            },
            //扫描枪扫码数量增加后，自动保存
            async scanSave(){
                //this.$device.subscribe('SCAN_ADD_VACCINE', (data) => {
                    console.log('SERVER_PUSH==>SCAN_ADD_VACCINE');
                    let result= {code: '1',name:'y1',batchNo:'1'};// 模拟扫描枪返回结果 todo
                    //检查是否异常疫苗
                    let ex = await this.queryExceptionVaccine();
                    if(result.batchNo == ex.batchNo){
                        this.exName = result.name;
                        this.exReason = "报废或失效"; // todo
                        this.ifTip = true;
                        return false;
                    }
                    //从缓存取数据
                    let data = this.map.get(result.code+'-'+result.batchNo);console.log((result.code+'-'+result.batchNo)+'-----map--',data)
                    //缓存找不到批次信息，是新批次，需要增加
                    if(_.isEmpty(data)){
                        //新增疫苗（新批次）与抽屉关联
                        await this.modifyDrawerById({id: this.drawerId,
                            vaccine: {
                                device: this.device._id,   //设备
                                code: result.code,   //疫苗编号
                                name: result.name,   //疫苗名称
                                batchNo: result.batchNo, //疫苗批次
                                total: 1,    //数量(入库总数)
                                surplus:1    //剩余数量
                            }});
                        //页面数据更新
                        await this.freshTableDatas('add', result.code, result.name, result.batchNo);
                    }else{
                        //修改数量
                        await this.modifyVaccineNum({
                            id: data.vaccineId,
                            total: 1,
                            surplus: 1
                        });
                        //页面数据更新
                        await this.freshTableDatas('modify',result.code, result.name, result.batchNo);
                    }
                    //增加入库记录
                    await this.saveInout({
                        batchId: this.batchId,
                        ...this.commonData,
                        code: result.code,
                        name: result.name,
                        total: 1,
                        surplus: 1
                    });
                    //刷新缓存
                    this.setCash();
                //});
            },
            freshTableDatas(action,code,name,batchNo){
                let arr = this.tableDatas;
                if(action == 'add'){
                    for(let i=0;i<arr.length;i++){
                       arr[i].clickIndex = null;//行颜色选中
                    }
                    arr.unshift({
                        name:name,
                        batchNo:batchNo,
                        x:this.commonData.x,
                        y:this.commonData.y,
                        clickIndex: 0,//行颜色选中
                        count:1
                    });
                }else{
                    for(let i=0;i<arr.length;i++){
                        if(arr[i].code==code || arr[i].batchNo==batchNo){
                            arr[i].count = arr[i].count + 1;
                            arr[i].clickIndex = i;//行颜色选中
                            break;
                        }else{
                            arr[i].clickIndex = null;
                        }
                    }
                }
            },
            finish(){
                let datas = [],array = this.tableDatas;
                for(let t=0;t<array.length;t++){
                    if(array[t].count>0){
                        datas.push(array[t]);
                    }
                }
                this.$router.push({ path: '/inout/inStockEnd', query: { datas: datas }});
            }
        },
        mounted() {
            //监听扫描枪事件
            setInterval(() => {
                this.scanSave();
            }, 5000);
            //this.scanSave();
            this.drawerId = this.$route.query.drawerId;
            this.batchId = uuid();
            this.commonData = {
                type: 1, //1:入库
                user: this.user._id,
                device: this.device._id,
                deviceType: 2, //2:冷藏柜
                unitCode: this.device.unitCode,
                unitName: this.device.unitName
            };
            this.setCash();
            this.queryDrawerByCondition(true);
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/inStockCount.less";
</style>
<style>
    .ivu-input{
        height: 2.5rem;
    }
</style>
