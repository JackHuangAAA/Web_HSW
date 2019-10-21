<!--库存详细-->
<template>
<div>
    <Row class="inoculate-head">
        <div class="goHome" @click="goHome()">
            <img src="/static/img/backHome.png" class="backHomeImg"/>
            <div>回到主页</div>
        </div>
    </Row>
    <div class="inoculate card">
        <!-- <Row class="inoculate-title">
            <Col span="24">详情页面</Col>
        </Row> -->
        <Row class="inoculate-table-row">
            <Col span="1">#</Col>
            <Col span="4">疫苗名称</Col>
            <Col span="4">批次号</Col>
            <Col span="6">有效期</Col>
            <Col span="3">入库数量</Col>
            <Col span="3">接种数量</Col>
            <Col span="3">剩余数量</Col>
        </Row>
        <Row v-for="(item, index) in datas" class="inoculate-table-row row-bg">
            <Col span="1">{{index+1}}</Col>
            <Col span="4">{{item.name}}</Col>
            <Col span="4">{{item.batchNo||'--'}}</Col>
            <Col span="6">{{item.updateDate}}</Col>
            <Col span="3">{{item.total}}</Col>
            <Col span="3">{{item.total-item.surplus}}</Col>
            <Col span="3">{{item.surplus}}</Col>
        </Row>
    </div>
</div>
</template>

<script>
    import moment from 'moment';

    export default {

        data() {
            return {
                datas:[]
            };
        },
        methods: {
            async getDetails(val) {
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {id: val});
                let arr=res.data[0].vaccine
                for(let i=0;i<arr.length;i++){
                    arr[i].updateDate=moment(arr[i].updateDate).format('YYYY-MM-DD HH:mm:ss')
                }
                this.datas = arr;
            },
            goHome(){
                this.$router.push('/main')
            },
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