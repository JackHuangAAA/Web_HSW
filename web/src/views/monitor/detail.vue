<template>
    <div class="main-table">
        <Row>
            <Col span="12" class="main-table-title">疫苗柜运行监控</Col>
            <Col span="5" class="main-table-search">
                <div class="main-table-search-lab">单位:</div>                    
                <input v-model="unitName" placeholder="" @keyup.enter="search_queryDevice()" />
            </Col>
            <Col span="5" class="main-table-box">
                <div class="main-table-box-lab">类型:</div>                    
                <Select v-model="select" class="select" clearable @on-change="search_queryDevice()">
                    <Option v-for="(item,index) in select_type" :value="item.key" :key="index">{{ item.name }}</Option>
                </Select>
            </Col>
            <Col span="2"><Button type="primary" class="search_btn" @click="search_queryDevice()" icon="ios-search">查询</Button></Col>
        </Row>
        <Table :columns="cols" :data="lists" size="small" class="table-mt" stripe border></Table>      
        <!-- <Row class="main-table-head">
            <Col span="2" class="id-center">序号</Col>
            <Col span="3">设备类型</Col>
            <Col span="5">设备编号</Col>
            <Col span="5">所在单位</Col>
            <Col span="3">当前温度</Col>
            <Col span="3">运行状态</Col>
            <Col span="3">原因</Col>
        </Row>
        <div class="table-body">
            <Row v-for="(item,index) of lists" :key="index" class="main-table-body">
                <Col span="2" class="id-center">{{index+1}}</Col>
                <Col span="3">{{item.type==1?'接种柜':'冷藏柜'}}</Col>
                <Col span="5">{{item.alias||'--'}}</Col>
                <Col span="5">{{item.unitName||'--'}}</Col>
                <Col span="3" :class="{abnormal:item.temperature<0 || item.temperature>5}">{{item.temperature||'--'}}</Col>
                <Col span="3" :class="{abnormal:item.temperature<0 || item.temperature>5}">{{item.status==0?'在线':item.status==1?'离线':'故障'}}</Col>
                <Col span="3">{{item.notes||'--'}}</Col>
            </Row>
        </div> -->
        <Row>
            <Page :total="total" show-sizer show-total @on-page-size-change="pageSizeChange" :current="search_type?search_active:active" @on-change="indexChange" :page-size="10"/>
        </Row>        
    </div>
</template>
<script>
import moment from 'moment';
export default {
    data(){
        return{
            unitName:'',
            select:0,
            active:1,
            search_active:1,
            search_type:false,
            total:0,
            pageSize:10,
            lists:[],
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
                    title: '编号',
                    key: 'alias'
                },{
                    title: '所在单位',
                    key: 'unitName'
                },{
                    title: '当前温度',
                    key: 'temperature',
                    render:(h,param)=>{
                        return h(
                            'div',{
                                class: param.row.temperature<0 || param.row.temperature>5?'abnormal':''
                            },
                            param.row.temperature
                        )
                    }
                },{
                    title: '运行状态',
                    key: 'status',
                    render:(h,param)=>{
                        return h(
                            'div',{
                                class: param.row.status==0?'':'abnormal'
                            },
                            param.row.status==0?'在线':param.row.status==1?'离线':'故障'
                        )
                    }
                },{
                    title: '原因',
                    key: 'notes'
                }
            ],
        }
    },
    methods:{
        queryDevice(){
            this.search_active=1;
            this.search_type=false;
            this.$api.get("device/queryDevices",{page:this.active,size:this.pageSize}).then(res=>{
                let data=res.data.rs;
                this.total=res.data.total;
                this.lists=data;
            })
        },
        search_queryDevice(){
            this.active=1;
            this.search_type=true;
            let formdata={
                page:this.search_active,
                size:this.pageSize,
                type:this.select==0?'':this.select,
                unitName:this.unitName==''?'':this.unitName
            };
            this.$api.get('/device/queryDevices',formdata).then(res=>{
                let data=res.data.rs;
                this.lists=data;
                this.total=res.data.total;
            });
        },
        indexChange(i){
            if(!this.search_type){
                this.active=i;
                this.queryDevice();
            }else{
                this.search_active=i;
                this.search_queryDevice();
            }
        },
        pageSizeChange(i){
            this.pageSize=i;
        }
    },
    mounted(){
        this.queryDevice();
    }
}
</script>
<style lang="less" scoped>
@import '../../style/color';
@import '../../style/table';
</style>
