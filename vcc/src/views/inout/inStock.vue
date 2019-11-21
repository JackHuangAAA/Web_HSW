<!--入库-->
<template>
    <div style="position:relative">
        <div class="black" v-if="addForm">
            <div class="addForm">
                <img src="/static/img/close.png" @click="cancel()">
                <div class="addForm-content">

                
                    <div class="vaccineAddOne" v-if="addVaccineOne">
                        <p class="vaccineAddName">{{addVaccineOne}}:</p>
                        <Input v-model="vaccineOneCount" placeholder="请输入入库数量" style="width: 17.5625rem" class="addInput"/>
                    </div>
                    <div class="vaccineAddTwo" v-if="addVaccineTwo">
                        <p class="vaccineAddName">{{addVaccineTwo}}:</p>
                        <Input v-model="vaccineTwoCount" placeholder="请输入入库数量" style="width: 17.5625rem" class="addInput"/>
                    </div>
                    <div class="vaccineAddThree" v-if="addVaccineThree">
                        <p class="vaccineAddName">{{addVaccineThree}}:</p>
                        <Input v-model="vaccineThreeCount" placeholder="请输入入库数量" style="width: 17.5625rem" class="addInput"/>
                    </div>
                    <div class="vaccineAddFour" v-if="addVaccineFour">
                        <p class="vaccineAddName">{{addVaccineFour}}:</p>
                        <Input v-model="vaccineFourCount" placeholder="请输入入库数量" style="width: 17.5625rem" class="addInput"/>
                    </div>
                </div>
                <div class="addForm-choose-btn">
                    <div class="cancel" @click="cancel()">
                        取消
                    </div>
                    <div class="addYes" @click="inStock()">
                        确定
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <div class="inStockTitle">
                    <img src="/static/img/back.png" class="back" @click="back()">
                    <p class="headP">请选择疫苗入库的抽屉</p>
                    <img src="/static/img/vaccineIn.png" class="vaccineIn">
            </div>
            {{"这是打开抽屉的返回数据："+JSON.stringify(test)}}
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
                                <div class="cabineLeft" v-if="item.nameThree">
                                    <p class="vaccineOneName">{{item.nameThree}}</p>
                                    <p class="vaccineOneCount">{{item.countThree||0}}支</p>
                                </div>
                                <div class="cabineRight" v-if="item.nameFour">
                                    <p class="vaccineTwoName">{{item.nameFour}}</p>
                                    <p class="vaccineTwoCount">{{item.countFour||0}}支</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="finish">
                        <div class="finishButton" @click="finishInStock">
                            完成
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <audio src="/static/audio/inputInStockCount.mp3" autoplay></audio>
    </div>
