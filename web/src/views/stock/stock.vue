<template>
    <div class="main-table">
        <Row>
            <Col span="12" class="main-table-title">疫苗柜库存监控</Col>
            <Col span="5" class="main-table-search">
                <div class="main-table-search-lab">单位:</div>                    
                <input v-model="unitName" placeholder="" @keyup.enter="search_queryDeviceStock()"/>
            </Col>
            <Col span="5" class="main-table-box">
                <div class="main-table-box-lab">类型:</div>                    
                <Select v-model="select" clearable @on-change="search_queryDeviceStock()">
                    <Option v-for="(item,index) in select_type" :value="item.key" :key="index">{{ item.name }}</Option>
                </Select>
            </Col>
            <Col span="2"><Button type="primary" class="search_btn" @click="search_queryDeviceStock" icon="ios-search">查询</Button></Col>
        </Row>
        <Table :columns="cols" :data="lists" size="small" class="table-mt" stripe border></Table>      
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
            active:1,
            lists:[],
            total:0,
            pageSize:10,
            search_active:1,
            search_type:false,
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
                            param.row.type==1?'接种柜':'冷藏柜'
                        )
                    }
                },{
                    title: '设备编号',
                    key: 'alias'
                },{
                    title: '所在单位',
                    key: 'unitName'
                },{
                    title: '所在接种台',
                    key: 'cabinetNo'
                },{
                    title: '库存状态',
                    key: 'flag',
                    render:(h,param)=>{
                        return h(
                            'div',{
                                class: param.row.flag!=0?'abnormal':''
                            },
                            param.row.flag
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
                                                params.row._id,
                                                params.row.type==1?'接种柜':'冷藏柜',
                                                params.row.alias?params.row.alias:'',
                                                params.row.unitName
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
        queryDeviceStock(){
            this.search_active=1;
            this.search_type=false;
            this.$api.get('/device/queryDeviceStock',{page:this.active,size:this.pageSize}).then(res=>{
                let data=res.data.rs;
                for(let i=0;i<data.length;i++){
                    data[i].createDate=moment(data[i].createDate).format('YYYY年MM月DD日HH:mm:ss');
                }
                this.total=res.data.total;
                this.lists=data;
            })
        },
        search_queryDeviceStock(){
            this.active=1;
            this.search_type=true;
            let formdata={
                page:this.search_active,
                size:this.pageSize,
                type:this.select==0?'':this.select,
                unitName:this.unitName==''?'':this.unitName
            }
            this.$api.get('/device/queryDeviceStock',formdata).then(res=>{
                let data=res.data.rs;
                for(let i=0;i<data.length;i++){
                    data[i].createDate=moment(data[i].createDate).format('YYYY年MM月DD日HH:mm:ss');
                }
                this.total=res.data.total;
                this.lists=data;
            })
        },
        indexChange(i){
            if(!this.search_type){
                this.active=i;
                this.queryDeviceStock();
            }else{
                this.search_active=i;
                this.search_queryDeviceStock();
            }
        },
        routerTo(_id,type,alias,position){
            this.$router.push({path:'/stock/stockDetail',query:{_id:_id,type,alias,position}});
        },
        pageSizeChange(i){
            this.pageSize=i;
            this.search_queryDeviceStock();
        }
    },
    mounted(){
        this.queryDeviceStock();
    },

}
</script>
<style lang="less" scoped>

</style>