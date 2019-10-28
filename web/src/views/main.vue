<template>
    <div class="main">
        <Row  class="main-top">
            <div class="box-shadow">
            <Col span="24">
                <Row class="main-box-title">冷藏柜统计</Row>
                <Row class="main-box" :gutter="18">
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/count.png" alt="">
                            <div>{{f_abnormalCount+f_normalCount}}</div>
                            <div>冷藏柜</div>
                        </div>                            
                    </Col>
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/normal.png" alt="">
                            <div>{{f_normalCount}}</div>
                            <div>正常</div>
                        </div>                            
                    </Col>
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/abnormal.png" alt="">
                            <div>{{f_abnormalCount}}</div>
                            <div>异常</div>
                        </div>                            
                    </Col>
                </Row>
            </Col>
            </div>
            <div class="box-shadow">        
            <Col span="24">
                <Row class="main-box-title">接种柜统计</Row>
                <Row class="main-box" :gutter="18">
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/count.png" alt="">
                            <div>{{i_abnormalCount+i_normalCount}}</div>
                            <div>接种柜</div>
                        </div>                            
                    </Col>
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/normal.png" alt="">
                            <div>{{i_normalCount}}</div>
                            <div>正常</div>
                        </div>                            
                    </Col>
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/abnormal.png" alt="">
                            <div>{{i_abnormalCount}}</div>
                            <div>异常</div>
                        </div>
                    </Col>
                </Row>
            </Col>
            </div>
            <div class="box-shadow">
            <Col span="24" >
                <Row class="main-box-title">温度报警</Row>
                <Row>
                    <div v-for="(item,index) of alarmList" :key="index" class="temperature-alarm">
                        <div class="temperature-alarm-icon">
                            <img src="/static/img/alarm_notice.png" alt="">
                            <div>{{item.unitName}}</div>
                        </div>                        
                        <div>{{item.reason}}</div>
                        <!-- 第一站点接种设备<span>20℃</span> -->
                    </div>
                </Row>
            </Col>
            </div>
        </Row>
        <!-- table -->
        <div class="main-table main-table-height">
            <Row>
                <Col span="18" class="main-table-title">各单位设备数量统计</Col>
                <Col span="6" class="main-table-search">
                    <div class="main-table-search-lab">接种单位:</div>                    
                    <input v-model="value1" placeholder="" />
                </Col>
            </Row>
            
            <Row class="main-table-head">
                <Col span="2" class="id-center">序号</Col>
                <Col span="4">接种单位</Col>
                <Col span="3">冷藏柜数量</Col>
                <Col span="3">正常冷藏柜</Col>
                <Col span="3">异常冷藏柜</Col>
                <Col span="3">接种柜数量</Col>
                <Col span="3">正常接种柜</Col>
                <Col span="3">异常接种柜</Col>
            </Row>
            <div class="home-table-body">
                <Row v-for="(item,index) of 10" :key="index" class="main-table-body">
                    <Col span="2" class="id-center">1</Col>
                    <Col span="4">{{index}}</Col>
                    <Col span="3">1</Col>
                    <Col span="3">1</Col>
                    <Col span="3">1</Col>
                    <Col span="3">1</Col>
                    <Col span="3">1</Col>
                    <Col span="3">1</Col>
                </Row>
            </div>
            
        </div>
    </div>
</template>

<style lang="less" scoped>
@import '../style/color.less';
@import '../style/main.less';
@import '../style/table.less';
</style>

<script>
    import {mapGetters, mapActions} from 'vuex'
    import enumTool from  '@/enum'
    import moment from 'moment'
    import io from  'socket.io-client'

    export default {
        data() {
            return {
                value1:'',
                f_abnormalCount:0,
                f_normalCount:0,
                i_abnormalCount:0,
                i_normalCount:0,
                lists:[],
                alarmList:[],
                
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                // currentMenu: 'currentMenu',
            })
        },
        components:{},
        methods: {
            ...mapActions({
                saveUser: 'saveUser',
                // setCurrentMenu: 'setCurrentMenu'
            }),
            queryDeviceByAggregate(){
                this.$api.get('/device/queryDeviceByAggregate',{flag:0,type:1,test:0}).then(res=>{
                    let data=res.data;
                    for(let i=0;i<data.length;i++){
                        if(data[i]._id.status==1){
                            this.i_normalCount=data[i].count
                        }
                        if(data[i]._id.status==2){
                            this.i_abnormalCount+=data[i].count
                        }
                        if(data[i]._id.status==0){
                            this.i_abnormalCount+=data[i].count
                        }
                    }
                })
                this.$api.get('/device/queryDeviceByAggregate',{flag:0,type:2,test:0}).then(res=>{
                    let data=res.data;
                    for(let i=0;i<data.length;i++){
                        if(data[i]._id.status==1){
                            this.f_normalCount=data[i].count
                        }
                        if(data[i]._id.status==2){
                            this.f_abnormalCount+=data[i].count
                        }
                        if(data[i]._id.status==0){
                            this.f_abnormalCount+=data[i].count
                        }
                    }
                })
                // this.$api.get('/device/queryDeviceByAggregate',{flag:1,test:0}).then(res=>{
                //     //查询各单位设备数量
                // })
            },
            queryAlarmDailyInfo(){
                this.$api.get('/alarm/queryAlarmDailyInfo',{test:0}).then(res=>{
                    let data=res.data
                    data=data.slice(0,4)
                    this.alarmList=data
                })
            },

        },
        mounted() {
            this.queryDeviceByAggregate()
            this.queryAlarmDailyInfo()
        }
    }
</script>

