<!--接种页面-->
<template>
    <div class="inoculate card">
        <Row class="inoculate-row">
            <div class="leftBcg"></div>
            <div class="rightBcg"></div>
            <Col span="12">
                <div class="title-center">接种人信息</div>
                <div class="inoculate-info">
                    <div class="inoculate-info-column">
                        <div class="inoculate-content human-info">
                            <div>{{vaccinationData.customer? vaccinationData.customer.code:''}}号</div>
                            <div>{{vaccinationData.customer?vaccinationData.customer.name: ''}}</div>
                        </div>
                        <div class="inoculate-content">
                            <div>接种疫苗：</div>
                            <div class="blue">{{vaccinationData.customer?vaccinationData.customer.vaccineName: ''}}</div>
                        </div>
                    </div>
                    <div class="inoculate-info-column">
                        <div class="inoculate-content">
                            <div>年龄：</div>
                            <div>{{vaccinationData.customer?vaccinationData.customer.age:''}}</div>
                        </div>                        
                        <div class="inoculate-content">
                            <div>接种支数：</div>
                            <div>{{vaccinationData.customer?vaccinationData.customer.vaccineNum:''}}</div>
                        </div>
                    </div>
                </div>
            </Col>
            <Col span="12" class="inoculate-row">
                <div class="title-center">疫苗信息</div>
                <div class="inoculate-info">
                    <div class="inoculate-info-column">
                        <div class="inoculate-content">
                            <div>品名：</div>
                            <div class="blue">{{vaccine.vaccineName}}</div>
                        </div>
                        <div class="inoculate-content">
                            <div>有效期：</div>
                            <div>{{getExpiryDate(vaccine.expiry)}}</div>
                        </div>
                    </div>
                    <div class="inoculate-info-column">
                        <div class="inoculate-content">
                            <div>批次号：</div>
                            <div>{{vaccine.batchNo}}</div>
                        </div>                        
                        <div class="inoculate-content">
                            <div>生产企业：</div>
                            <div>{{vaccine.producer}}</div>
                        </div>
                    </div> 
                </div>
            </Col>
        </Row>
        <Row class="inoculate-notice">
            <Col span="24">
                <div v-if="progress==0" class="inoculate-notice-rs"><img src="/static/img/scan.png"/><div>请扫描疫苗，核对是否正确</div></div>
                <div v-if="progress==1" class="inoculate-notice-rs"><img src="/static/img/succeed.png"/><div>疫苗接种信息匹配成功</div></div>
                <div v-if="progress==2" class="inoculate-notice-rs"><img src="/static/img/error.png"/>疫苗接种信息匹配失败</div>
            </Col>
        </Row>
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
                        'vaccineCode': 'ym20190920134508999',
                        'vaccineName': '脊髓灰质炎疫苗',
                        'vaccineNum': 1
                    }
                }, //接种信息
                vaccine: {
                    'vaccineName': '脊髓灰质炎疫苗',
                    expiry: new Date(),
                    batchNo:'b-99',
                    producer:'rrr'
                } //疫苗扫码信息
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

<style lang="less">
    @import "~@/style/vaccination.less";
</style>