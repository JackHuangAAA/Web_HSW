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
                    <Scroll :on-reach-bottom="handleReachBottom" :loading-text="loadText">
                        <div class="vaccineContentContent" v-for="(item,index) in vaccineData">
                            <div class="vaccineName">
                                {{item.name}}
                            </div>
                            <div class="vaccineAllowance">
                                {{item.surplus}}
                            </div>
                            <div class="allowanceStatus" :class="{dangerStatus:item.surplus==0}">
                                {{item.surplus==0?'库存为零':'缺少库存'}}
                            </div>
                        </div>
                    </Scroll>
                </div>
            </div>
        </div>
        <div class="button" v-if="!mainScreen">
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
                vaccineData:[],
                lackNumber:0,
                state:false,
                page:1,
                size:15,
                loadText:'正在加载……'
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
            },
            mainScreen:{
                type:Boolean,
                default:false
            }
        },
        components:{},
        methods: {
            ...mapActions({
                saveUser: 'saveUser',
                saveDevice: 'saveDevice'
            }),
            //查询温度报警
            async queryAlarmByByCondition(){
                let res = await this.$api.get("/alarm/queryAlarmNum",{
                    device: this.device._id,
                    type:1,
                    ifToday:'today'
                    });
                this.alarmNumber = res.data.total;
            },
            //冷藏柜已有疫苗种类
            async queryVaccineNum(){
                let res = await this.$api.get("/vaccine/queryVaccineStorageNum",{
                    device: this.device._id,
                    queryNum:true
                });
                console.log("result:"+JSON.stringify(res))
                this.vaccineNumber = res.data.total;
            },
            //查询预警疫苗信息（数量小于等于10）
            async queryVaccineStorageNum(){
                let res = await this.$api.get("/vaccine/queryVaccineStorageNum", {
                    device: this.device._id,
                    surplusltTen: true,
                    page:this.page,
                    size:this.size
                });
                let arr=res.data.rs;
                // res.data.rs.forEach(item=>{
                //     if(item.surplus<10){
                //         arr.push(item);
                //     }
                // });

                if(arr.length>0){
                    this.loadText='正在加载……';
                    this.vaccineData =this.vaccineData.concat(arr);
                    this.page++;
                }else{
                    this.loadText='已显示所有数据';
                }
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
            },
            handleReachBottom () {
                 return new Promise(resolve => {
                    setTimeout(() => {
                        this.queryVaccineStorageNum();
                        resolve();
                    }, 2000);
                });
            }
        },
        destroyed(){
            this.state=false
        },
        mounted() {
            this.state=true
            //查询首页数据
            //获取设备信息
            this.$device.getDeviceCode().then(res => {
                this.$api.get('/device/queryDeviceByCondition',{code:res}).then((res)=>{
                    console.log('vuex save device info:'+JSON.stringify(res.data[0]));
                    this.saveDevice(res.data[0]);
                    this.queryVaccineNum();
                    this.queryAlarmByByCondition();
                    this.queryVaccineStorageNum();
                    this.receiveSocketData();
                });
            });
        }
    }
</script>

<style lang="less" scoped>
    @import "~@/style/main.less";
</style>
