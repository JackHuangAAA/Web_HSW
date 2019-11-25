<template>
    <div class="">
        <div class="complete-info">
                <div class="complete-info-row">
                    <div>疫苗：<span>{{data?data.vaccine.name:''}}</span></div>
                    <div>批次号：<span>{{data?data.vaccine.batchNo:''}}</span></div>
                    <div>疫苗剂量：<span>{{data?data.vaccine.dosage:''}}</span></div>
                    <div>监管码：<span>{{data?data.vaccine.supervisionCode:''}}</span></div>
                </div>
                <div class="complete-info-row">
                    <div>有效期：<span>2019-09-18 12:30:28   至  {{data?data.vaccine.expiry:''}}</span></div>
                    <div>生产厂家：<span>{{data?data.vaccine.product:''}}</span></div>
                    <div>费用：<span>Y{{data?data.vaccine.cost:''}}</span></div>
                </div>
            </div>
    </div>
</template>
<script>
import { mapGetters, mapActions, mapState } from 'vuex';
export default {
    data () {
        return {
            data:{}
        }    
    },
    props:{
        datas: Object
    },
    computed: {
        ...mapGetters({
            user: 'user',
            device: 'device'
        })
    },
    methods: {
        ...mapActions({
            saveUser: 'saveUser',
            saveDevice: 'saveDevice'
        }),
        initData(){
            //从vuex user 里获取接种数据
            this.data = this.user;
        }
    },
    mounted(){
        //初始接种数据
        this.initData();
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/detail.less';
</style>