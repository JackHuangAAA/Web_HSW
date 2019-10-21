<!--入库-->
<template>
    <div style="position:relative">
        <div class="container">
            <div class="inStockTitle">
                    <img src="/static/img/back.png" class="back" @click="back()">
                    <p class="headP">请将入库疫苗扫码</p>
                    <img src="/static/img/vaccineInTip.png" class="vaccineIn">
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
                                <Radio v-model="item.single" class="checkBox" @on-change="changeRadio(index)" v-if="item.nameOne"></Radio >
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
                        确定
                    </div>
                    <div class="cancel" @click="back()">
                        取消
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
                checkDrawerId:''
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
            back(){
                this.$router.push('/main');
            },
            changeRadio(index){
                let i=0;
                this.cabineDatas.forEach(item =>{
                    if(index != i){
                        item.single = false;
                    }else{
                        //选中抽屉id
                        this.checkDrawerId = item.id;
                    }
                    i++;
                })
            },
            next(){
                this.$router.push({ path: '/inout/inStockCount', query: { drawerId: this.checkDrawerId }});
            }/*,
            async modifyVaccineNum(params){
                await this.$api.post("/vaccine/modifyVaccineNum", params);
            },
            async saveInout(params){
                await this.$api.post("/inout/saveInout", params);
            },
            async inStock(){
                //抽屉1号格
                if(this.vaccineOneId){
                    await this.modifyVaccineNum({
                        id: this.vaccineOneId,
                        total: this.vaccineOneCount,
                        surplus: this.vaccineOneCount
                    });
                    await this.saveInout({
                        ...this.commonData,
                        x: this.vaccineOneX,
                        y: this.vaccineOneY,
                        code: this.vaccineOneCode,
                        name: this.addVaccineOne,
                        total: this.vaccineOneCount,
                        surplus: this.vaccineOneCount
                    });
                }
                //抽屉2号格
                if(this.vaccineTwoId){
                    await this.modifyVaccineNum({
                        id: this.vaccineTwoId,
                        total: this.vaccineTwoCount,
                        surplus: this.vaccineTwoCount
                    });
                    await this.saveInout({
                        ...this.commonData,
                        x: this.vaccineTwoX,
                        y: this.vaccineTwoY,
                        code: this.vaccineTwoCode,
                        name: this.addVaccineTwo,
                        total: this.vaccineTwoCount,
                        surplus: this.vaccineTwoCount
                    });
                }
                this.queryDrawerByCondition();
                this.addForm = false;
            },
            finishInStock(){
                this.$router.push({ path: '/inout/detail', query: { action: 'in'} });
            }*/
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
