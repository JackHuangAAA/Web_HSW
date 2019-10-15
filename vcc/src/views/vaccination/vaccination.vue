<!--接种页面-->
<template>
    <div class="inoculate card">
        <Row>
            <Col span="12">
                <div>接种人信息</div>
                <div>
                 {{info.customer.code}} &nbsp;&nbsp;{{info.customer.name}}
                    年龄：{{info.customer.age}}
                    接种疫苗：{{info.customer.vaccineName}}
                    数量：{{info.customer.vaccineNum}}
                </div>
            </Col>
            <Col span="12">
                <div>疫苗信息</div>
                <div>
                    品名：{{vaccine.vaccineName}}
                    批次号：{{vaccine.supervisionCode}}
                    有效期：{{vaccine.expiry}}
                    生产企业：{{vaccine.producer}}
                </div>
            </Col>
        </Row>
        <Row>
            <Col span="12">
                <div v-if="progress==0"> 请扫描疫苗，核对是否正确</div>
                <div v-if="progress==1">疫苗接种信息匹配成功</div>
                <div v-if="progress==2">疫苗接种信息匹配失败</div>
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
                this.vaccine = res.data;
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