</template>
<script>
    import {mapGetters} from 'vuex';
    import uuid from 'uuid/v1';

    export default {
        data() {
            return {
                commonData: null,
                cabineDatas: [],
                index: '1',
                row: 5,
                addForm: false, //添加弹窗是否显示
                addVaccineOne: '', //抽屉1号格疫苗名称
                vaccineOneCode: '',//抽屉1号格疫苗编号
                addVaccineTwo: '',//抽屉2号格疫苗名称
                vaccineTwoCode: '',//抽屉2号格疫苗编号
                vaccineOneCount: null,//抽屉1号格疫苗数量
                vaccineTwoCount: null,//抽屉2号格疫苗数量
                vaccineOneId: '',//抽屉1号格疫苗id
                vaccineTwoId: '',//抽屉2号格疫苗id
                vaccineOneX: '',//抽屉1号格疫苗X
                vaccineTwoX: '',//抽屉2号格疫苗X
                vaccineOneY: '',//抽屉1号格疫苗Y
                vaccineTwoY: '',//抽屉2号格疫苗Y
                addVaccineThree: '', //抽屉3号格疫苗名称
                vaccineThreeCode: '',//抽屉3号格疫苗编号
                addVaccineFour: '',//抽屉4号格疫苗名称
                vaccineFourCode: '',//抽屉4号格疫苗编号
                vaccineThreeCount: null,//抽屉3号格疫苗数量
                vaccineFourCount: null,//抽屉4号格疫苗数量
                vaccineThreeId: '',//抽屉3号格疫苗id
                vaccineFourId: '',//抽屉4号格疫苗id
                vaccineThreeX: '',//抽屉3号格疫苗X
                vaccineFourX: '',//抽屉4号格疫苗X
                vaccineThreeY: '',//抽屉3号格疫苗Y
                vaccineFourY: '',//抽屉4号格疫苗Y
                endData:[],//完成的数据
                test:''
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
                for (let i = 0; i < 10; i++) {
                    let num = array[i].vaccine.length, vaccine = array[i].vaccine, temp = {};
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
                            if (k == 2) {
                                temp.nameThree = vaccine[k].name;
                                temp.countThree = vaccine[k].surplus;
                                temp.idThree = vaccine[k]._id;
                                temp.codeThree = vaccine[k].code;
                            }
                            if (k == 3) {
                                temp.nameFour = vaccine[k].name;
                                temp.countFour = vaccine[k].surplus;
                                temp.idFour = vaccine[k]._id;
                                temp.codeFour = vaccine[k].code;
                            }
                        }
                    } else {
                        temp.nameOne = '';
                        temp.countOne = '';
                        temp.nameTwo = '';
                        temp.countTwo = '';
                        temp.nameThree = '';
                        temp.countThree = '';
                        temp.nameFour = '';
                        temp.countFour = '';
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
                this.vaccineOneCount = null;
                this.vaccineTwoCount = null;
                this.vaccineOneId = this.cabineDatas[index].idOne;
                this.vaccineTwoId = this.cabineDatas[index].idTwo;
                this.vaccineOneCode = this.cabineDatas[index].codeOne;
                this.vaccineTwoCode = this.cabineDatas[index].codeTwo;
                this.vaccineOneX = this.cabineDatas[index].x;
                this.vaccineTwoX = this.cabineDatas[index].x;
                this.vaccineOneY = this.cabineDatas[index].y;
                this.vaccineTwoY = this.cabineDatas[index].y;

                this.addVaccineThree = this.cabineDatas[index].nameThree;
                this.addVaccineFour = this.cabineDatas[index].nameFour;
                this.vaccineThreeCount = null;
                this.vaccineFourCount = null;
                this.vaccineThreeId = this.cabineDatas[index].idThree;
                this.vaccineFourId = this.cabineDatas[index].idFour;
                this.vaccineThreeCode = this.cabineDatas[index].codeThree;
                this.vaccineFourCode = this.cabineDatas[index].codeFour;
                this.vaccineThreeX = this.cabineDatas[index].x;
                this.vaccineFourX = this.cabineDatas[index].x;
                this.vaccineThreeY = this.cabineDatas[index].y;
                this.vaccineFourY = this.cabineDatas[index].y;

                if(this.addVaccineOne || this.addVaccineTwo || this.addVaccineThree || this.addVaccineFour){
                    this.addForm = true;
                }
            },
            cancel(){
                this.addVaccineOne = '';
                this.addVaccineTwo = '';
                this.vaccineOneCount = null;
                this.vaccineTwoCount = null;
                this.addVaccineThree = '';
                this.addVaccineFour = '';
                this.vaccineThreeCount = null;
                this.vaccineFourCount = null;
                this.addForm = false;
            },
            async modifyVaccineNum(params){
                await this.$api.post("/vaccine/modifyVaccineNum", params);
            },
            async saveInout(params){
                await this.$api.post("/inout/saveInout", params);
            },
            async inStock(){
                //batchId为一次流水id(1次流水可能有2条入库记录)
                let batchId = uuid();
                //抽屉1号格
                if(this.vaccineOneId && this.vaccineOneCount>0){
                    await this.modifyVaccineNum({
                        id: this.vaccineOneId,
                        total: this.vaccineOneCount,
                        surplus: this.vaccineOneCount
                    });
                    await this.saveInout({
                        batchId: batchId,
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
                if(this.vaccineTwoId && this.vaccineTwoCount>0){
                    await this.modifyVaccineNum({
                        id: this.vaccineTwoId,
                        total: this.vaccineTwoCount,
                        surplus: this.vaccineTwoCount

                    });
                    await this.saveInout({
                        batchId: batchId,
                        ...this.commonData,
                        x: this.vaccineTwoX,
                        y: this.vaccineTwoY,
                        code: this.vaccineTwoCode,
                        name: this.addVaccineTwo,
                        total: this.vaccineTwoCount,
                        surplus: this.vaccineTwoCount
                    });
                }
                //抽屉3号格
                if(this.vaccineThreeId && this.vaccineThreeCount>0){
                    await this.modifyVaccineNum({
                        id: this.vaccineThreeId,
                        total: this.vaccineThreeCount,
                        surplus: this.vaccineThreeCount
                    });
                    await this.saveInout({
                        batchId: batchId,
                        ...this.commonData,
                        x: this.vaccineThreeX,
                        y: this.vaccineThreeY,
                        code: this.vaccineThreeCode,
                        name: this.addVaccineThree,
                        total: this.vaccineThreeCount,
                        surplus: this.vaccineThreeCount
                    });
                }
                //抽屉4号格
                if(this.vaccineFourId && this.vaccineFourCount>0){
                    await this.modifyVaccineNum({
                        id: this.vaccineFourId,
                        total: this.vaccineFourCount,
                        surplus: this.vaccineFourCount

                    });
                    await this.saveInout({
                        batchId: batchId,
                        ...this.commonData,
                        x: this.vaccineFourX,
                        y: this.vaccineFourY,
                        code: this.vaccineFourCode,
                        name: this.addVaccineFour,
                        total: this.vaccineFourCount,
                        surplus: this.vaccineFourCount
                    });
                }
                //入库的数据
                let flag=true,i_index=0;
                if(_.isEmpty(this.endData)){
                    if(this.addVaccineOne){
                        this.endData.push({name:this.addVaccineOne,
                        surplus:this.vaccineOneCount
                        });
                    }
                    if(this.addVaccineTwo){
                        this.endData.push( {name:this.addVaccineTwo,
                        surplus:this.vaccineTwoCount
                        },);
                    }
                    if(this.addVaccineThree){
                        this.endData.push({name:this.addVaccineThree,
                        surplus:this.vaccineThreeCount
                        },);
                    }
                    if(this.addVaccineFour){
                        this.endData.push({name:this.addVaccineFour,
                        surplus:this.vaccineFourCount
                        });
                    }
                }else{
                    if(this.addVaccineOne){
                        for(let i=0;i<this.endData.length;i++){
                            if(this.endData[i].name==this.addVaccineOne){
                                flag=false;
                                i_index=i;
                                break;
                            }
                        }
                        if(flag){
                            this.endData.push({name:this.addVaccineOne,
                                surplus:this.vaccineOneCount
                            });
                        }else{
                            if(this.vaccineOneCount!=null){
                                this.endData[i_index].surplus=parseInt(this.endData[i_index].surplus)+parseInt(this.vaccineOneCount);
                            }
                            flag=true;
                            i_index=0;
                        }
                    }
                    if(this.addVaccineTwo){
                        for(let i=0;i<this.endData.length;i++){
                            if(this.endData[i].name==this.addVaccineTwo){
                                flag=false;
                                i_index=i;
                                break;
                            }
                        }
                        if(flag){
                            this.endData.push( {name:this.addVaccineTwo,
                                surplus:this.vaccineTwoCount
                            });
                        }else{
                            if(this.vaccineTwoCount!=null){
                                this.endData[i_index].surplus=parseInt(this.endData[i_index].surplus)+parseInt(this.vaccineTwoCount);
                            }
                            flag=true;
                            i_index=0;
                        }
                    }
                    if(this.addVaccineThree){
                        for(let i=0;i<this.endData.length;i++){
                            if(this.endData[i].name==this.addVaccineThree){
                                flag=false;
                                i_index=i;
                                break;
                            }
                        }
                        if(flag){
                            this.endData.push({name:this.addVaccineThree,
                            surplus:this.vaccineThreeCount
                            });
                        }else{
                            if(this.vaccineThreeCount){
                                this.endData[i_index].surplus=parseInt(this.endData[i_index].surplus)+parseInt(this.vaccineThreeCount);
                            }
                            flag=true;
                            i_index=0;
                        }
                    }
                    if(this.addVaccineFour){
                        for(let i=0;i<this.endData.length;i++){
                            if(this.endData[i].name==this.addVaccineFour){
                                flag=false;
                                i_index=i;
                                break;
                            }
                        }
                        if(flag){
                            this.endData.push({name:this.addVaccineFour,
                            surplus:this.vaccineFourCount
                            });
                        }else{
                            if(this.vaccineFourCount){
                                this.endData[i_index].surplus=parseInt(this.endData[i_index].surplus)+parseInt(this.vaccineFourCount);
                            }
                            flag=true;
                            i_index=0;
                        }
                    }
                }
                //打开抽屉
                this.$device.openDrawer({num:this.vaccineOneX+"#"+this.vaccineOneY}).then(res=>{
                        console.log(res);
                        this.test=res;
                    }).catch(err=>{
                        console.log('open drawer error: '+err.message());
                    });
                console.log(this.endData);
                this.queryDrawerByCondition();
                this.addForm = false;
            },
            //打开指定抽屉
            openDrawer(){
                //获取选中的的抽屉
                //打开柜子的参数  num
                let ids = [], position = '', array = this.cabineDatas;
                for(let n=0;n<array.length;n++) {
                    if(array[n].single){
                        ids.push(array[n].drawerId);
                        position+=','+array[n].x+'#'+array[n].y
                    }
                }
                //position不为空时，调用Android接口，打开抽屉
                if(position!=''){
                    this.$device.openDrawer({num:position.slice(1)}).then(res=>{
                        this.$router.push({ path: '/inout/scanTip', query: { openDrawerIds: ids} });
                    }).catch(err=>{
                        console.log('open drawer error: '+err.message());
                    });
                }
            },
            finishInStock(){
                this.$router.push({ path: '/inout/detail', query: { action: 'in', inStockDate:this.endData} });
            }
        },
        mounted() {
            this.endData=[];
            this.commonData = {
                type: 1, //1:入库
                user: this.user._id,
                device: this.device._id,
                deviceType: 1, //1:接种柜
                unitCode: this.device.unitCode,
                unitName: this.device.unitName
            };
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
