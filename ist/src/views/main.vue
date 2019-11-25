<template>
    <div class="container">
        <img src="/static/img/logo.png" class="logo" alt="">
        <div class="device">设备编号：{{device.id}}</div>
        <div class="title">智能接种，排队挂号，扫描疫苗本</div>
        <div class="scanNotice">
            <div>扫码挂号</div>
            <div>请将疫苗本条码对准扫描口，进行扫码</div>
        </div>
        <div class="awaitPeople">当前等待接种<span>20</span>人</div>
    </div>
</template>

<script>
import { mapGetters, mapActions, mapState } from 'vuex';

export default {
    data() {
        return {
               
        }
    },
    computed: {
        ...mapGetters({
            user: 'user',
            device: 'device'
        })
    },
    mounted(){
        this.$device.getDeviceCode().then(res=>{

        })
    }
}
</script>

<style lang="less" scoped>
@import '~@/style/main.less';
</style>
