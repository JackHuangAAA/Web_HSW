<!--区域划分-->
<template>
    <div class="container">
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
                                <!-- <Input v-model="value" placeholder="选择疫苗名称" class="chooseVaccine"/> -->
                                <div class="noVaccine" v-show="ifOneChoose">
                                    <Select v-model="value" class="chooseVaccine"  @on-change="selectOne()">
                                        <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                    </Select>
                                </div>
                                <div class="noVaccineTwo" v-show="ifTwoChoose">
                                    <Select v-model="value1" class="chooseVaccine"  @on-change="selectTwo()">
                                        <Option v-for="item in cityList2" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                    </Select>
                                </div>
                                <div class="vaccineName" v-show="ifVaccineOne">
                                    麻疹疫苗
                                </div>
                                <div class="vaccineNameTwo" v-show="ifVaccineTwo">
                                    狂犬疫苗
                                </div>
                                <img src="/static/img/add.png" class="addImg" @click="addVaccine(index)" v-show="ifOneChoose">
                                <img src="/static/img/add.png" class="addImg2" @click="addVaccineTwo(index)" v-show="ifTwoChoose">
                                <img src="/static/img/del.png" class="delImg" v-show="ifVaccineOne" @click="delVaccineOne()">
                                <img src="/static/img/del.png" class="delImgTwo" v-show="ifVaccineTwo" @click="delVaccineTwo()">
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
</template>

<script>
    import { mapGetters } from "vuex";
    export default {
        data() {
            return {
                row: 0,
                value: '',
                value1: '',
                cabineData: [{},{},{},{},{},{},{},{}],
                cityList: [{value:22,label:22},{value:33,label:33}],
                cityList2: [{value:22,label:'q'},{value:33,label:'a'}],
                ifAdd: false,
                ifVaccineOne: false,
                ifVaccineTwo: false,
                ifOneChoose: true,
                ifTwoChoose: false,
                ifAddTwo: false
            };
        },
        computed: {
            ...mapGetters({
                device: "device"
            })
        },
        methods: {
            addVaccine: function(index){
                if(this.ifAdd == true){
                    this.ifOneChoose = false;
                    this.ifVaccineOne = true;
                    this.ifTwoChoose = true;
                    // this.value = "";
                    // this.ifAdd = false;
                }
            },
            addVaccineTwo: function(){
                if(this.ifAddTwo == true){
                    this.ifTwoChoose = false;
                    this.ifVaccineTwo = true;
                }
            },
            selectOne: function(){
                this.ifAdd = true;
            },
            selectTwo: function(){
                console.log('1111111');
                // console.log('test:' + this.ifAddTwo);
                this.ifAddTwo = true;
            },
            delVaccineOne: function(){
                if(this.ifTwoChoose == true){
                    this.ifOneChoose = true;
                    this.ifVaccineOne = false;
                    this.ifTwoChoose = false
                }
            },
            delVaccineTwo: function(){
                this.ifVaccineTwo = false;
                this.ifTwoChoose = true;
                this.value1 = "";
            }
        },
        mounted() {

        },
    };
</script>

<style lang="less" scoped>
    @import "~@/style/region.less";
</style>