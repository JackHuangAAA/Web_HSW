<!--入库-->
<template>
    <div>
        <div class="container">
            <div class="inStockTitle">
                    <img src="/static/img/back.png" class="back" @click="back()">
                    <p class="headP">请选择疫苗入库的抽屉</p>
                    <img src="/static/img/vaccineIn.png" class="vaccineIn">
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
                            <div class="cabine" v-for="(item,index) in cabineData">
                                <div class="cabineLeft">
                                    <p class="vaccineOneName">{{item.nameOne}}</p>
                                    <p class="vaccineOneCount">{{item.countOne||0}}支</p>
                                </div>
                                <div class="cabineRight">
                                    <p class="vaccineTwoName">{{item.nameTwo}}</p>
                                    <p class="vaccineTwoCount">{{item.countTwo||0}}支</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="finish">
                        <div class="finishButton">
                            完成
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                cabineData: [{nameOne: '狂犬疫苗',nameTwo: '麻疹疫苗',countOne:100,countTwo:100},{},{},{}],
                index: '1',
                row: 0
            }
        },
        computed: {

        },
        components:{},
        methods: {
            back: function(){
                this.$router.push('/main');
            }
        },
        mounted() {
            this.row = Math.ceil(this.cabineData.length/2);
            console.log(this.row);
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/inStock.less";
</style>