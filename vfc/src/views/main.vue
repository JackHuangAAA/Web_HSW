<template>
    <div class="container">
        <div class="cabineStatus">
            <div class="abnormalTemperature">
                <p class="cardOne"><span style="font-size:2.25rem;margin-right:5px;">{{alarmNumber||0}}</span>/次</p>
                <p class="cardTwo">温度异常</p>
                <img class="cardImg" src="/static/img/warning.png">
            </div>
            <div class="peopleCount">
                <p class="cardOne"><span style="font-size:2.25rem;margin-right:5px;">{{vaccineNumber||0}}</span>/种</p>
                <p class="cardTwo">疫苗种类</p>
                <img class="cardImg" src="/static/img/vaccinekinds.png">
            </div>
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
        </div>

        <div class="vaccineStatus">
            <div class="vaccineStatusTitle">
                <span class="vaccineStatusTitleTip">疫苗库存状态</span>
                <div class="redBlock"></div>
                <p class="bj">报警</p>
                <div class="yellowBlock"></div>
                <p class="yj">预警</p>
            </div>
            <div class="vaccineContent">
                <div v-for="(item,index) in vaccineData" class="vaccineStatusShow" 
                v-bind:class='{warning:item.vaccineOneCount == 0 || item.vaccineTwoCount == 0,
                tips:item.vaccineOneCount <10 || item.vaccineTwoCount < 10,
                noData:item.vaccineOneCount == ""&&item.vaccineOneCount !== 0}'>
                    <div class="vaccineLeft" v-if="item.vaccineOneName">
                        <p class="vaccineOneName">{{item.vaccineOneName}}</p>
                        <p class="vaccineOneCount">{{item.vaccineOneCount || 0}}支</p>
                    </div>
                    <div class="vaccineRight" v-if="item.vaccineTwoName">
                        <p class="vaccineTwoName">{{item.vaccineTwoName}}</p>
                        <p class="vaccineTwoCount">{{item.vaccineTwoCount || 0}}支</p>
                    </div>
                    <div class="vaccineLeft" v-if="item.vaccineThreeName">
                        <p class="vaccineOneName">{{item.vaccineThreeName}}</p>
                        <p class="vaccineOneCount">{{item.vaccineThreeCount || 0}}支</p>
                    </div>
                    <div class="vaccineRight" v-if="item.vaccineFourName">
                        <p class="vaccineTwoName">{{item.vaccineFourName}}</p>
                        <p class="vaccineTwoCount">{{item.vaccineFourCount || 0}}支</p>
                    </div>
                    <div class="vaccineLeft" v-if="item.vaccineFiveName">
                        <p class="vaccineOneName">{{item.vaccineFiveName}}</p>
                        <p class="vaccineOneCount">{{item.vaccineFiveCount || 0}}支</p>
                    </div>
                    <div class="vaccineRight" v-if="item.vaccineSixName">
                        <p class="vaccineTwoName">{{item.vaccineSixName}}</p>
                        <p class="vaccineTwoCount">{{item.vaccineSixCount || 0}}支</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="button">
                <div class="buttonLeft">
                    <div class="ymrk" @click="vaccineIn()">疫苗入库</div>
                </div>
                <div class="buttonRight">
                    <div class="ymck" @click="vaccineOut()">疫苗出库</div>
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
                vaccineNumber:0,
                temperature: 0,
                temperatureDes:'正常',
                vaccineData:[]
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
            //查询温度报警
            async queryAlarmByByCondition(){
                let res = await this.$api.get("/alarm/queryAlarmByByCondition",{
                    device: this.device._id,
                    type:1,
                    ifToday:'today'
                    });
                this.alarmNumber = res.data.length;
            },
            //冷藏柜已有疫苗种类
            async queryVaccineNum(){
                let res = await this.$api.get("/vaccine/queryVaccineNum",{
                    device: this.device._id
                });
                this.vaccineNumber = res.data.length;
            },
            //查询抽屉疫苗信息
            async queryDrawerByCondition(){
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    device: this.device._id
                });
                let array = res.data;
                for (let i = 0; i < 12; i++) {
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
                            if (k == 4) {
                                temp.vaccineFiveName = vaccine[k].name;
                                temp.vaccineFiveCount = vaccine[k].surplus;
                            }
                            if (k == 5) {
                                temp.vaccineSixName = vaccine[k].name;
                                temp.vaccineSixCount = vaccine[k].surplus;
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
                        temp.vaccineFiveName = '';
                        temp.vaccineFiveCount = '';
                        temp.vaccineSixName = '';
                        temp.vaccineSixCount = '';
                    }
                    this.vaccineData.push(temp);
                }
            },
            vaccineIn(){
                this.$router.push('/inout/inStock');
            },
            vaccineOut(){
                this.$router.push('/inout/outStock');
            }
        },
        mounted() {console.log('this.device-----%',this.device)
            //查询首页数据
            this.queryDrawerByCondition();
            this.queryAlarmByByCondition();
            this.queryVaccineNum();
        }
    }
</script>

<style lang="less" scoped>
    @import "~@/style/main.less";
</style>
