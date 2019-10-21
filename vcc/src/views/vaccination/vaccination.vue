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
                            <div>{{info.customer? info.customer.code:''}}号</div>
                            <div>{{info.customer?info.customer.name: ''}}</div>
                        </div>
                        <div class="inoculate-content">
                            <div>接种疫苗：</div>
                            <div class="blue">{{info.customer?info.customer.vaccineName: ''}}</div>
                        </div>
                    </div>
                    <div class="inoculate-info-column">
                        <div class="inoculate-content">
                            <div>年龄：</div>
                            <div>{{info.customer?info.customer.age:''}}</div>
                        </div>                        
                        <div class="inoculate-content">
                            <div>接种支数：</div>
                            <div>{{info.customer?info.customer.vaccineNum:''}}</div>
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
                            <div>{{vaccine.supervisionCode}}</div>
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
                vaccinationData:'', //接种信息
                info: '',
                vaccine: ''
            };
        },
        computed: {
            ...mapGetters({
                device: 'device',
            })
        },
        methods: {
            //测试使用
            async getCustomer() {
                let res = await this.$api.post("/zcy/reciveVaccination");
                this.info = res.data;console.log('99------%j',this.info)
            },
            //测试使用
            async queryVaccine() {
                let res = await this.$api.get(`/zcy/queryVaccine`);
                this.vaccine = res.data;console.log('100-------------%',this.vaccine)
            },
            //比对疫苗信息
            async matchInfo(obj){

                //疫苗数量减少1

                //比对成功，增加出库信息和接种信息
                await this.saveInout({type:0});
                await this.saveVaccination({});
            },
            getExpiryDate(val){
                return moment(val).format('YYYY-MM-DD HH:mm:ss');
            },
            async saveInout(params){
                await this.$api.post("/inout/saveInout", params);
            },
            async saveVaccination(params){
                await this.$api.post("/vaccination/saveVaccination", params);
            },
            async openDrawer(val){
                //查询疫苗所在抽屉信息,使用规则：多个柜子满足,按最少优先使用
                let vaccine = await this.$api.get(`/vaccine/queryVaccineByCondition`,{
                    device: this.device._id,
                    sortSurplus: true,
                    surplusIsNotZero: true,
                    code: '002'  //val.vaccineCode
                });
                let drawer = await this.$api.get(`/drawer/queryDrawerByCondition`,{
                    device: this.device._id,
                    vaccineCode: vaccine.data[0]._id
                });
                console.log('drawer===>',drawer)
                //调用打开抽屉接口
                this.$device.openDrawer();
            },
            //接收接种状态
            receiveVaccination(){
                this.$device.subscribe('VACCINATION_STATUS', (data) => {
                    console.log('SERVER_PUSH==>VACCINATION_STATUS');
                    let vaccination = null;

                    this.$router.push('/main');
                });
            }
        },
        mounted() {
            //接收推送的接种信息
            this.vaccinationData = this.$route.query.vaccination;
            //打开需要接种的疫苗所在抽屉
            this.openDrawer(this.vaccinationData);
            let data = "";
            this.matchInfo(data);
            //测试使用
            this.getCustomer();
            this.queryVaccine();
            this.matchInfo(data);
        }
    };
</script>

<style lang="less">
    @import "~@/style/vaccination.less";
</style>