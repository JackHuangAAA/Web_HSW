<template>
    <div class="container">
        <img src="/static/img/whiteLogo.png" class="logo">
        <div class="code">设备编号：{{deviceId}}</div>
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
            deviceId: '',
            title: ''
        }    
    },
    mounted(){
<<<<<<< HEAD
        console.log(this.$route.path)
        this.title = '叫号综合显示屏';
        this.$router.push('/display/queue');
=======
        //console.log(this.$route.path)
        //this.title = '叫号综合显示屏';
        //this.$router.push('/display/queue');
>>>>>>> develop
        //this.title = '1号登记台';
        //this.$router.push('/display/desk');
        //this.title = '留观显示屏';
        //this.$router.push('/display/check');
<<<<<<< HEAD
=======
    },
    created(){
        console.log("created")
        __app.$on("setTitle",msg=>{
            console.log(msg)
            this.title = msg.title;
            this.deviceId = msg.deviceId;
        })
>>>>>>> develop
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/displayHome.less';
</style>