<template>
    <div class="layout">
        <div class="header">
            <img src="/static/img/whiteLogo.png" alt="">
            <div>设备编号：7549360545</div>
        </div>
        <div class="main">
            <router-view></router-view>
        </div>
        <div class="copyright">银信博荣疫苗接种一体化解决方案</div>
    </div>
</template>
<script>
export default {
    data () {
        return {
            
        }    
    },
    mounted(){
        this.$router.push('/register/register')
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/home.less';
</style>