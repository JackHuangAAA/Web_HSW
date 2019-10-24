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
                </div>
                <div class="leftBottom">
                    <div class="personInf" v-for="(item,index) in vaccinationData.vaccine">
                        <div class="personInfVaccine">
                            <p class="personInfVaccineP vaccineOne">接种疫苗</p>
                            <p class="personInfVaccineP vaccineTwo">{{item.name}}</p>
                            <p class="personInfVaccineP vaccineThree">{{item.product}}</p>
                        </div>
                        <div class="personInfCount">
                            <p class="getVaccineOne">数量</p>
                            <p class="getVaccineCount">{{item.num}}支</p>
                        </div>
                        <div class="personInfStatus">
                            <p class="getVaccineOne">状态:</p>
                            <p class="getVaccineCount">
                                <img src="/static/img/succeed.png" class="succeed" v-if="item.status"><span v-if="item.status" class="tipInf" style="color:#1AA95E">信息匹配</span>
                                <img src="/static/img/error.png" class="succeed" v-if="!item.status"><span v-if="!item.status" class="tipInf" style="color:#F42954">信息不匹配</span>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="right">
                <div class="vaccineInf" v-for="(item,index) in vaccine">
                    <div class="vaccineTop">
                        <div class="vaccineTopLeft">
                            <div class="topTip">
                                品名:
                            </div>
                            <div class="bottomInf">
                                {{item.name}}
                            </div>
                        </div>
                        <div class="vaccineTopRight">
                            <div class="topTip">
                                批次号:
                            </div>
                            <div class="bottomInf" style="color:#3E4955">
                                {{item.batchNo}}
                            </div>
                        </div>
                    </div>
                    <div class="vaccineBottom">
                        <div class="vaccineTopLeft">
                            <div class="topTip">
                                有效期:
                            </div>
                            <div class="bottomInf" style="color:#3E4955;">
                                {{getExpiryDate(item.expiry)}}
                            </div>
                        </div>
                        <div class="vaccineTopRight">
                            <div class="topTip">
                                生产企业:
                            </div>
                            <div class="bottomInf" style="color:#3E4955">
                                {{item.producer}}
                            </div>
                        </div>
                    </div>
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
                        'age': 4
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
                ]//疫苗扫码信息
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
                this.$device.subscribe('VACCINATION_SCAN', (data) => {
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
                this.$device.subscribe('VACCINATION_STATUS', (data) => {
                    console.log('SERVER_PUSH==>VACCINATION_STATUS');
                    //根据状态，判断是否跳转到首页

                    this.$router.push('/main');
                });
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
            //this.vaccinationData = this.$route.query.vaccination;
            //打开需要接种的疫苗所在抽屉
            //this.openDrawer(this.vaccinationData);
            this.receiveVaccinationStatus();
            //接收疫苗扫描后结果，与接种信息比对
            this.matchInfo();
        }
    };
</script>

<style lang="less" scoped>
    @import "~@/style/vaccination.less";
</style>