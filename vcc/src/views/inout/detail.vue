<!--出入库明细-->
<template>
    <div class="inoculate">
        <div class="stock-title"><img src="/static/img/succeed.png"/>疫苗{{action}}完成</div>
        <div class="stock-menber">
            <div>{{action}}人员：{{user.name}}</div>
            <div>{{action}}时间：{{now}}</div>
        </div>
        <div class="goHome" @click="goHome()"></div>
        <!--head-->
        <Row>
            <Col span="12" class="column-pd">
                <Row class=" row-center">
                    <Col span="6">#</Col>
                    <Col span="9">疫苗名称</Col>
                    <Col span="9">{{action}}数量</Col>
                </Row>
            </Col>
            <Col span="12" class="column-pd">
                <Row class=" row-center">
                    <Col span="6">#</Col>
                    <Col span="9">疫苗名称</Col>
                    <Col span="9">{{action}}数量</Col>
                </Row>
            </Col>
        </Row>
        <!--data-->
        <Row>
            <Col span="12" class="column-pd">
            <Row v-for="(item, index) in leftDatas" class="stock-row row-center">
                <Col span="6">{{index+1}}</Col>
                <Col span="9">{{item.name}}</Col>
                <Col span="9">{{item.surplus}}</Col>
            </Row>
            </Col>
            <Col span="12" class="column-pd">
            <Row v-for="(item, index) in rightDatas" class="stock-row row-center">
                <Col span="6">{{index+10}}</Col>
                <Col span="9">{{item.name}}</Col>
                <Col span="9">{{item.surplus}}</Col>
            </Row>
            </Col>
        </Row>
        <audio :src="action==='出库'?'/static/audio/outStockEnd.mp3':'/static/audio/inStockEnd.mp3'" autoplay></audio>
    </div>
</template>

<script>
    import moment from 'moment';
    import { mapGetters } from "vuex";

    export default {

        data() {
            return {
                action:'',
                outVaccineIds:[],
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
            async clearVaccineNumber(){
                await this.$api.post("/vaccine/clearVaccineTotal", {
                    device: this.device._id,
                    ids: this.outVaccineIds
                });
            },
            async getDetails() {
                let res = await this.$api.get("/vaccine/queryVaccineStorageNum", {
                    device: this.device._id,
                    ids: this.outVaccineIds
                });
                this.datas = res.data.rs;
                if(this.datas.length>10){
                    this.leftDatas = _.slice(this.datas,0,10);
                    this.rightDatas = _.slice(this.datas,10);
                }else{
                    this.leftDatas = this.datas;
                }
                //出库时需要对疫苗数量清零
                if(this.action == '出库'){
                    await this.clearVaccineNumber();
                }
            },
            goHome(){
                this.$router.push("/main");
            },
            setAction(val){
                this.action = (val=='in')?'入库':'出库';
            }
        },
        mounted() {
            //接收出入库参数
            this.outVaccineIds = this.$route.query.ids;
            this.action = this.$route.query.action;
            this.setAction(this.action);
            if(this.action=='入库'){
                this.leftDatas = _.slice(this.$route.query.inStockDate,0,10);
                this.rightDatas = _.slice(this.$route.query.inStockDate,10);
                console.log(this.$route.query.inStockDate)
            }else{
                this.getDetails();
            }
            
        }
    };
</script>

<style lang="less">
    @import "~@/style/stockDetail.less";
</style>