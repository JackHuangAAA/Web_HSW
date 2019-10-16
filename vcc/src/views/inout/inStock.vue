<!--入库-->
<template>
    <div style="position:relative">
        <div class="black" v-if="addForm">
            <div class="addForm">
                <img src="/static/img/close.png" @click="cancel()">
                <div class="vaccineAddOne" v-if="addVaccineOne">
                    <p class="vaccineAddName">{{addVaccineOne}}:</p>
                    <Input v-model="vaccineOneCount" placeholder="请输入入库数量" style="width: 17.5625rem" class="addInput"/>
                </div>
                <div class="vaccineAddTwo" v-if="addVaccineTwo">
                    <p class="vaccineAddName">{{addVaccineTwo}}:</p>
                    <Input v-model="vaccineTwoCount" placeholder="请输入入库数量" style="width: 17.5625rem" class="addInput"/>
                </div>
                <div class="cancel" @click="cancel()">
                    取消
                </div>
                <div class="addYes" @click="inStock()">
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
                cabineDatas: [],
                index: '1',
                row: 5,
                addForm: false, //添加弹窗是否显示
                addVaccineOne: '',
                addVaccineTwo: '',
                vaccineOneCount: 0,
                vaccineTwoCount: 0,
                vaccineOneId: '',
                vaccineTwoId: '',
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
            async queryDrawerByCondition(){
                this.cabineDatas = [];
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    device: this.device._id
                });
                let array = res.data;
                for (let i = 0; i < 10; i++) {
                    let num = array[i].vaccine.length, vaccine = array[i].vaccine, temp = {};
                    if (num > 0) {
                        for (let k = 0; k < num; k++) {

                            if (k == 0) {
                                temp.nameOne = vaccine[k].name;
                                temp.countOne = vaccine[k].surplus;
                                temp.idOne = vaccine[k]._id;
                            }
                            if (k == 1) {
                                temp.nameTwo = vaccine[k].name;
                                temp.countTwo = vaccine[k].surplus;
                                temp.idTwo = vaccine[k]._id;
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
            back(){
                this.$router.push('/main');
            },
            addVaccine(index){
                this.addVaccineOne = this.cabineDatas[index].nameOne;
                this.addVaccineTwo = this.cabineDatas[index].nameTwo;
                this.vaccineOneCount = this.cabineDatas[index].countOne;
                this.vaccineTwoCount = this.cabineDatas[index].countTwo;
                this.vaccineOneId = this.cabineDatas[index].idOne;
                this.vaccineTwoId = this.cabineDatas[index].idTwo
                if(this.addVaccineOne || this.addVaccineTwo){
                    this.addForm = true;
                }
            },
            cancel(){
                this.addVaccineOne = '';
                this.addVaccineTwo = '';
                this.vaccineOneCount = 0;
                this.vaccineTwoCount = 0;
                this.addForm = false;
            },
            async inStock(){
                if(this.vaccineOneId){
                    await this.$api.post("/vaccine/modifyVaccine", {
                        id: this.vaccineOneId,
                        total: this.vaccineOneCount,
                        surplus: this.vaccineOneCount
                    });
                }
                if(this.vaccineTwoId){
                    await this.$api.post("/vaccine/modifyVaccine", {
                        id: this.vaccineTwoId,
                        total: this.vaccineTwoCount,
                        surplus: this.vaccineTwoCount
                    });
                }
                this.queryDrawerByCondition();
                this.addForm = false;
            }
        },
        mounted() {
            this.queryDrawerByCondition();
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/inStock.less";
</style>
<style>
    .ivu-input{
        height: 2.5rem;
    }
</style>
