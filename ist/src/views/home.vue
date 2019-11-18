<template>
    <div class="layout">
        <div class="header">
            <img src="/static/img/whiteLogo.png" alt="">
            <div>设备编号：7549360545</div>
        </div>
        <div class="main">
            <router-view></router-view>
        </div>
        
    </div>
</template>
<script>
export default {
    data () {
        return {
            
        }    
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/home.less';
</style>