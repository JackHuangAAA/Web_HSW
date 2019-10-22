<template>
    <div class="container">
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
                    <fplogin v-if="show" @click="test()"></fplogin>
                    <loginform v-if="!show" @Submit="userLogin"></loginform>
                </div>
            </div>
                <img class="bg2" src="/static/img/loginp2.png">
        </div>
    </div>
</template>

<script>
import loginform from '@/components/loginform'
import fplogin from '@/components/fplogin'
import { mapGetters, mapActions } from "vuex";

export default {
    components: {
        loginform,
        fplogin
    },
    data() {
        return {
            show: true
        };
    },
    computed: {
        ...mapGetters({
            user: "user",
            device: "device"
        })
    },
    created() { },

    methods: {
        ...mapActions(["saveUser"]),
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
        async userLogin(form) {
            form = {
                code: 'admin',
                password: '000000',

            };
            let res = await this.$api.get("/zcy/checkUser", form);
            if (res.data.check) {
                let user = await this.$api.post("/user/modifyUserByCode", {
                    code: res.data.code,
                    name: res.data.name,
                    type: 1 //医生
                });
                await this.saveUser(user.data);
                this.$router.push('/');
            }
        },
        fingerLogin() {
            this.show = true;
        },
        accountLogin() {
            this.show = false;
        },
        //接收指纹比对结果
        checkFinger(){
            /*this.$device.subscribe("FINGER_RESULT", (res) => {
                console.log('SERVER_PUSH==>FINGER_RESULT');
            );*/
        }
    },
    mounted() {
        //this.$device.subscribe("SCANNER_RESULT", this.SCANNER());
        //this.$device.subscribe("SOCKET_DATA", this.SOCKET());
        //指纹登录 todo
        this.checkFinger();
    },
};
</script>

<style lang="less" scoped>
@import "~@/style/login.less";
</style>