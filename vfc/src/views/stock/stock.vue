<!--库存-->
<template>
    <div>
    <Row class="inoculate-head">
        <div class="history">
            缺少库存<span style="color:rgb(255,96,19);margin-left:5px">{{lackNumber}}种</span>
        </div>
        <div class="type">
            疫苗名称:
            <Select v-model="vaccineCode" style="width:70%" clearable @on-change="contextChange">
                <Option v-for="item in kindList" :value="item.code" :key="item.code">{{ item.name }}</Option>
            </Select>
        </div>
        <div class="date">
            生产厂家:
            <Input v-model="product" placeholder="请输入厂家" style="width:70%" clearable @on-change="contextChange"/>
            <!--<Select v-model="model1" style="width:70%">
                <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>-->
        </div>
    </Row>
    <div class="inoculate card">
        <Row class="inoculate-table-row">
            <Col span="4">疫苗名称</Col>
            <Col span="7">生产厂家</Col>
            <Col span="4">唯一号</Col>
            <Col span="6">有效期</Col>
            <Col span="3">库存数量</Col>
        </Row>
        <div class="inoculate-table">
            <Scroll :on-reach-bottom="handleReachBottom" :loading-text="loadText">
                <Row v-for="(item, index) in vaccineDatas" class="inoculate-table-row row-bg">
                    <Col span="4">{{item.name}}</Col>
                    <Col span="7" class="producer">{{item.product}}</Col>
                    <Col span="4">{{item.code}}</Col>
                    <Col span="6">{{dateFormat(item.expiry)}}</Col>
                    <Col span="3" :class="{alarmStatus:item.surplus<=10,dangerStatus:item.surplus==0}">{{item.surplus}}</Col>
                </Row>
            </Scroll>
        </div>
    </div>
</div>
</template>
<script>
    import {mapGetters} from 'vuex';
    export default {
        data() {
            return {
                cityList:[{label:'1',value:'1'}],
                vaccineCode: '',
                product:'',
                kindList:[],
                lackNumber:0,
                vaccineDatas:[],
                page:1,
                size:25,
                loadText:'正在加载……'
            }
        },
        computed: {
            ...mapGetters({
                device: 'device',
            })
        },
        components:{},
        methods: {
            //查询疫苗信息
            async lackVaccineNum(){
                let res = await this.$api.get("/vaccine/queryVaccineStorageNum", {
                    device: this.device._id,
                    surplusltTen: true,
                    code: this.vaccineCode,
                    product: this.product,
                    page:this.page,
                    size:this.size
                });
                let arr=res.data.rs;
                if(arr.length>0){
                    this.loadText='正在加载……';
                    this.vaccineDatas =this.vaccineDatas.concat(arr);
                    this.page++;
                }else{
                    this.loadText='已显示所有数据';
                }
                this.lackNumber = res.data.total;
            },
            async queryVaccineKinds(){
                let res = await this.$api.get("/zcy/queryVaccineKinds");
                this.kindList = res.data;
            },
            dateFormat(val){
                return moment(val).format('YYYY-MM-DD HH:mm:ss');
            },
            contextChange(){
                this.page=1;
                this.vaccineDatas=[];
                this.lackVaccineNum();
            },
            handleReachBottom () {
                 return new Promise(resolve => {
                    setTimeout(() => {
                        this.lackVaccineNum();
                        resolve();
                    }, 2000);
                });
            }
        },
        mounted() {
            this.lackVaccineNum();
            this.queryVaccineKinds();
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/stock.less";
</style>
