<template>
    <div class="main-table main-tb">
        <Row>
            <Col span="24" class="stockDetail-table-title">{{alias}}<span>{{type}}</span><i>{{position}}</i></Col>
            <!-- <Col span="5" class="main-table-search">
                <div class="main-table-search-lab">疫苗名称:</div>                    
                <input v-model="value1" placeholder="" />
            </Col> -->
            <div class="comeback" @click="routerTo()">返回</div>
        </Row>
        <Table :columns="cols" :data="list" size="small" class="table-mt" stripe border></Table>      
        <!-- <Row class="main-table-head">
            <Col span="2" class="id-center">序号</Col>
            <Col span="10">疫苗名称</Col>
            <Col span="9">库存余量</Col>
            <Col span="3">状态描述</Col>
        </Row> -->
        <!-- <div class="table-body">
            <Row v-for="(item,index) of list" :key="index">
                <Row class="main-table-body">
                        <Col span="2" class="id-center">{{index+1}}</Col>
                        <Col span="10">{{item.name}}</Col>
                        <Col span="9" :class="{orange:item.surplus<10,abnormal:item.surplus==0}">{{item.surplus}}</Col>
                        <Col span="3" :class="{orange:item.surplus<10,abnormal:item.surplus==0}">{{item.surplus>10?'正常':'库存不足'}}</Col>
                    </div>
                </Row> -->
                <!-- <Row span="24" class="show" :class="{none:!item.isShow,}">
                    <Row class="detail-table-head">
                        <Col span="2" class="id-center">序号</Col>
                        <Col span="2">疫苗名称</Col>
                        <Col span="3">批次号</Col>
                        <Col span="5">生成厂家</Col>
                        <Col span="5">有效期</Col>
                        <Col span="4">库存余量</Col>
                        <Col span="3">状态描述</Col>
                    </Row>
                    <Row class="detail-table-body">
                        <Col span="2" class="id-center">01</Col>
                        <Col span="2">乙肝疫苗</Col>
                        <Col span="3">Y750230-64368</Col>
                        <Col span="5">北京天坛生物制品股份有限公司</Col>
                        <Col span="5">2019-09-18 12:30:28</Col>
                        <Col span="4">100</Col>
                        <Col span="3">库存不足</Col>
                    </Row>
                </Row> -->
            <!-- </Row>
        </div> -->
        <Row>
            <Page :total="total" show-sizer show-total @on-page-size-change="pageSizeChange" :current="active" @on-change="indexChange" :page-size="10"/>
        </Row>        
    </div>
</template>
<script>
export default {
    data(){
        return{
            active:1,
            _id:'',
            type:'',
            alias:'',
            pageSize:10,
            position:'',
            list:[],
            total:0,
            cols: [
                {
                    type: 'index',
                    width:100,
                    align: 'center'
                },{
                    title: '疫苗名称',
                    key: 'type',
                    render:(h,param)=>{
                        return h(
                            'div',
                            param.row._id.name
                        )
                    }
                },{
                    title: '库存余量',
                    render:(h,param)=>{
                        return h(
                            'div',{
                                class:param.row.count==0?'abnormal':param.row.count<10?'orange':''
                            },
                            param.row.count
                        )
                    }
                },{
                    title: '状态描述',
                    render:(h,param)=>{
                        return h(
                            'div',{
                                class:param.row.count==0?'abnormal':param.row.count<10?'orange':''
                            },
                            param.row.count>10?'正常':'库存不足'
                        )
                    }
                }
            ],
        }
    },
    wathch:{
        '$route.path'(to,from){
            this._id=this.$route.query._id;
        },
        _id(){
            this.queryDeviceByVaccineStock();
        },
    },
    methods:{
        async queryDeviceByVaccineStock(){
            let rs=await this.$api.get('/device/queryDeviceByVaccineStock',
            {deviceId:this._id,
            page:this.active,
            size:this.pageSize}).then(res=>{
                let data=res.data.rs;
                this.total=res.data.total;
                this.list=data;
            })
        },
        indexChange(i){
            this.active=i;
            this.queryDeviceByVaccineStock();
        },
        routerTo(){
            this.$router.go(-1);
        },
        pageSizeChange(i){
            this.pageSize=i;
            this.queryDeviceByVaccineStock();
        }
    },
    mounted(){
        this._id=this.$route.query._id;
        this.type=this.$route.query.type;
        this.alias=this.$route.query.alias;
        this.position=this.$route.query.position;
        this.queryDeviceByVaccineStock();
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
@import '~@/style/stockDetail.less';
</style>