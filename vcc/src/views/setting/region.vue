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
                                 <!-- <div class="noVaccine" v-show=item.ifOneChoose>
                                    <Select v-model="item.vaccineOne" class="chooseVaccine" :label-in-value="true" @on-change="selectOne(index,$event)">
                                        <Option v-for="item in kindList" :value="item.code" :key="item.code">{{ item.name }}</Option>
                                    </Select>
                                </div>
                                <div class="noVaccineTwo" v-show=item.ifTwoChoose>
                                    <Select v-model="item.vaccineTwo" class="chooseVaccine" :label-in-value="true" @on-change="selectTwo(index,$event)">
                                        <Option v-for="item in kindList" :value="item.code" :key="item.code">{{ item.name }}</Option>
                                    </Select>
                                </div>
                                <div class="vaccineName" v-show=item.ifVaccineOne>
                                    {{item && item.vaccine && item.vaccine[0] && item.vaccine[0].name}}
                                </div>
                                <div class="vaccineNameTwo" v-show=item.ifVaccineTwo>
                                    {{item && item.vaccine && item.vaccine[1] && item.vaccine[1].name}}
                                </div>
                                <img src="/static/img/add.png" class="addImg" v-show="item.ifOneChoose" @click="addVaccine(index)">
                                <img src="/static/img/add.png" class="addImg2" v-show="item.ifTwoChoose" @click="addVaccineTwo(index)">
                                <img src="/static/img/del.png" class="delImg" v-show="item.ifVaccineOne" @click="delVaccineOne(index)">
                                <img src="/static/img/del.png" class="delImgTwo" v-show="item.ifVaccineTwo" @click="delVaccineTwo(index)">
                                -->
                                <div class="vaccineBox">
                                    <div class="vaccineBox-list" v-for="(item,index) of 4">
                                        <div>asdasd</div>
                                        <div>
                                            <img src="/static/img/del.png">
                                        </div>
                                        
                                    </div>
                                    <Select class="chooseVaccine" :label-in-value="true" @on-change="selectOne(index,$event)">
                                        <Option v-for="item in kindList" :value="item.code" :key="item.code">{{ item.name }}</Option>
                                    </Select>
                                    <img src="/static/img/add.png" class="boxAdd" @click="addVaccine(index)">
                                    
                                </div>
                            </div> 
                        </div>
                    </div>
                    <!--<div class="finish">
                        <div class="finishButton">
                            完成
                        </div>
                    </div>-->
                </div>
    </div>
</template>

