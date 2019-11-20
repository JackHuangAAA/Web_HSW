<template>
    <div class="main-table">
        <Row>
            <Col span="7" class="main-table-title">出入库记录查询</Col>
            <Col span="5" class="main-table-search">
                <div class="main-table-search-lab">单位:</div>                    
                <input v-model="unitName" placeholder="" @keyup.enter="search_queryInouts()"/>
            </Col>
            <Col span="5" class="main-table-box">
                <div class="main-table-box-lab">类型:</div>                    
                <Select v-model="select" clearable @on-change="search_queryInouts()">
                    <Option v-for="(item,index) in select_type" :value="item.key" :key="index">{{ item.name }}</Option>
                </Select>
            </Col>
            <Col span="5" class="main-table-box">
                <div class="main-table-box-time">时间:</div>
                <DatePicker :value="dateTime" format="yyyy/MM/dd" @on-change="dateChange" type="daterange" placement="bottom-end" placeholder="请选择日期" ></DatePicker>
            </Col>
            <Col span="2"><Button type="primary" class="search_btn" @click="search_queryInouts" icon="ios-search">查询</Button></Col>
        </Row>
        <Table :columns="cols" :data="lists" size="small" max-height=435 class="table-mt" stripe border></Table>
        <Row>
            <Page :total="total" show-sizer show-total @on-page-size-change="pageSizeChange" :current="search_type?search_active:active" @on-change="indexChange" :page-size="10"/>
        </Row>        
    </div>
</template>
<script>
import moment from 'moment'
export default {
    data(){
        return{
            unitName:'',
            select:0,
            dateTime: [],
            active:1,
            lists:[],
            total:0,
            pageSize:10,
            search_type:false,
            search_active:1,
            select_type:[
                {
                    name:'接种柜',
                    key:1
                },
                {
                    name:'冷藏柜',
                    key:2
                }
            ],
            cols: [
                {
                    type: 'index',
                    width:100,
                    align: 'center'
                },{
                    title: '设备类型',
                    key: 'type',
                    render:(h,param)=>{
                        return h(
                            'div',
                            param.row.deviceType==1?'接种柜':'冷藏柜'
                        )
                    }
                },{
                    title: '所在单位',
                    key: 'unitName'
                },{
                    title: '操作批次号',
                    key: '_id'
                },{
                    title: '疫苗数量',
                    key: 'count'
                },{
                    title: '操作类型',
                    key: 'type',
                    render:(h,param)=>{
                        return h(
                            'div',
                            param.row.type==1?'入库':'出库'
                        )
                    }
                },{
                    title: '操作',
                    align: 'center',
                    render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'default',
                                        size: 'small'
                                    },
                                    on: {
                                        click: () => {
                                            this.routerTo(
                                                params.row._id
                                            )
                                        }
                                    }
                                }, '查看详情')
                            ]
                        )
                    }
                }
            ],
        }
    },
    methods:{
        queryInouts(){
            this.search_active=1;
            this.search_type=false;
            this.$api.get('/inout/queryInoutsBybatchId',{size:this.pageSize,page:this.active}).then(res=>{
                let data=res.data.rs;
                this.total=res.data.total;
                this.lists=data;
            })
        },
        search_queryInouts(){
            this.active=1;
            this.search_type=true;
            let formdata={
                size:this.pageSize,
                page:this.search_active,
                deviceType:this.select==0?'':this.select,
                unitName:this.unitName==''?'':this.unitName,
                begin:this.dateTime[0],
                end:this.dateTime[1]
            }
            this.$api.get('/inout/queryInoutsBybatchId',formdata).then(res=>{
                let data=res.data.rs;
                this.total=res.data.total;
                this.lists=data;
            })
        },
        indexChange(i){
            if(!this.search_type){
                this.active=i;
                this.queryInouts();
            }else{
                this.search_active=i;
                this.search_queryInouts();
            }
            
        },
        dateChange(daterange){
            this.dateTime=daterange;
            this.search_queryInouts();
        },
        routerTo(_id){
            this.$router.push({path:'/inout/inoutDetail',query:{_id:_id}});
        },
        pageSizeChange(i){
            this.pageSize=i;
            this.search_queryInouts();
        }
    },
    mounted(){
        this.queryInouts();
    },
}
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
@import '../../style/common.less';
</style>