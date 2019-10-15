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
                <Col span="1">
                    <div>#</div>
                    <div>{{index+1}}</div>
                </Col>
                <Col span="3">
                <div>报警类型</div>
                <div>{{item.type}}</div>
                </Col>
                <Col span="4">
                <div>报警时间</div>
                <div>{{getAlarmDate(item.createDate)}}</div>
                </Col>
                <Col span="8">
                <div>报警原因</div>
                <div>{{item.reason}}</div>
                </Col>
                <Col span="8">
                <div>解决情况</div>
                <div>{{item.solution}}</div>
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
            getAlarms() {
                this.$api.get("alarm/queryAlarmByByCondition", {
                        device: this.device._id,
                        ifToday: "today"
                    }).then(res => {
                        this.datas = res.data;console.log('------>%j',res.data);
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