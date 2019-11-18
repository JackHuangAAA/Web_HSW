<!--接种页面-->
<template>
    <div class="container">
        <div class="containerBody">
            <div class="title">
            <div class="titleLeft">
                接种人信息
            </div>
            <div class="titleRight">
                疫苗信息
            </div>
        </div>
        <div class="content">
            <div class="left">
                <div class="leftTop">
                    <div class="leftTopLeft">
                        <span class="code">{{vaccinationData.customer? vaccinationData.customer.code:''}}号</span><span class="name">{{vaccinationData.customer?vaccinationData.customer.name: ''}}</span>
                    </div>
                    <div class="leftTopRight">
                        <div class="age">
                            年龄:
                        </div>
                        <div class="ageCount">
                            {{vaccinationData.customer?vaccinationData.customer.age:''}}周岁
                        </div>
                    </div>
                    <div class="leftTopRightTwo">
                        <div class="age">
                            性别:
                        </div>
                        <div class="ageCount">
                            {{vaccinationData.customer?vaccinationData.customer.sex:''}}
                        </div>
                    </div>
                </div>
                <div class="leftBottom">
                    <CheckboxGroup v-model="model1">
                        <div class="personInf" v-for="(item,index) in vaccinationData.vaccine">
                            <div>
                                <span v-if="!item.status" class="tipInf" style="color:#F42954">信息不匹配</span><img src="/static/img/error.png" class="succeed" v-if="!item.status">
                                <span v-if="item.status" class="tipInf" style="color:#1AA95E">信息匹配</span><img src="/static/img/succeed.png" class="succeed" v-if="item.status">
                                <Checkbox class="personInf-check" :label="item.name"></Checkbox>
                            </div>
                            <div class="producter">
                                生产厂家：{{item.product}}
                            </div>
                            <div class="vaccine-info">
                                <div class="vaccine-info-part">
                                    <div>接种部位：</div>
                                    <Select v-model="model1" style="width:9rem;height:2.25rem">
                                        <Option v-for="item in part" :value="item.value" :key="item.key">{{ item.value }}</Option>
                                    </Select>
                                </div>
                                <div class="vaccine-info-num">
                                    接种剂量：{{item.num}}支
                                </div>
                            </div>
                            <div class="vaccine-btnBox">
                                <button>取消接种</button>
                                <button>确认接种</button>
                            </div>
                        </div>
                    </CheckboxGroup>
                </div>
            </div>
            <div class="right">
                <div class="vaccineInf" v-for="(item,index) in vaccine">
                    <div class="vaccineInf-top">
                        <div><span>{{item.name}}</span></div>
                        <div>监管码：<span>Y750230-64368</span></div>
                        <div class="mt24">批次号：<span>Y750230-64368</span></div>
                        <div class="mt24">疫苗剂量：<span>0.5ml/支</span></div>
                    </div>
                    <div class="vaccineInf-bottom">有效期：<span>2019-09-18 12:30:28   至  2022-12-29 12:20:19</span></div>
                    <div class="vaccineInf-bottom">生产厂家：<span>{{item.producer}}</span></div>
                    <div class="vaccineInf-bottom">接种份数：<span>1</span></div>
                </div>
            </div>
        </div>
        </div>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex';
    import moment from 'moment';
    import uuid from 'uuid/v1';

    export default {
        data() {
            return {
                progress:0,
                batchId: uuid(),
                commonData: null,
                queryVaccine: null,  //查询的本地疫苗信息
                vaccinationData: {
                    'customer':{
                        'code': '089',
                        'name': '李义',
                        'age': 4,
                        'sex':'女'
                    },
                    'vaccine':[
                        {
                            name:'乙肝疫苗',
                            product:'长春生物制药',
                            num:'1',
                            status:true
                        },{
                            name:'流感疫苗',
                            product:'长春生物制药',
                            num:'1',
                            status:true
                        },{
                            name:'腮腺炎疫苗',
                            product:'长春生物制药',
                            num:'1',
                            status: false
                        }

                    ]
                }, //接种信息
                vaccine: [
                    {
                        'name': '乙肝疫苗',
                        'expiry': new Date(),
                        'batchNo':'b-001',
                        'producer':'长春生物制药'
                    },
                    {
                        'name': '流感疫苗',
                        'expiry': new Date(),
                        'batchNo':'b-002',
                        'producer':'长春生物制药'
                    },
                    {
                        'name': '腮腺炎疫苗',
                        'expiry': new Date(),
                        'batchNo':'b-003',
                        'producer':'长春生物制药'
                    }
                ],//疫苗扫码信息
                part: [
                    {
                        value: '左上臂',
                        key: '左上臂'
                    },
                    {
                        value: '右上臂',
                        key: '右上臂'
                    }
                ],
                model1: [],
                single:false
            };
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device'
            })
        },
        methods: {
            //比对疫苗信息
            async matchInfo(){
                //接收疫苗
                this.$device.subscribe('SCANNER_RESULT', (data) => {
                    console.log("这里是扫码枪的内容 result:" + JSON.stringify(data))
                    this.vaccine = data.data;
                    //疫苗信息与扫码的疫苗比对,根据不同结果显示不同提示信息
                    if(this.vaccine.name == this.vaccinationData.name){
                        this.progress = 1;
                        //比对成功，疫苗数量减少1、增加出库信息、保存接种信息
                        //疫苗数量减少1
                        this.modifyVaccineNum({
                            id: this.queryVaccine._id,
                            total: 0,
                            surplus: -1
                        });
                        //增加出库信息
                        this.saveInout({
                            batchId: batchId,
                            ...this.commonData,
                            x: this.vaccineOneX,
                            y: this.vaccineOneY,
                            code: this.vaccineOneCode,
                            name: this.addVaccineOne,
                            total: this.vaccineOneCount,
                            surplus: this.vaccineOneCount
                        }); //todo
                        //保存接种信息
                        this.saveVaccination({}); //todo
                    }else{
                        this.progress = 2;
                    }
                });
            },
            getExpiryDate(val){
                return moment(val).format('YYYY-MM-DD HH:mm:ss');
            },
            async modifyVaccineNum(params){
                await this.$api.post("/vaccine/modifyVaccineNum", params);
            },
            async saveInout(params){
                await this.$api.post("/inout/saveInout", params);
            },
            async saveVaccination(params){
                await this.$api.post("/vaccination/saveVaccination", params);
            },
            async queryVaccineByCondition(params){
                let res = await this.$api.get(`/vaccine/queryVaccineByCondition`, params);
                this.queryVaccine = res.data[0];
            },
            async queryDrawerByCondition(params){
                return await this.$api.get(`/drawer/queryDrawerByCondition`, params);
            },
            async openDrawer(obj){
                //查询疫苗所在抽屉信息,使用规则：多个柜子满足,按最少优先使用
                await this.queryVaccineByCondition({
                    device: this.device._id,
                    sortSurplus: true,
                    surplusIsNotZero: true,
                    code: obj.vaccineCode //接种信息中的疫苗code todo
                });
                let drawer = await thisqueryDrawerByCondition({
                    device: this.device._id,
                    vaccineCode: this.queryVaccine._id
                });
                console.log('drawer===>',drawer)
                //调用打开抽屉接口
                this.$device.openDrawer();  //todo
            },
            //接收接种状态
            receiveVaccinationStatus(){
                this.$device.subscribe('SOCKET_VACCINATION_STATUS_DATA', (data) => {
                    console.log('SOCKET_VACCINATION_STATUS_DATA====> result:'+JSON.stringify(data));
                    //根据状态，判断是否跳转到首页
                    let res=JSON.parse(data.data)
                    if(res.status=='finish'){
                        this.$router.push('/main');
                    }                    
                });
            },
            queryQueue(){
                // this.$api.get()
            }
        },
        mounted() {
            this.commonData = {
                type: 0, //0:接种
                user: this.user._id,
                device: this.device._id,
                deviceType: 1, //1:接种柜
                unitCode: this.device.unitCode,
                unitName: this.device.unitName
            };
            //接收推送的接种信息(home.vue中接收)
            // this.vaccinationData = this.$route.query.vaccination;
            //打开需要接种的疫苗所在抽屉
            //this.openDrawer(this.vaccinationData);
            //this.receiveVaccinationStatus();
            //接收疫苗扫描后结果，与接种信息比对
            this.matchInfo();
            //获取排队数据
        }
    };
</script>

<style lang="less" scoped>
    @import "~@/style/vaccination.less";
</style>