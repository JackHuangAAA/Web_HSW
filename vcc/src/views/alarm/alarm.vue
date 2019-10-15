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
                <Col span="1" class="alarm-card-id">
                    <div>{{index+1}}</div>
                </Col>
                <Col span="3" class="alarm-card-pd">
                    <div class="alarm-card-title">报警类型:</div>
                    <div class="alarm-card-info">{{item.type}}</div>
                </Col>
                <Col span="5" class="alarm-card-pd">
                    <div class="alarm-card-title">报警时间:</div>
                    <div class="alarm-card-info">{{getAlarmDate(item.createDate)}}</div>
                </Col>
                <Col span="7" class="alarm-card-pd">
                    <div class="alarm-card-title">报警原因:</div>
                    <div class="alarm-card-info">{{item.reason}}</div>
                </Col>
                <Col span="8" class="alarm-card-pd">
                    <div class="alarm-card-title">解决情况:</div>
                    <div class="alarm-card-info">{{item.solution}}</div>
                </Col>
            </Row>
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
            })
        },
        methods: {
            getAlarms() {console.log('------>%j',this.device);
                this.$api.get("alarm/queryAlarmByByCondition", {
                        device: "5da172d36baebc8f36dc7e6e",//this.device._id,
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