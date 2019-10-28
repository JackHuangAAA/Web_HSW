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
                <span class="vaccineStatusTitleTip"><img src="/static/img/tip.png" style="margin-right:1rem;">缺少库存疫苗<span style="color:#FF5500;margin-left:1.5rem;">{{lackNumber}}种</span></span>
            </div>
            <div class="vaccineContent">
                <div class="vaccineContentTitle">
                    <div class="headName">
                        疫苗名称
                    </div>
                    <div class="headAllowance">
                        剩余库存量
                    </div>
                    <div class="headStatus">
                        库存状态
                    </div>
                </div>
                <div class="vaccineContentContent-table">
                    <div class="vaccineContentContent" v-for="(item,index) in vaccineData">
                        <div class="vaccineName">
                            {{item.name}}
                        </div>
                        <div class="vaccineAllowance">
                            {{item.surplus}}
                        </div>
                        <div class="allowanceStatus" :class="{dangerStatus:item.surplus==0}">
                            缺少库存
                        </div>
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
                // temperature: 0,
                // temperatureDes:'正常',
                vaccineData:[],
                lackNumber:0
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device',
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
            //查询预警疫苗信息（数量小于等于10）
            async queryVaccineStorageNum(){
                let res = await this.$api.get("/vaccine/queryVaccineStorageNum", {
                    device: this.device._id,
                    surplusltTen: true
                });
                this.vaccineData = res.data.rs;
                this.lackNumber = res.data.total;
            },
            receiveSocketData(){
                this.$device.subscribe('SOCKET_DATA', (data) => {
                    if(this.state==true){
                        console.log('SOCKET_DATA====> result:'+ JSON.stringify(data.data));
                        let res=JSON.parse(data.data)
                        if(res.type=="refresh"){
                            this.queryVaccineNum();
                            this.queryAlarmByByCondition();
                            this.queryVaccineStorageNum();
                        }
                    }
                });
            },
            vaccineIn(){
                this.$router.push('/inout/inStock');
            },
            vaccineOut(){
                this.$router.push('/inout/outStock');
            }
        },
        mounted() {
            //查询首页数据
            if(this.device){
                this.queryVaccineNum();
                this.queryAlarmByByCondition();
                this.queryVaccineStorageNum();
                this.receiveSocketData();
            }
        }
    }
</script>

<style lang="less" scoped>
    @import "~@/style/main.less";
</style>
