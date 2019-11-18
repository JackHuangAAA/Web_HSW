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
@import '~@/style/common.less';
@import '~@/style/stockDetail.less';
</style>