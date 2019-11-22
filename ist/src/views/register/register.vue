<template>
    <div class="container">
        <div class="title">基本信息</div>
        <div class="userInfo">
            <div>姓名：<span>{{customerData?customerData.name:''}}</span></div>
            <div>性别：<span>{{customerData?customerData.sex:''}}</span></div>
            <div>年龄：<span>{{customerData?customerData.age:''}}周岁</span></div>
            <div>距离上次接种时间：<span class="yellow">{{customerData?customerData.intervalTime:''}}天</span></div>
        </div>
        <div class="title">请选择接种疫苗</div>
        <div class="content">
            <div class="left activebg">
                <div class="acitve-title">免费疫苗</div>
                <div class="left-box">
                    <div class="left-left">
                        <div class="box-top">
                            <div class="box-title">{{freeVaccineData?freeVaccineData.name:''}}</div>
                            <div>监管码：<span>{{freeVaccineData?freeVaccineData.supervisionCode:''}}</span></div>
                            <div>批次号：<span>{{freeVaccineData?freeVaccineData.batchNo:''}}</span></div>
                            <div>疫苗剂量：<span>{{freeVaccineData?freeVaccineData.dosage:''}}</span></div>
                        </div>
                        <div class="mb">有效期：<span>{{freeVaccineData?freeVaccineData.expiry:''}}</span></div>
                        <div>生产厂家：<span>{{freeVaccineData?freeVaccineData.product:''}}</span></div>
                    </div>
                    <div class="left-right">
                        <div>免费￥0.00</div>
                        <Checkbox v-model="single" value="" label=""> </Checkbox>
                    </div>
                </div>
            </div>
            <div class="right">
                <div class="acitve-title">自费疫苗</div>
                <div class="right-box">
                    <div class="right-left">
                        <div class="box-top">
                            <div class="box-title">{{paidVaccineData?paidVaccineData.name:''}}</div>
                            <div>监管码：<span>{{paidVaccineData?paidVaccineData.supervisionCode:''}}</span></div>
                            <div>批次号：<span>{{paidVaccineData?paidVaccineData.batchNo:''}}</span></div>
                            <div>疫苗剂量：<span>{{paidVaccineData?paidVaccineData.dosage:''}}</span></div>
                        </div>
                        <div class="mb">有效期：<span>{{paidVaccineData?paidVaccineData.expiry:''}}</span></div>
                        <div>生产厂家：<span>{{paidVaccineData?paidVaccineData.product:''}}</span></div>
                    </div>
                    <div class="right-right">
                        <div>自费￥{{paidVaccineData?paidVaccineData.cost:''}}</div>
                        <Checkbox v-model="single" value="" label=""> </Checkbox>
                    </div>
                </div>  
            </div>
        </div>
        <div class="btn-box">
            <div class="cancel" @click="quit()">退出</div>
            <div class="confirm" @click="confirm()">确认</div>
        </div>
    </div>
</template>
<script>
import { mapGetters, mapActions, mapState } from 'vuex';
import moment from 'moment';
import uuid from 'uuid/v1';
export default {
    data () {
        return {
            single:false,

            paidVaccineData:{},
            freeVaccineData:{},
            customerData:{}
        }    
    },
    computed: {
        ...mapGetters({
            user: 'user',
            device: 'device'
        })
    },
    methods:{
        ...mapActions({
            saveUser: 'saveUser',
            saveDevice: 'saveDevice'
        }),
        confirm: function(){
            this.$router.push({path:'/complete/complete',query:{type: true}})
        },
        quit: function(){
            this.saveUser(null);
            this.$router.push('/main');
        },
        async queryCustomer(){
            await this.$api.get('/customer/queryCustomerByCondition',{code:'12306'}).then(res=>{
                console.log("扫码结果获取的客户信息："+res);
                this.customerData=res.data[0];
                console.log(this.customerData);
            });
        },

        //获取客户这次接种的疫苗信息
        async queryPlan() {
            //console.log(obj);
            await this.$api.get('/plan/queryPlanByCondition', {id: "5dca60a147c905699496ef84"}).then(res => {
                console.log("疫苗接种计划："+res);
                this.vaccine = res.data[0];
                this.vaccineName = this.vaccine.vaccine;
            });
        },
        //获取疫苗价格信息
        async queryVaccinePrice(){
            await this.$api.get('/vaccinePrice/queryVaccinePrice',{name:"卡介苗"}).then(res=>{
                console.log("疫苗价格信息："+res);
                this.vaccinePrice=res.data[0];
            });
        }

    },
    mounted(){
        //根据扫码获得的客户code获取客户信息
        this.queryCustomer();
        //根据客户信息中的下次接种计划id获得plan表中的疫苗名称
        this.queryPlan();
        //获取疫苗价格信息
        this.queryVaccinePrice();
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/register.less';
</style>