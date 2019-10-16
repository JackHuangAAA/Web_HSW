<!--入库-->
<template>
    <div style="position:relative">
        <div class="black" v-if="addForm">
            <div class="addForm">
                <img src="/static/img/close.png" @click="cancel()">
                <div class="vaccineAddOne">
                    <p class="vaccineAddName">{{addVaccineOne}}:</p>
                    <Input v-model="vaccineOneCount" placeholder="请输入入库数量" style="width: 17.5625rem" class="addInput"/>
                </div>
                <div class="vaccineAddTwo">
                    <p class="vaccineAddName">{{addVaccineTwo}}:</p>
                    <Input v-model="vaccineTwoCount" placeholder="请输入入库数量" style="width: 17.5625rem" class="addInput"/>
                </div>
                <div class="cancel" @click="cancel()">
                    取消
                </div>
                <div class="addYes" @click="yes()">
                    确定
                </div>
            </div>
        </div>
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
                            <div class="cabine" v-for="(item,index) in cabineDatas" @click="addVaccine(index)">
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
    import {mapGetters} from 'vuex'

    export default {
        data() {
            return {
                cabineDatas: [{nameOne: '狂犬疫苗',nameTwo: '麻疹疫苗',countOne:100,countTwo:100},{},{},{}],
                index: '1',
                row: 0,
                addForm: false, //添加弹窗是否显示
                addVaccineOne: '',
                addVaccineTwo: '',
                vaccineOneCount: "",
                vaccineTwoCount: ""
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
            back: function(){
                this.$router.push('/main');
            },
            async queryDrawerByCondition(){
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    device: this.device._id
                });console.log("99------------>"+JSON.stringify(res.data));
                let array = res.data;
                for (let i = 0; i < 10; i++) {
                    let num = array[i].vaccine.length, vaccine = array[i].vaccine, temp = {};
                    if (num > 0) {
                        for (let k = 0; k < num; k++) {
                            if (k == 0) {
                                temp.nameOne = vaccine[k].name;
                                temp.countOne = vaccine[k].surplus;
                            }
                            if (k == 1) {
                                temp.nameTwo = vaccine[k].name;
                                temp.countTwo = vaccine[k].surplus;
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
            addVaccine: function(index){
                console.log(index);
                this.addForm = true;
                this.addVaccineOne = this.cabineDatas[index].nameOne;
                this.addVaccineTwo = this.cabineDatas[index].nameTwo;
            },
            cancel: function(){
                this.addForm = false;
            },
            yes: function(){
                this.addForm = false;
        },
        mounted() {
            this.row = Math.ceil(this.cabineDatas.length/2);
            this.queryDrawerByCondition();
        }
    }
    }
</script>
<style lang="less" scoped>
    @import "~@/style/inStock.less";
</style>
<style>
    .ivu-input{
        height: 2.5rem;
    }
</style>
