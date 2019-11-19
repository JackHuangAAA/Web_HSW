<template>
    <div class="main-table">
        <Row>
            <Col span="7" class="main-table-title">温度运行监控</Col>
            <Col span="5" class="main-table-search">
                <div class="main-table-search-lab">单位:</div>                    
                <input v-model="unitName" placeholder="" @keyup.enter="search_queryTemperatures()"/>
            </Col>
            <Col span="5" class="main-table-box">
                <div class="main-table-box-lab">类型:</div>                    
                <Select v-model="select" clearable @on-change="search_queryTemperatures()">
                    <Option v-for="(item,index) in select_type" :value="item.key" :key="index">{{ item.name }}</Option>
                </Select>
            </Col>
            <Col span="5" class="main-table-box">
                <div class="main-table-box-time">时间:</div>
                <DatePicker :value="dateTime" format="yyyy/MM/dd" @on-change="dateChange" type="daterange" placement="bottom-end" placeholder="请选择日期"></DatePicker>
            </Col>
            <Col span="2"><Button type="primary" class="search_btn" @click="search_queryTemperatures" icon="ios-search">查询</Button></Col>
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
                    title: '设备编号',
                    key: 'device',
                    render:(h,param)=>{
                        return h(
                            'div',
                            param.row.device!=null?param.row.device.alias:''
                        )
                    }
                },{
                    title: '所在单位',
                    key: 'unitName'
                },{
                    title: '运行温度',
                    key: 'degree',
                    render:(h,param)=>{
                        return h(
                            'div',{
                                class: param.row.degree[0].value<0 || param.row.degree[0].value>5?'abnormal':''
                            },
                            param.row.degree[0].value+'℃'
                        )
                    }
                },{
                    title: '温度状态',
                    render:(h,param)=>{
                        return h(
                            'div',{
                                class: param.row.degree[0].value<0 || param.row.degree[0].value>5?'abnormal':''
                            },
                            param.row.degree[0].value>=0 && param.row.degree[0].value<=5?'正常':'异常'
                        )
                    }
                },{
                    title: '时间',
                    render:(h,param)=>{
                        return h(
                            'div',
                            param.row.degree[0].time
                        )
                    }
                }
            ],
        }
    },
    methods:{
        queryTemperatures(){
            this.search_active=1;
            this.search_type=false;
            this.$api.get('/temperature/queryTemperatures',{page:this.active,size:this.pageSize}).then(res=>{
                let data=res.data.rs;
                for(let i=0;i<data.length;i++){
                    data[i].degree[0].time=moment(data[i].degree[0].time).format('YYYY年MM月DD日HH:mm:ss');
                }
                this.lists=data;
                this.total=res.data.total;
            })
        },
        search_queryTemperatures(){
            this.active=1;
            this.search_type=true;
            let formdata={
                page:this.search_active,
                size:this.pageSize,
                deviceType:this.select==0?'':this.select,
                unitName:this.unitName==''?'':this.unitName,
                begin:this.dateTime[0]||'',
                end:this.dateTime[1]||''
            };
            this.$api.get('/temperature/queryTemperatures',formdata).then(res=>{
                let data=res.data.rs;
                for(let i=0;i<data.length;i++){
                    data[i].degree[0].time=moment(data[i].degree[0].time).format('YYYY年MM月DD日HH:mm:ss');
                }
                this.lists=data;
                this.total=res.data.total;
            })
        },
        indexChange(i){
            if(!this.search_type){
                this.active=i;
                this.queryTemperatures();
            }else{
                this.search_active=i;
                this.search_queryTemperatures();
            }
        },
        dateChange(daterange){
            this.dateTime=daterange;
            this.search_queryTemperatures();
        },
        pageSizeChange(i){
            this.pageSize=i;
            this.search_queryTemperatures();
        }
    },
    mounted(){
        this.queryTemperatures();
    }
}
</script>
<style lang="less" scoped>
@import '../../style/color.less';
@import '../../style/common.less';
</style>