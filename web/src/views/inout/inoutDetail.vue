<template>
    <div class="main-table main-tb">
        <Row>
            <Col span="24" class="stockDetail-table-title">{{alias}}<span>{{type}}</span><i>{{position}}</i></Col>
            <div class="comeback" @click="routerTo()">返回</div>
        </Row>
        <Table :columns="cols" :data="lists" size="small" class="table-mt" stripe border></Table>
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
            // select_type:{
            //     0:{
            //         name:'请选择'
            //     },
            //     1:{
            //         name:'入库'
            //     },
            //     2:{
            //         name:'出库'
            //     }
            // },
            lists:[],
            cols: [
                {
                    type: 'index',
                    width:100,
                    align: 'center'
                },{
                    title: '类型',
                    key: 'type',
                    render:(h,param)=>{
                        return h(
                            'div',
                            param.row.type==1?'入库':'出库'
                        )
                    }
                },{
                    title: '操作人',
                    render:(h,param)=>{
                        return h(
                            'div',
                            param.row.user==null?'':param.row.user.name
                        )
                    }
                },{
                    title: '疫苗名称',
                    key: 'name'
                },{
                    title: '操作时间',
                    key: 'createDate'
                },{
                    title: '疫苗数量',
                    render:(h,param)=>{
                        return h(
                            'div',
                            param.row.type==1?param.row.total:param.row.total-param.row.surplus
                        )
                    }
                }
            ],
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
@import '../../style/common.less';
</style>