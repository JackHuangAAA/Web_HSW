<template>
    <div class="container">
        <div class="vaccineStatus">
            <div class="vaccineStatusTitle">
                <span class="vaccineStatusTitleTip">疫苗库存状态</span>
                <div class="redBlock"></div>
                <p class="bj">报警</p>
                <div class="yellowBlock"></div>
                <p class="yj">预警</p>
            </div>
            <div class="vaccineContent">
                <div v-for="(item,index) in vaccineData" class="vaccineStatusShow">
                    <div class="vaccineLeft" v-if="item.vaccineOneName">
                        <p class="vaccineOneName" :class="{warning:item.vaccineOneCount == 0,tips:item.vaccineOneCount <10}">{{item.vaccineOneName}}</p>
                        <p class="vaccineOneCount" :class="{warning:item.vaccineOneCount == 0,tips:item.vaccineOneCount <10}">{{item.vaccineOneCount || 0}}支</p>
                    </div>
                    <div class="vaccineRight" v-if="item.vaccineTwoName">
                        <p class="vaccineTwoName" :class="{warning:item.vaccineTwoCount == 0,tips:item.vaccineTwoCount <10}">{{item.vaccineTwoName}}</p>
                        <p class="vaccineTwoCount" :class="{warning:item.vaccineTwoCount == 0,tips:item.vaccineTwoCount <10}">{{item.vaccineTwoCount || 0}}支</p>
                    </div>
                    <div class="vaccineLeft" v-if="item.vaccineThreeName">
                        <p class="vaccineOneName" :class="{warning:item.vaccineThreeCount == 0,tips:item.vaccineThreeCount <10}">{{item.vaccineThreeName}}</p>
                        <p class="vaccineOneCount" :class="{warning:item.vaccineThreeCount == 0,tips:item.vaccineThreeCount <10}">{{item.vaccineThreeCount || 0}}支</p>
                    </div>
                    <div class="vaccineRight" v-if="item.vaccineFourName">
                        <p class="vaccineTwoName" :class="{warning:item.vaccineFourCount == 0,tips:item.vaccineFourCount <10}">{{item.vaccineFourName}}</p>
                        <p class="vaccineTwoCount" :class="{warning:item.vaccineFourCount == 0,tips:item.vaccineFourCount <10}">{{item.vaccineFourCount || 0}}支</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="cabineStatus">
            <div class="temperature">
                <img src="/static/img/temperature.png">
                <p class="temP">{{temperature}}</p>
                <p class="temStatus">{{temperatureDes}}</p>
                <!--<p class="roomTem">室温： 26℃</p>-->
                <p class="tem1">{{temperature-3}}</p>
                <p class="tem2">{{temperature-2}}</p>
                <p class="tem3">{{temperature-1}}</p>
                <p class="tem4">{{temperature}}</p>
                <p class="tem5">{{temperature+1}}</p>
                <p class="tem6">{{temperature+2}}</p>
                <p class="tem7">{{temperature+3}}</p>
            </div>
            <div class="abnormalTemperature">
                <p class="cardOne"><span style="font-size:2.25rem;margin-right:5px;">{{alarmNumber||0}}</span>/次</p>
                <p class="cardTwo">温度异常</p>
                <img class="cardImg" src="/static/img/warning.png">
            </div>
            <div class="peopleCount">
                <p class="cardOne"><span style="font-size:2.25rem;margin-right:5px;">{{customerNumber||0}}</span>/人</p>
                <p class="cardTwo">接种顾客</p>
                <img class="cardImg" src="/static/img/customer.png">
            </div>
            <div class="btnBox">
                <div class="addButton" @click="vaccineIn()">
                    疫苗入库
                </div>
                <div class="outButton" @click="vaccineOut()">
                    疫苗出库
                </div>
            </div>
            <div class="startButton" @click="startVaccine()">
                开始接种
            </div>
        </div>
    </div>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex'
    import enumTool from  '@/enum'
    import moment from 'moment'
    import io from  'socket.io-client'

    export default {
        data() {
            return {
                alarmNumber: 0,
                customerNumber:0,
                // temperature: 0,
                // temperatureDes:'正常',
                vaccineData:[],
                state:false
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device'
            })
        },
        props:{
            temperature:{
                type:Number,
                default:5
            },
            temperatureDes:{
                type:String,
                default:'正常'
            }
        },
        components:{},
        methods: {
            //查询温度报警
            async queryAlarmByByCondition(){
                let res = await this.$api.get("/alarm/queryAlarmByCondition",{
                    device: this.device._id,
                    type:1,
                    ifToday:'today'
                    });
                this.alarmNumber = res.data.length;
            },
            //当天服务人数
            async queryVaccinationDailyInfo(){
                let res = await this.$api.get("/vaccination/queryVaccinationDailyInfo",{
                    device: this.device._id,
                    ifToday:'today'
                });
                this.customerNumber = res.data.length;
            },
            //查询抽屉疫苗信息
            async queryDrawerByCondition(){
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    device: this.device._id
                });
                let array = res.data;
                for (let i = 0; i < 10; i++) {
                    let num = array[i].vaccine.length, vaccine = array[i].vaccine, temp = {};
                    if (num > 0) {
                        for (let k = 0; k < num; k++) {
                            if (k == 0) {
                                temp.vaccineOneName = vaccine[k].name;
                                temp.vaccineOneCount = vaccine[k].surplus;
                            }
                            if (k == 1) {
                                temp.vaccineTwoName = vaccine[k].name;
                                temp.vaccineTwoCount = vaccine[k].surplus;
                            }
                            if (k == 2) {
                                temp.vaccineThreeName = vaccine[k].name;
                                temp.vaccineThreeCount = vaccine[k].surplus;
                            }
                            if (k == 3) {
                                temp.vaccineFourName = vaccine[k].name;
                                temp.vaccineFourCount = vaccine[k].surplus;
                            }
                        }
                    } else {
                        temp.vaccineOneName = '';
                        temp.vaccineOneCount = '';
                        temp.vaccineTwoName = '';
                        temp.vaccineTwoCount = '';
                        temp.vaccineThreeName = '';
                        temp.vaccineThreeCount = '';
                        temp.vaccineFourName = '';
                        temp.vaccineFourCount = '';
                    }
                    this.vaccineData.push(temp);
                }
            },
            receiveSocketData(){
                this.$device.subscribe('SOCKET_DATA', (data) => {
                    if(this.state==true){
                        console.log('SOCKET_DATA====> result:'+ JSON.stringify(data.data));
                        let res=JSON.parse(data.data)
                        if(res.type=="refresh"){
                            this.queryDrawerByCondition();
                            this.queryAlarmByByCondition();
                            this.queryVaccinationDailyInfo();
                        }
                    }
                });
            },
            vaccineIn(){
                this.$router.push('/inout/inStock');
            },
            vaccineOut(){
                this.$router.push('/inout/outStock');
            },
            startVaccine(){
                this.$router.push('/vaccination/vaccination');
            }
        },
        destroyed(){
            
        },
        mounted() {
            //查询首页数据
            // __app.on("NOW_TEMPERATURE",(data)=>{
            //     console.log("NOW_TEMPERATURE: " + JSON.stringify(data));
            // });
            this.state=true
            if(this.device){
                this.queryDrawerByCondition();
                this.queryAlarmByByCondition();
                this.queryVaccinationDailyInfo();
                this.receiveSocketData();
            }
        }
    }
</script>

<style lang="less" scoped>
    @import "~@/style/main.less";
</style>
