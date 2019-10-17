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
                    <div v-for="(item) of 4" :key="item" class="temperature-alarm">
                        <div class="temperature-alarm-icon">
                            <img src="/static/img/alarm_notice.png" alt="">
                            <div>杭州市第一人民医院</div>
                        </div>                        
                        <div>第一站点接种设备<span>20℃</span></div>
                    </div>
                </Row>
            </Col>
            </div>
        </Row>
        <!-- table -->
        <div class="main-table">
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
            <Row v-for="(item,index) of 5" :key="index" class="main-table-body">
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
                this.$api.get('/device/queryDeviceByAggregate',{flag:0,test:0}).then(res=>{
                    let data=res.data;
                    for(let i=0;i<data.length;i++){
                        if(data[i]._id.type==1 && data[i]._id.status==1){
                            this.i_normalCount=data[i].count
                        }
                        if(data[i]._id.type==1 && data[i]._id.status==2){
                            this.i_abnormalCount+=data[i].count
                        }
                        if(data[i]._id.type==1 && data[i]._id.status==0){
                            this.i_abnormalCount+=data[i].count
                        }
                        if(data[i]._id.type==2 && data[i]._id.status==1){
                            this.f_normalCount=data[i].count
                        }
                        if(data[i]._id.type==2 && data[i]._id.status==2){
                            this.f_abnormalCount+=data[i].count
                        }
                        if(data[i]._id.type==2 && data[i]._id.status==0){
                            this.f_abnormalCount+=data[i].count
                        }
                    }
                    console.log(this.i_abnormalCount +" "+this.i_normalCount+" " +this.f_abnormalCount+ " "+this.f_normalCount)
                })
                this.$api.get('/device/queryDeviceByAggregate',{flag:1,test:0}).then(res=>{
                    
                })
            },
            queryAlarmDailyInfo(){
                this.$api.get('/alarm/queryAlarmDailyInfo',{test:0}).then(res=>{
                    console.log(res)
                })
            },

        },
        mounted() {
            this.queryDeviceByAggregate()
            this.queryAlarmDailyInfo()
        }
    }
</script>

