<template>
    <div class="container">
        <img src="static/img/whiteLogo.png" class="logo">
        <div class="code">设备编号：SC0003</div>
        <p class="title">{{title}}</p>
        <div class="main">
            <router-view></router-view>
        </div>
        <p class="pageEnd">银信博荣疫苗接种一体化解决方案</p>
    </div>
</template>
<script>
export default {
    data () {
        return {
            title: ''
        }    
    },
    mounted(){
        //console.log(this.$route.path)
        //this.title = '叫号综合显示屏';
        //this.$router.push('/display/queue');
        //this.title = '1号登记台';
        //this.$router.push('/display/desk');
        //this.title = '留观显示屏';
        //this.$router.push('/display/check');
    },
    created(){
        console.log("created")
        __app.$on("setTitle",msg=>{
            console.log(msg)
            this.title = msg
        })
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/displayHome.less';
</style>