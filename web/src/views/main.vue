<template>
    <div class="main">
        <Row :gutter="20" class="main-top">
            <Col span="8">
                <Row class="main-box-title">冷藏柜统计</Row>
                <Row class="main-box" :gutter="18">
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/count.png" alt="">
                            <div>70</div>
                            <div>冷藏柜</div>
                        </div>                            
                    </Col>
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/normal.png" alt="">
                            <div>50</div>
                            <div>正常</div>
                        </div>                            
                    </Col>
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/abnormal.png" alt="">
                            <div>20</div>
                            <div>异常</div>
                        </div>                            
                    </Col>
                </Row>
            </Col>            
            <Col span="8">
                <Row class="main-box-title">接种柜统计</Row>
                <Row class="main-box" :gutter="18">
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/count.png" alt="">
                            <div>70</div>
                            <div>接种柜</div>
                        </div>                            
                    </Col>
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/normal.png" alt="">
                            <div>58</div>
                            <div>正常</div>
                        </div>                            
                    </Col>
                    <Col span="8">
                        <div class="box-bs">
                            <img src="/static/img/abnormal.png" alt="">
                            <div>2</div>
                            <div>异常</div>
                        </div>
                    </Col>
                </Row>
            </Col>
            <Col span="8">
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
        </Row>
        <!-- table -->
        <div class="main-table">
            <Row>
                <Col span="19" class="main-table-title">各单位设备数量统计</Col>
                <Col span="5" class="main-table-search">
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
            <Row v-for="item of 9" :key="item" class="main-table-body">
                <Col span="2" class="id-center">{{item}}</Col>
                <Col span="4">1</Col>
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
                value1:''
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                currentMenu: 'currentMenu',
            })
        },
        components:{},
        methods: {
            ...mapActions({
                saveUser: 'saveUser',
                setCurrentMenu: 'setCurrentMenu'
            })

        },
        mounted() {

        }
    }
</script>

