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
                            <div class="cabine" v-for="(item,index) in cabineDatas">
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
    export default {
        data() {
            return {
                cabineDatas: [{nameOne: '狂犬疫苗',nameTwo: '麻疹疫苗',countOne:100,countTwo:100},{},{},{}],
                index: '1',
                row: 0,
                addVaccineOne: '',
                addVaccineTwo: '',
                vaccineOneCount: "",
                vaccineTwoCount: ""
            }
        },
        computed: {

        },
        components:{},
        methods: {

        },
        mounted() {
            this.row = Math.ceil(this.cabineDatas.length/2);
            //给数组添加checkbox 来判断是否选中 选中为true 未选中为false
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/stock.less";
</style>
