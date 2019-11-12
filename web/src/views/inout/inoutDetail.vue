<template>
    <div class="main-table main-tb">
        <Row>
            <Col span="24" class="stockDetail-table-title">{{alias}}<span>{{type}}</span><i>{{position}}</i></Col>
            <!-- <i>2019-9-20</i><i>~</i><i>2019-9-20</i> -->
            <!-- <Col span="6" class="main-table-box">
                <div class="main-table-box-lab">类型:</div>                    
                <Select v-model="select">
                    <Option v-for="(item,index) in select_type" :value="index" :key="index">{{ item.name }}</Option>
                </Select>
            </Col> -->
            <div class="comeback" @click="routerTo()">返回</div>
        </Row>      
        <Row class="main-table-head">
            <Col span="3" class="id-center">序号</Col>
            <Col span="3">类型</Col>
            <Col span="4">操作人</Col>
            <Col span="5">疫苗名称</Col>
            <Col span="6">操作时间</Col>
            <Col span="3">疫苗数量</Col>
            <!-- <Col span="3">操作</Col> -->
        </Row>
        <div class="table-body">
            <Row  v-for="(item,index) of lists" :key="index">
                <Row class="main-table-body">
                    <Col span="3" class="id-center">{{index+1}}</Col>
                    <Col span="3">{{item.type==1?'入库':'出库'}}</Col>
                    <Col span="4">{{item.user==null?'--':item.user.name?item.user.name:'--'}}</Col>
                    <Col span="5">{{item.name}}</Col>
                    <Col span="6">{{item.createDate}}</Col>
                    <Col span="3">{{item.type==1?item.total:item.total-item.surplus}}</Col>
                    <!-- <Col span="3" class="open-detail"><div @click="()=>{$set(item,'isShow',!item.isShow)}">{{item.isShow==false?'展开详情':'收起'}}</div></Col> -->
                </Row>
                <!-- <Row span="24" class="show" :class="{none:!item.isShow,}">
                    <Row class="detail-table-head">
                        <Col span="3" class="id-center">序号</Col>
                        <Col span="3">疫苗名称</Col>
                        <Col span="4">批次号</Col>
                        <Col span="6">生产厂家</Col>
                        <Col span="5">有效期</Col>
                        <Col span="3">入库数量</Col>
                    </Row>
                    <Row v-for="item of 6" :key="item" class="detail-table-body">
                        <Col span="3" class="id-center">{{item}}</Col>
                        <Col span="3">百白破疫苗</Col>
                        <Col span="4">Y750230-64368</Col>
                        <Col span="6">北京天坛生物制品股份有限公司</Col>
                        <Col span="5">2019-09-18 12:30:28</Col>
                        <Col span="3">80</Col>
                    </Row>
                </Row> -->
            </Row>
        </div>
        
        <Row>
            <Page :total="total" show-sizer show-total @on-page-size-change="pageSizeChange" :current="active" @on-change="indexChange" :page-size="10"/>
        </Row>        
    </div>
</template>
<script>
import moment from 'moment'
export default {
    data(){
        return{
            active:1,
            total:0,
            _id:'',//批次号
            date:'',
            type:'',
            position:'',
            alias:'',
            pageSize:10,
            select_type:{
                0:{
                    name:'请选择'
                },
                1:{
                    name:'入库'
                },
                2:{
                    name:'出库'
                }
            },
            lists:[]
        }
    },
    methods:{
        queryInouts(){
            this.search_active=1
            this.$api.get('/inout/queryInouts',{size:10,page:this.active,batchId:this._id}).then(res=>{
                let data=res.data.rs;
                for(let i=0;i<data.length;i++){
                    data[i].createDate=moment(data[i].createDate).format('YYYY年MM月DD日HH:mm:ss');
                }
                this.total=res.data.total;
                this.lists=data;
                this.date=data[0].createDate;
                this.type=data[0].deviceType==1?'接种柜':'冷藏柜';
                this.alias=data[0].device==null?'--':data[0].device.alias?data[0].device.alias:'--';
                this.position=data[0].unitName;
            })
        },
        indexChange(i){
            this.active=i;
            this.queryInouts();
        },
        dateChange(daterange){
            this.dateTime=daterange;
        },
        routerTo(){
            this.$router.go(-1);
        },
        pageSizeChange(i){
            this.pageSize=i;
            this.queryInouts();
        }
    },
    mounted(){
        this._id=this.$route.query._id;
        this.queryInouts();
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
@import '~@/style/inoutDetail.less';
</style>