<!--库存详细-->
<template>
    <div class="inoculate card">
        <Row class="inoculate-title">
            <Col span="24">详情页面</Col>
        </Row>
        <Row class="inoculate-table-row">
            <Col span="1">#</Col>
            <Col span="6">疫苗名称</Col>
            <Col span="5">位置信息</Col>
            <Col span="4">入库数量</Col>
            <Col span="4">接种数量</Col>
            <Col span="4">剩余数量</Col>
        </Row>
        <Row v-for="(item, index) in datas" class="inoculate-table-row row-bg">
            <Col span="1">{{index+1}}</Col>
            <Col span="6">{{item.name}}</Col>
            <Col span="5">({{position}})</Col>
            <Col span="4">{{item.total}}</Col>
            <Col span="4">{{item.total-item.surplus}}</Col>
            <Col span="4">{{item.surplus}}</Col>
        </Row>
        <div class="goback">返回上一页</div>
    </div>
</template>

<script>
    import moment from 'moment';

    export default {

        data() {
            return {
                progress:0,
                info: '',
                vaccine: '',
                datas:[],
                position:''

            };
        },
        methods: {
            async getDetails(val) {
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {id: val});console.log(res.data[0].y+'ttt-----------%j',res.data[0]);
                this.position = res.data[0].x+','+res.data[0].y;
                this.datas = res.data[0].vaccine;
            }
        },
        mounted() {
            //接收抽屉编号
            let id = this.$route.query.drawerId;
            this.getDetails(id);
        }
    };
</script>

<style lang="less">
    @import "~@/style/stockDetail.less";
</style>