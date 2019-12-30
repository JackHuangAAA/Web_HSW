<template>
    <div class="regcontainer">
        <div class="box">
            <div class="title">基本信息</div>
            <div class="userInfo">
                <div>姓名：<span>{{customer?customer.name:''}}</span></div>
                <div>性别：<span>{{customer?customer.sex:''}}</span></div>
                <div>年龄：<span>{{customer?customer.age:''}}周岁</span></div>
                <div>距离上次接种时间：<span class="yellow">{{customer?customer.intervalTime:''}}天</span></div>
            </div>
        </div>
        <div class="box vaccine-choose">
            <div class="title">请选择接种疫苗</div>
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
                            <!--<Checkbox v-model="self" value="" label=""> </Checkbox>-->
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
                            <div>自费￥<span>{{paidVaccineData?paidVaccineData.cost:''}}</span></div>
                            <Checkbox v-model="single" value="" label=""> </Checkbox>

                        </div>
                    </div>  
                </div>
            <div class="btn-box">
                <div class="cancel" @click="quit()">退出</div>
                <div class="confirm" @click="confirm()">确认</div>
            </div>
        </div>
    </div>
</template>
<script>
import { mapGetters, mapActions, mapState } from 'vuex';

export default {
    data () {
        return {
            single:false,
            paidVaccineData:{},
            freeVaccineData:{},
            customer:{}
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
        confirm(){
            let customerVaccine = {}

            customerVaccine.customer = this.customer

            if(this.single){
                this.$router.push({path:'/pay/pay',query:{type: this.single}});
                customerVaccine.vaccine = this.paidVaccineData;
                console.log(this.paidVaccineData)
            }else{
                this.$router.push({path:'/register/free'});
                customerVaccine.vaccine = this.freeVaccineData;
                console.log(this.freeVaccineData)
            }
            this.saveUser(customerVaccine);
        },
        quit(){
            this.saveUser(null);
            this.$router.push('/main');
        },
        async queryCustomer(){
            //获取主页传过来的扫码内容
            let customer = this.$route.query.customer;
            this.customer = customer.data[0];

            let planId = customer.data[0].next.plan;
            //计算距离上次接种的时间差

            let previou_time = customer.data[0].previou.date?customer.data[0].previou.date:new Date();
            this.customer.intervalTime =Math.floor( (new Date().getTime()-new Date(previou_time).getTime())/(24*3600*1000));
            let plan = await this.$api.get('/plan/queryPlanByCondition', {id:planId,test:0});
            let vaccineName = plan.data[0].vaccine;

            let vaccinePrice =await this.$api.get('/vaccinePrice/queryVaccinePrice',{name:vaccineName,test:0});
            this.vaccine = vaccinePrice.data;
            this.paidVaccineData=vaccinePrice.data.paid;
            this.freeVaccineData=vaccinePrice.data.free;
        }
    },
    mounted(){
        //根据扫码获得的客户code获取客户信息
        this.queryCustomer();
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/register.less';
</style>