<!--入库对应位置展示-->
<template>
    <div style="position:relative">
        <div class="container">
            <div class="inStockTitle">
                <p class="headP">请将疫苗放入对应位置</p>
                <img src="/static/img/inCabinet2.png" class="vaccineIn">
            </div>
            <div class="main">
                <div class="mainTop">
                    <p class="ctOne">抽屉1</p>
                    <p class="ctLeft">左</p>
                    <img src="/static/img/l-r.png" class="l_r">
                    <p class="ctRight">右</p>
                    <p class="ctTwo">抽屉2</p>
                </div>
                <div class="mainBottom">
                    <div class="mainBottomLeft">
                        <p class="ctTop">上</p>
                        <img src="/static/img/t-b.png" class="t_b">
                        <p class="ctBottom">下</p>
                    </div>
                    <div class="mainBottomRight">
                        <div class="index">
                            <p class="indexBlock" v-for="(item,index) in row"><span class="indexSpan">第{{index+1}}行</span></p>
                        </div>
                        <div class="cabines">
                            <div class="cabine" v-for="(item,index) in cabineDatas">
                                <div class="cabineLeft" v-if="item.nameOne">
                                    <p class="vaccineOneName">{{item.nameOne}}</p>
                                </div>
                                <div class="cabineRight" v-if="item.nameTwo">
                                    <p class="vaccineTwoName">{{item.nameTwo}}</p>
                                </div>
                                <div class="cabineLeft" v-if="item.nameThree">
                                    <p class="vaccineOneName">{{item.nameThree}}</p>
                                </div>
                                <div class="cabineRight" v-if="item.nameFour">
                                    <p class="vaccineTwoName">{{item.nameFour}}</p>
                                </div>
                                <div class="cabineLeft" v-if="item.nameFive">
                                    <p class="vaccineOneName">{{item.nameFive}}</p>
                                </div>
                                <div class="cabineRight" v-if="item.nameSix">
                                    <p class="vaccineTwoName">{{item.nameSix}}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="finish">
                    <div class="yes" @click="next()">
                        入库完成
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import {mapGetters} from 'vuex'
    export default {
        data() {
            return {
                cabineDatas: [],
                index: '1',
                row: 6,
                checkDrawerId:'',
                datas: null
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device'
            })
        },
        components:{},
        methods: {
            async queryDrawerByCondition(){
                this.cabineDatas = [];
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    device: this.device._id
                });
                let array = res.data;
                for (let i = 0; i < 12; i++) {
                    let num = array[i].vaccine.length, vaccine = array[i].vaccine, temp = {};
                    temp.id = array[i]._id;
                    temp.single = false;
                    if (num > 0) {
                        for (let k = 0; k < num; k++) {
                            if (k == 0) {temp.nameOne = vaccine[k].name;}
                            if (k == 1) {temp.nameTwo = vaccine[k].name;}
                            if (k == 2) {temp.nameThree = vaccine[k].name;}
                            if (k == 3) {temp.nameFour = vaccine[k].name;}
                            if (k == 4) {temp.nameFive = vaccine[k].name;}
                            if (k == 5) {temp.nameSix = vaccine[k].name;}
                        }
                    } else {
                        temp.nameOne = '';
                    }
                    this.cabineDatas.push(temp);
                }
            },
            next(){
                this.$router.push({ path: '/inout/inStockEnd', query: { datas: this.datas }});
            }
        },
        mounted() {
            this.datas = this.$route.query.datas;
            this.queryDrawerByCondition();
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/inPosition.less";
</style>
<style>
    .ivu-input{
        height: 2.5rem;
    }
</style>
