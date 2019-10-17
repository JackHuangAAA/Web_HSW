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
                                <div class="noVaccine" v-show=item.ifOneChoose>
                                    <Select v-model="item.vaccineOne" class="chooseVaccine"  @on-change="selectOne(index)">
                                        <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                    </Select>
                                </div>
                                <div class="noVaccineTwo" v-show=item.ifTwoChoose>
                                    <Select v-model="item.vaccineTwo" class="chooseVaccine"  @on-change="selectTwo(index)">
                                        <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                    </Select>
                                </div>
                                <div class="vaccineName" v-show=item.ifVaccineOne>
                                    麻疹疫苗
                                </div>
                                <div class="vaccineNameTwo" v-show=item.ifVaccineTwo>
                                    狂犬疫苗
                                </div>
                                <img src="/static/img/add.png" class="addImg" @click="addVaccine(index)" v-show=item.ifOneChoose>
                                <img src="/static/img/add.png" class="addImg2" @click="addVaccineTwo(index)" v-show=item.ifTwoChoose>
                                <img src="/static/img/del.png" class="delImg" v-show=item.ifVaccineOne @click="delVaccineOne(index)">
                                <img src="/static/img/del.png" class="delImgTwo" v-show=item.ifVaccineTwo @click="delVaccineTwo(index)">
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
                cabineData: [{},{},{},{},{},{},{},{}],
                cityList: [{value:22,label:22},{value:33,label:33}],
                // ifAdd: false,
                // ifVaccineOne: false,
                // ifVaccineTwo: false,
                // ifOneChoose: true,
                // ifTwoChoose: false,
                // ifAddTwo: false
            };
        },
        computed: {
            ...mapGetters({
                device: "device"
            })
        },
        methods: {
            addVaccine: function(index){
                if(this.cabineData[index].ifAdd == true){
                    this.cabineData[index].ifOneChoose = false;
                    this.cabineData[index].ifVaccineOne = true;
                    this.cabineData[index].ifTwoChoose = true;
                }
                this.$forceUpdate()
                console.log(this.cabineData);
            },
            addVaccineTwo: function(index){
                if(this.cabineData[index].ifAddTwo == true){
                    this.cabineData[index].ifTwoChoose = false;
                    this.cabineData[index].ifVaccineTwo = true;
                }
                this.$forceUpdate()
            },
            selectOne: function(index){
                if(this.cabineData[index].vaccineOne !== "" && this.cabineData[index].vaccineOne !== undefined){
                    this.cabineData[index].ifAdd = true;
                }
            },
            selectTwo: function(index){
                if(this.cabineData[index].vaccineTwo !== "" && this.cabineData[index].vaccineTwo !== undefined){
                    this.cabineData[index].ifAddTwo = true;
                }
            },
            delVaccineOne: function(index){
                if(this.cabineData[index].ifTwoChoose == true){
                    this.cabineData[index].ifOneChoose = true;
                    this.cabineData[index].ifVaccineOne = false;
                    this.cabineData[index].ifTwoChoose = false;
                     this.cabineData[index].vaccineOne = "";
                }
                this.cabineData[index].ifAdd = false;
                this.$forceUpdate()
            },
            delVaccineTwo: function(index){
                this.cabineData[index].ifVaccineTwo = false;
                this.cabineData[index].ifTwoChoose = true;
                this.cabineData[index].vaccineTwo = "";
                this.cabineData[index].ifAddTwo = false;
                console.log(index + '--' +this.cabineData[index].ifAddTwo);
                this.$forceUpdate()
            }
        },
        mounted() {
            this.cabineData.forEach(item =>{
                item.vaccineOne = "";
                item.vaccineTwo = "";
                item.ifAdd = false;        //第一个加号是否可点击
                item.ifAddTwo = false;     //第二个加号是否可点击
                item.ifOneChoose = true;   //第一个select是否显示
                item.ifTwoChoose = false;  //第二个select是否显示
                item.ifVaccineOne = false; //第一个已选择疫苗是否显示
                item.ifVaccineTwo = false; //第二个已选择疫苗是否显示
            })
            this.$forceUpdate()
        },
    };
</script>

<style lang="less" scoped>
    @import "~@/style/region.less";
</style>