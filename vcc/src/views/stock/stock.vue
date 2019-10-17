<!--库存-->
<template>
    <div class="container">
        <div class="main">
                <div class="mainTop">
                    <p class="tip">提示：点击疫苗所在抽屉，查看库存详情</p>
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
                            <div class="cabine" v-for="(item,index) in cabineDatas" @click="detail(item.id)">
                                <div class="cabineLeft" v-if="item.nameOne">
                                    <p class="vaccineOneName">{{item.nameOne}}</p>
                                    <p class="vaccineOneCount">{{item.countOne||0}}支</p>
                                </div>
                                <div class="cabineRight" v-if="item.nameTwo">
                                    <p class="vaccineTwoName">{{item.nameTwo}}</p>
                                    <p class="vaccineTwoCount">{{item.countTwo||0}}支</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
    </div>
</template>
<script>
    import {mapGetters} from 'vuex';
    export default {
        data() {
            return {
                cabineDatas: [],
                index: '1',
                row: 5
            }
        },
        computed: {
            ...mapGetters({
                device: 'device',
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
                for (let i = 0; i < 10; i++) {
                    let num = array[i].vaccine.length, vaccine = array[i].vaccine, temp = {};
                    temp.id = array[i]._id;
                    temp.x = array[i].x;
                    temp.y = array[i].y;
                    if (num > 0) {
                        for (let k = 0; k < num; k++) {

                            if (k == 0) {
                                temp.nameOne = vaccine[k].name;
                                temp.countOne = vaccine[k].surplus;
                                temp.idOne = vaccine[k]._id;
                                temp.codeOne = vaccine[k].code;
                            }
                            if (k == 1) {
                                temp.nameTwo = vaccine[k].name;
                                temp.countTwo = vaccine[k].surplus;
                                temp.idTwo = vaccine[k]._id;
                                temp.codeTwo = vaccine[k].code;
                            }
                        }
                    } else {
                        temp.nameOne = '';
                        temp.countOne = '';
                        temp.nameTwo = '';
                        temp.countTwo = '';
                    }
                    this.cabineDatas.push(temp);
                }
            },
            detail(drawerId){
                this.$router.push({ path: '/stock/stockDetail', query: { drawerId: drawerId} });
            }
        },
        mounted() {
            this.queryDrawerByCondition();
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/stock.less";
</style>
