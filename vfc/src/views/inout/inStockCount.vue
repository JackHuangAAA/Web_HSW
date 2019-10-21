<template>
    <div style="position:relative">
        <div class="block" v-if="ifTip">
            <div class="tips" v-if="ifTip">
                <img src="/static/img/close.png" class="close" @click="closeTip()">
                <div class="tipsTitle">
                    <img src="/static/img/info.png" class="info">
                    <p class="tipsTitleP">百白破疫苗</p>
                </div>
                <div class="tipP">
                    YH5683098批次有效期失效请将此疫苗报废处理
                </div>
                <div class="tipsYes" @click="closeTip()">
                    确定
                </div>
            </div>
        </div>
        <div class="container">
            <div class="inStockTitle">
                    <img src="/static/img/back.png" class="back" @click="back()">
                    <p class="headP">请将入库疫苗扫码</p>
                    <img src="/static/img/vaccineInTwo.png" class="vaccineIn">
            </div>
            <div class="main">
                <div class="title">
                    <div class="index">
                        序号
                    </div>
                    <div class="vaccineName">
                        疫苗名称
                    </div>
                    <div class="code">
                        批次号
                    </div>
                    <div class="coordinate">
                        位置信息
                    </div>
                    <div class="count">
                        入库数量
                    </div>
                </div>
                <div class="table">
                    <div v-for="(item,index) in tableDatas" @click="selectVaccine(index)" class="tableData" :class="{clicked: clickIndex==index}">
                        <div class="index">
                            <span v-if="index>8">{{index+1}}</span>
                            <span v-if="index<8 || index ==8">0{{index+1}}</span>
                        </div>
                        <div class="vaccineName">
                            {{item.vaccineName}}
                        </div>
                        <div class="code">
                            {{item.code}}
                        </div>
                        <div class="coordinate">
                            {{item.site}}
                        </div>
                        <div class="count">
                            <input type="text" class="countInput" v-model="item.count">
                        </div>
                    </div>
                </div>
                <div class="button">
                    <div class="buttonBack" @click="back()">
                        继续入库
                    </div>
                    <div class="finish" @click="finish()">
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
                tableDatas: [{vaccineName: '百白破疫苗',code:'yHUG-7U940',site: '(1,1)',count:0},{vaccineName:'狂犬疫苗',code:'yHUG-7U940',site: '(1,1)',count:0}],
                clickIndex: 0,
                ifTip: false
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device',
            })
        },
        components:{},
        methods: {
            back(){
                this.$router.push('/inout/inStock');
            },
            closeTip: function(){
                this.ifTip = false;
            },
            finish: function(){
                this.$router.push('/inout/inStockEnd');
            },
            selectVaccine: function(index){
                this.clickIndex = index;
                this.ifTip = true;
            }
        },
        mounted() {

        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/inStockCount.less";
</style>
<style>
    .ivu-input{
        height: 2.5rem;
    }
</style>
