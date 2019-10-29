<template>
    <div class="main-table">
        <Row>
            <Col span="8" class="main-table-title">出入库记录查询</Col>
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
            <Col span="1"><Button type="primary" class="search_btn" @click="search_queryInouts">搜索</Button></Col>
        </Row>      
        <Row class="main-table-head">
            <Col span="1" class="id-center">序号</Col>
            <Col span="3">疫苗柜类型</Col>
            <Col span="3">所在单位</Col>
            <Col span="8">操作批次号</Col>
            <Col span="3">疫苗数量</Col>
            <Col span="3">操作类型</Col>
            <Col span="3">操作</Col>
        </Row>
        <div class="table-body">
            <Row v-for="(item,index) of lists" :key="index" class="main-table-body">
                <Col span="1" class="id-center">{{index+1}}</Col>
                <Col span="3">{{item.deviceType==1?'接种柜':'冷藏柜'}}</Col>
                <Col span="3">{{item.unitName}}</Col>
                <Col span="8">{{item._id||'--'}}</Col>
                <Col span="3">{{item.count||'--'}}</Col><!--2019-9-29 ~ 2019-9-29 -->
                <Col span="3">{{item.type==1?'入库':'出库'}}</Col>
                <Col span="3" class="view-detail">
                    <div @click="routerTo(item._id)">查看详情</div>
                </Col>
            </Row>
        </div>
        <Row>
            <Page :total="total" show-elevator :current="search_type?search_active:active" @on-change="indexChange" :page-size="10"/>
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
            ]
        }
    },
    methods:{
        queryInouts(){
            this.search_active=1;
            this.search_type=false;
            this.$api.get('/inout/queryInoutsBybatchId',{size:10,page:this.active,test:0}).then(res=>{
                let data=res.data.rs;
                this.total=res.data.total;
                this.lists=data;
            })
        },
        search_queryInouts(){
            this.active=1;
            this.search_type=true;
            let formdata={
                size:10,
                page:this.search_active,
                deviceType:this.select==0?'':this.select,
                unitName:this.unitName==''?'':this.unitName,
                begin:this.dateTime[0],
                end:this.dateTime[1],
                test:0
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
    },
    mounted(){
        this.queryInouts();
    },
}
</script>
<style lang="less" scoped>

</style>