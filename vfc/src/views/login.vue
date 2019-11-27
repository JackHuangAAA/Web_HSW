<template>
<div style="width:100%;height:100vh">
    <div class="container" v-if="!showMain">
        <div class="main">
            <div class="title">
                <img class="bg1" src="/static/img/loginp1.png">
                <img class="logo" src="/static/img/logo.png">
                <p class="logoP">银信疫苗冷藏柜</p>
                <p class="logoE">welcome to login system</p>
            </div>
            <div class="loginContent">
                <div class="select">
                    <p class="fingerLogin" @click="fingerLogin" :class="{'changeColor':show}">指纹登录</p>
                    <p class="accountLogin" @click="accountLogin" :class="{'changeColor':!show}">账号登录</p>
                </div>
                <div class="loginForm" :class="{'loginFormChange':!show}">
                    <fplogin @closeTimer="closeTimer" v-if="show" @click="test()"></fplogin>
                    <loginform v-if="!show" :state="state" @Submit="userLogin"></loginform>
                </div>
            </div>
            <img class="bg2" src="/static/img/loginp2.png">
        </div>
    </div>
    <div v-if="showMain" style="width:100%;height:100%;" @click="showLogin">
        <mainScreen style="background:#ecf0f8" :mainScreen="true" :temperature="parseFloat(temperature)" :temperatureDes="temperatureDes" />
    </div>
</div>
</template>

<script>
import loginform from '@/components/loginform'
import fplogin from '@/components/fplogin'
import mainScreen from '@/views/main'
import { mapGetters, mapActions } from "vuex";

export default {
    components: {
        loginform,
        fplogin,
        mainScreen
    },
    data() {
        return {
            show: true,
            state:true,
            showMain:true,
            timer:null,
            temperature:5,
            temperatureDes:'正常',
            audio:null,
            temperatureTimer:null
        };
    },
    computed: {
        ...mapGetters({
            user: "user",
            device: "device"
        })
    },
    created() {
        this.saveUser(null);
    },
    methods: {
        ...mapActions({
            saveUser:'saveUser',
            saveDevice: 'saveDevice'
        }),
        login(type) {
            switch (type) {
                case "fp":
                    this.show = true;
                    break;
                case "up":
                    this.show = false;
                    break;
                default:
                    break;
            }
        },
        showLogin(){
            this.showMain=false;
            clearInterval(this.temperatureTimer);
            this.timer=setTimeout(()=>{
                this.$device.un_fingerSearch();
                this.showMain=true;
                this.receiveTemperature();
                clearTimeout(this.timer);
            },180000);
        },
        closeTimer(){
            clearTimeout(this.timer);
        },
        async userLogin(form) {
            let res = await this.$api.get("/zcy/checkUser", form);
            if (res.data.check) {
                let user = await this.$api.post("/user/modifyUserByCode", {
                    code: res.data.code,
                    name: res.data.name,
                    type: 1 //医生
                });
                await this.saveUser(user.data);
                this.$device.openDoor().then(res=>{
                    console.log("开门结果 result:"+ JSON.stringify(res.rsp));
                    //结果为true门打开了
                })
                this.$router.push('/');
            }else{
                this.state=false;
            }
        },
        //主动请求温度信息
        queryTemperature(){
            console.log("CONTROLLER_BOARD===>TEMPERATURE");
            this.$device.temperature().then(res=>{
                console.log("第一次主动请求数据 result:"+JSON.stringify(res));
                let temp = '', val= JSON.parse(res.res).toFixed(1);
                if(val>8 || val<2){
                    this.audio.play();//异常语音播放
                    this.temperatureDes = '异常';
                }else{
                    this.temperatureDes = '正常';
                }
                this.temperature = val;
                //保存温度/警报到设备记录
                this.$api.post('/temperature/saveTemperatures',{
                    device:this.device._id,
                    deviceType:1, //1:接种柜;
                    unitCode:this.device.unitCode,
                    unitName:this.device.unitName,
                    temperature:val
                })
            })
        },
        receiveTemperature(){
            this.temperatureTimer=setInterval(()=>{
                this.queryTemperature();
            },60000);
        },
        //温度异常语音
        controllerAudio(){
            this.audio = new Audio();
            this.audio.src = '/static/audio/temperatureAbnormal.mp3';
        },
        fingerLogin() {
            this.show = true;
        },
        accountLogin() {
            this.show = false;
        }
    },
    destroyed(){
        clearInterval(this.temperatureTimer);
    },
    mounted() {
        //接收温度信息
        this.queryTemperature();
        this.controllerAudio();
        this.receiveTemperature();
    }
};
</script>

<style lang="less" scoped>
@import "~@/style/login.less";
</style>