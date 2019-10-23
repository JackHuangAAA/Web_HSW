<!--预警页面-->
<template>
    <div class="alarm card">
        <div class="alarm-header">
            <div class="alarm-header-title">
                <p>报警记录</p>
            </div>
            <div class="alarm-header-info">
                <p>排序依照最近时间排序</p>
            </div>
        </div>
        <div class="alarm-main">
            <Row v-for="(item, index) in datas" class="alarm-card">
                <Col span="2" class="alarm-card-id">
                    <div>{{index+1}}</div>
                </Col>
                <Row span="22" style="width:100%">
                    <Col span="11" class="alarm-card-pd">
                        <div class="alarm-card-title">报警类型:</div>
                        <div class="alarm-card-info">{{item.type==1?'温度异常':'库存不足'}}</div>
                    </Col>
                    <Col span="13" class="alarm-card-pd">
                        <div class="alarm-card-title">报警原因:</div>
                        <div class="alarm-card-info">{{item.reason}}</div>
                    </Col>
                    <Col span="11" class="alarm-card-pd row2">
                        <div class="alarm-card-title">报警时间:</div>
                        <div class="alarm-card-info">{{getAlarmDate(item.createDate)}}</div>
                    </Col>
                    
                    <Col span="13" class="alarm-card-pd row2">
                        <div class="alarm-card-title">解决情况:</div>
                        <div class="alarm-card-info">{{item.solution}}</div>
                    </Col>
                </Row>                
            </Row>
            <div v-if="nullData" style="width:100%;text-align:center;font-size: 20px;">暂无数据</div>
        </div>
    </div>
</template>

<script>
    import { mapGetters } from "vuex";
    import moment from 'moment';

    export default {
        data() {
            return {
                datas: []
            };
        },
        computed: {
            ...mapGetters({
                device: "device"
            }),
            nullData:function () {
                return this.datas.length==0?true:false;
            }
        },
        methods: {
            getAlarms() {
                this.$api.get("alarm/queryAlarmByByCondition", {
                        device: this.device._id,
                        ifToday: "today"
                    }).then(res => {
                        this.datas = res.data;
                    });
            },
            getAlarmDate(val){
                return moment(val).format('YYYY-MM-DD HH:mm:ss');
            }
        },
        mounted() {
            this.getAlarms();
        }
    };
</script>

<style lang="less">
    @import "~@/style/alarm.less";
</style>