<script>
    import { mapGetters } from "vuex";
    export default {
        data() {
            return {
                row: 0,
                cabineData: [],
                kindList: [],
                vaccineOneName:'',
                vaccineTwoName:''
            };
        },
        computed: {
            ...mapGetters({
                device: "device"
            })
        },
        methods: {
            async addVaccine(index){
                //显示第二个select
                if(this.cabineData[index].ifAdd == true){
                    //增加疫苗信息到DB
                    let drawer = this.cabineData[index];
                    await this.$api.post("/drawer/modifyDrawerById", {
                        id: drawer._id,
                        vaccine: {
                            device: this.device._id,   //设备
                            code: drawer.vaccineOne,   //疫苗编号
                            name: this.vaccineOneName  //疫苗名称
                        }
                    });
                    this.queryDrawerByCondition();
                }
                //this.$forceUpdate();
            },
            async addVaccineTwo(index){
                //显示第2个疫苗可以删除标记
                if(this.cabineData[index].ifAddTwo == true){
                    //增加疫苗信息到DB
                    let drawer = this.cabineData[index];
                    await this.$api.post("/drawer/modifyDrawerById", {
                        id: drawer._id,
                        vaccine: {
                            device: this.device._id,   //设备
                            code: drawer.vaccineOne,   //疫苗编号
                            name: this.vaccineTwoName  //疫苗名称
                        }
                    });
                    this.queryDrawerByCondition();
                }
                //this.$forceUpdate();
            },
            selectOne(index, event){
                this.cabineData[index].ifAdd = false;
                if(event == undefined){ return false;}//删除时，阻止异常报错
                if(this.cabineData[index].vaccineOne !== "" && this.cabineData[index].vaccineOne !== undefined){
                    this.vaccineOneName = event.label;//新选择的疫苗名称
                    this.cabineData[index].ifAdd = true;
                }
            },
            selectTwo(index, event){
                this.cabineData[index].ifAddTwo = false;
                if(event==undefined){ return false;}//删除时，阻止异常报错
                if(this.cabineData[index].vaccineTwo !== "" && this.cabineData[index].vaccineTwo !== undefined){
                    let temp = this.cabineData[index].vaccine;
                    if(temp[0].code == this.cabineData[index].vaccineTwo){
                        this.$Message.error({
                            content: '同一个抽屉不允许设置相同的疫苗',
                            duration: 10,
                            closable: true
                        });
                        return false;
                    }
                    this.vaccineTwoName = event.label;//新选择的疫苗名称
                    this.cabineData[index].ifAddTwo = true;
                }
            },
            async delVaccineOne(index){
                let drawer = this.cabineData[index];
                if(drawer.surplus>0){
                    this.$Message.info({
                        top: 300,
                        content: '该疫苗数量大于0，不允许删除',
                        duration: 10,
                        closable: true
                    });
                    return false;
                }
                //删除疫苗信息从DB
                let res = await this.$api.post("/drawer/modifyDrawerByIdDec", {
                        id: drawer._id,
                        vaccineId:  drawer.vaccine[0]._id//疫苗id
                    }
                );
                this.queryDrawerByCondition();
            },
            async delVaccineTwo(index){
                let drawer = this.cabineData[index];
                if(drawer.surplus>0){
                    this.$Message.info({
                        top: 300,
                        content: '该疫苗数量大于0，不允许删除',
                        duration: 10,
                        closable: true
                    });
                    return false;
                }
                //删除疫苗信息从DB
                let res = await this.$api.post("/drawer/modifyDrawerByIdDec", {
                        id: drawer._id,
                        vaccineId:  drawer.vaccine[1]._id//疫苗id
                    }
                );
                this.queryDrawerByCondition();
                /*this.cabineData[index].ifVaccineTwo = false;
                this.cabineData[index].ifTwoChoose = true;
                this.cabineData[index].vaccineTwo = "";
                this.cabineData[index].ifAddTwo = false;
                this.$forceUpdate()*/
            },
            async queryVaccineKinds(){
                let res = await this.$api.get("/zcy/queryVaccineKinds");
                this.kindList = res.data;
            },
            async queryDrawerByCondition(){
                this.cabineData = [];
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    device: this.device._id
                });
                console.log(res)
                this.cabineData=res.data
                // this.cabineData = res.data;
                // //初始化区域信息
                // this.cabineData.forEach(item =>{
                //     let vaccine = item.vaccine;
                //     //vaccine有值时，控制显示内容
                //     if(vaccine.length>0){
                //         //抽屉只有一中疫苗
                //         if(vaccine.length ==1){
                //             item.vaccineOne = vaccine[0].code;
                //             item.surplus = vaccine[0].surplus;
                //             item.vaccineTwo = "";
                //             item.ifAdd = false;        //第一个加号不可点击
                //             item.ifAddTwo = false;      //第二个加号是可点击
                //             item.ifOneChoose = false;  //第一个select不显示
                //             item.ifTwoChoose = true;  //第二个select显示
                //             item.ifVaccineOne = true; //第一个已选择疫苗显示
                //             item.ifVaccineTwo = false; //第二个已选择疫苗不显示
                //         }
                //         //抽屉只有2中疫苗
                //         if(vaccine.length ==2){
                //             for(let i=0;i<vaccine.length;i++){
                //                 if(i=0){
                //                     item.vaccineOne = vaccine[i].code;
                //                     item.surplus = vaccine[i].surplus;
                //                 }
                //                 if(i=1){
                //                     item.vaccineTwo = vaccine[i].code;
                //                     item.surplus = vaccine[i].surplus;
                //                 }
                //                 item.ifAdd = false;        //第一个加号不可点击
                //                 item.ifAddTwo = false;     //第二个加号不可点击
                //                 item.ifOneChoose = false;  //第一个select不显示
                //                 item.ifTwoChoose = false;  //第二个select不显示
                //                 item.ifVaccineOne = true;  //第一个已选择疫苗显示
                //                 item.ifVaccineTwo = true;  //第二个已选择疫苗显示
                //             }
                //         }
                //     }else{
                //         //原始初始化
                //         item.vaccineOne = "";
                //         item.vaccineTwo = "";
                //         item.ifAdd = false;        //第一个加号是否可点击
                //         item.ifAddTwo = false;     //第二个加号是否可点击
                //         item.ifOneChoose = true;   //第一个select是否显示
                //         item.ifTwoChoose = false;  //第二个select是否显示
                //         item.ifVaccineOne = false; //第一个已选择疫苗是否显示
                //         item.ifVaccineTwo = false; //第二个已选择疫苗是否显示
                //     }
                // });
            }
        },
        mounted() {
            this.queryVaccineKinds();
            this.queryDrawerByCondition();
        },
    };
</script>

<style lang="less" scoped>
    @import "~@/style/region.less";
</style>