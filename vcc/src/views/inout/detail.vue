<!--出入库明细-->
<template>
    <div>
        <div>疫苗{{action}}完成</div>
        <div>{{action}}人员：{{user.name}} &nbsp;&nbsp;&nbsp;&nbsp; {{action}}时间：{{now}}</div>
        <!--head-->
        <Row>
            <Col span="12">
                <Row>
                    <Col span="6">#</Col>
                    <Col span="9">疫苗名称</Col>
                    <Col span="9">{{action}}数量</Col>
                </Row>
            </Col>
            <Col span="12">
                <Row>
                    <Col span="6">#</Col>
                    <Col span="9">疫苗名称</Col>
                    <Col span="9">{{action}}数量</Col>
                </Row>
            </Col>
        </Row>
        <!--data-->
        <Row>
            <Col span="12">
            <Row v-for="(item, index) in leftDatas">
                <Col span="6">{{index+1}}</Col>
                <Col span="9">{{item.name}}</Col>
                <Col span="9">{{item.surplus}}</Col>
            </Row>
            </Col>
            <Col span="12">
            <Row v-for="(item, index) in rightDatas">
                <Col span="6">{{index+10}}</Col>
                <Col span="9">{{item.name}}</Col>
                <Col span="9">{{item.surplus}}</Col>
            </Row>
            </Col>
        </Row>
    </div>
</template>

<script>
    import moment from 'moment';
    import { mapGetters } from "vuex";

    export default {

        data() {
            return {
                action:'',
                now: moment().format('YYYY-MM-DD HH:mm:ss'),
                leftDatas:[],
                rightDatas:[]
            };
        },
        computed: {
            ...mapGetters({
                user: "user",
                device: "device"
            })
        },
        methods: {
            async getDetails() {
                let res = await this.$api.get("/vaccine/queryVaccineStorageNum", {device: this.device._id});
                this.datas = res.data.rs;console.log('7788--->%j',res.data);
                let aa = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15];
                if(this.datas.length>10){
                    this.leftDatas = _.slice(this.datas,0,10);
                    this.rightDatas = _.slice(this.datas,10);
                }else{
                    this.leftDatas = this.datas;
                }
                console.log(this.leftDatas+'----yyy-----'+this.rightDatas)
            },
            setAction(val){
                this.action = (val=='in')?'入库':'出库';
            }
        },
        mounted() {
            console.log(JSON.stringify(this.user)+'----7788--->%j',this.device);
            //接收抽屉编号
            let action = "in";//this.$route.query.action;
            this.setAction(action);
            this.getDetails();
        }
    };
</script>

<style lang="less">
    @import "~@/style/stockDetail.less";
</style>