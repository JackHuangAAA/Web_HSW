<template>
    <div>
    <Row class="inoculate-head">
        <div class="history">
            历史记录
        </div>
        <div class="type">
            类型:
            <Select v-model="actionType" style="width:70%" clearable @on-change="contextChange">
                <Option v-for="item in types" :value="item.key" :key="item.key">{{ item.label }}</Option>
            </Select>
        </div>
        <div class="date">
            日期:<DatePicker type="date" v-model="date" style="width: 200px" clearable @on-change="contextChange"></DatePicker>
        </div>
    </Row>
    <div class="inoculate card">

        <Row class="inoculate-table-row">
            <Col span="4">疫苗名称</Col>
            <Col span="7">生产厂家</Col>
            <Col span="4">唯一号</Col>
            <Col span="6">有效期</Col>
            <Col span="3">{{action}}数量</Col>
        </Row>
        <div class="inoculate-table">
            <Row v-for="(item, index) in inoutDatas" class="inoculate-table-row row-bg">
                <Col span="4">{{item.name||'--'}}</Col>
                <Col span="7" class="producer">{{item.product||'--'}}</Col>
                <Col span="4">{{item.code||'--'}}</Col>
                <Col span="6">{{item.expiry||'--'}}</Col>
                <Col span="3" :class="{alarmStatus:item.surplus<=10,dangerStatus:item.surplus==0}">{{item.surplus||'--'}}</Col>
            </Row>
        </div>
    </div>
</div>
</template>
<script>
    import {mapGetters} from 'vuex';
    import moment from 'moment';

    export default {
        data() {
            return {
                types:[
                    {label:'入库',key:1},
                    {label:'出库',key:2}
                    ],
                actionType: 2,
                action:'出库',
                date: moment().format('YYYY-MM-DD'),
                inoutDatas:[]
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
            async queryInoutByCondition(){
                let res = await this.$api.get("/inout/queryInoutByCondition", {
                    device: this.device._id,
                    type: this.actionType,
                    date: this.date
                });
                this.inoutDatas = res.data;
            },
            dateFormat(val){
                return moment(val).format('YYYY-MM-DD HH:mm:ss');
            },
            contextChange(){
                this.actionType == 1?'入库':'出库';
                this.queryInoutByCondition();
            }
        },
        mounted() {
           this.queryInoutByCondition();
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/flowing.less";
</style>
