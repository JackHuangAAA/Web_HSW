<template>
    <div>
    <Row class="inoculate-head">
        <div class="history">
            历史记录
        </div>
        <div class="type">
            类型:
            <Select v-model="model1" style="width:70%">
                <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
        </div>
        <div class="date">
            日期:<DatePicker type="date" placeholder="Select date" style="width: 200px" v-model="date"></DatePicker>
        </div>
    </Row>
    <div class="inoculate card">
        <!-- <Row class="inoculate-title">
            <Col span="24">详情页面</Col>
        </Row> -->
        <Row class="inoculate-table-row">
            <Col span="4">疫苗名称</Col>
            <Col span="7">生产厂家</Col>
            <Col span="4">唯一号</Col>
            <Col span="6">截止有效期</Col>
            <Col span="3">出库数量</Col>
        </Row>
        <Row v-for="(item, index) in datas" class="inoculate-table-row row-bg">
            <Col span="4">{{item.name||'--'}}</Col>
            <Col span="7">{{item.produce||'--'}}</Col>
            <Col span="4">{{item.code||'--'}}</Col>
            <Col span="6">{{item.date||'--'}}</Col>
            <Col span="3">{{item.count||'--'}}</Col>
        </Row>
    </div>
</div>
</template>
<script>
    import {mapGetters} from 'vuex';
    import moment from 'moment'
    export default {
        data() {
            return {
                cityList:[{label:'1',value:'1'}],
                model1: '',
                date:'',
                datas:[{name: '乙型肝炎疫苗',produce: '北京科生生物制品有限公司',code: 'yHUG-7U940',date: '2019-09-18 12:30:28',count: '3'},{},{}]
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
            
        },
        mounted() {
           
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/flowing.less";
</style>
