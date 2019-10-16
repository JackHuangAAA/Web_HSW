<template>
    <div class="container">
        <div class="main">
            <div class="title">
                <img class="bg1" src="/static/img/loginp1.png">
                <img class="logo" src="/static/img/logo.png">
                <p class="logoP">银信疫苗接种平台</p>
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
            <!-- <div class="footer">
                <img class="bg2" src="/static/img/loginp2.png">
            </div> -->
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
        SOCKET(cb) {
            console.log(cb);
        },
        SCANNER(cb) {
            console.log(cb);
        },
        async userLogin(form) {
            form = {
                code: 'admin',
                password: '000000',

            }
            let res = await this.$api.get("/zcy/checkUser", form);
            if (res.data.check) {
                let user = await this.$api.post("/user/modifyUserByCode", {
                    code: res.data.code,
                    name: res.data.name
                });
                await this.saveUser(user.data);
                this.$router.push('/');
            }
        },
        fingerLogin: function() {
            this.show = true;
        },
        accountLogin: function() {
            this.show = false;
        }
    },
    mounted() {
        //this.$device.subscribe("SCANNER_RESULT", this.SCANNER());
        //this.$device.subscribe("SOCKET_DATA", this.SOCKET());
    },
};
</script>

<style lang="less" scoped>
@import "~@/style/login.less";
</style>