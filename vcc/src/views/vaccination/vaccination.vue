<!--接种页面-->
<template>
    <div class="inoculate card">
        <Row class="inoculate-row">
            <Col span="12">
                <div class="title-center">接种人信息</div>
                <div class="inoculate-info">
                    <div class="inoculate-info-column">
                        <div class="inoculate-content human-info">
                            <div>{{info.customer.code}}号</div>
                            <div>&nbsp;&nbsp;{{info.customer.name}}</div>
                        </div>
                        <div class="inoculate-content">
                            <div>接种疫苗：</div>
                            <div class="blue">{{info.customer.vaccineName}}</div>
                        </div>
                    </div>
                    <div class="inoculate-info-column">
                        <div class="inoculate-content">
                            <div>年龄：</div>
                            <div>{{info.customer.age}}</div>
                        </div>                        
                        <div class="inoculate-content">
                            <div>接种支数：</div>
                            <div>{{info.customer.vaccineNum}}</div>
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
                            <div>{{vaccine.expiry}}</div>
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
    import moment from 'moment';

    export default {

        data() {
            return {
                progress:0,
                info: '',
                vaccine: ''
            };
        },
        methods: {
            //比对疫苗信息
            async matchInfo(obj){

            },
            //测试使用
            async getCustomer() {
                let res = await this.$api.post("/zcy/reciveVaccination");
                this.info = res.data;console.log('99------%j',this.info)
            },
            //测试使用
            async queryVaccine() {
                let res = await this.$api.get(`/zcy/queryVaccine`);
                this.vaccine = res.data;console.log('100-------------%',this.vaccine)
            }

        },
        mounted() {
            //接收导医台推送的接种信息
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