<template>
    <div class="main-table">
        <Row>
            <Col span="7" class="main-table-title">出入库记录查询</Col>
            <Col span="5" class="main-table-search">
                <div class="main-table-search-lab">接种单位:</div>                    
                <input v-model="unitName" placeholder="" @keyup.enter="search_queryInouts()"/>
            </Col>
            <Col span="6" class="main-table-box">
                <div class="main-table-box-lab">疫苗柜类型:</div>                    
                <Select v-model="select" @on-change="search_queryInouts()">
                    <Option v-for="(item,index) in select_type" :value="index" :key="index">{{ item.name }}</Option>
                </Select>
            </Col>
            <Col span="6" class="main-table-box">
                <div>时间:</div>
                <DatePicker :value="dateTime" format="yyyy/MM/dd" @on-change="dateChange" type="daterange" placement="bottom-end" placeholder="Select date" style="width: 200px"></DatePicker>
            </Col>
        </Row>      
        <Row class="main-table-head">
            <Col span="1" class="id-center">序号</Col>
            <Col span="2">疫苗柜类型</Col>
            <Col span="3">疫苗柜编号</Col>
            <Col span="5">所在单位</Col>
            <Col span="5">日期</Col>
            <Col span="2">入库总量</Col>
            <Col span="2">出库总量</Col>
            <Col span="2">库存剩余</Col>
            <Col span="2">操作</Col>
        </Row>
        <Row v-for="(item,index) of lists" :key="index" class="main-table-body">
            <Col span="1" class="id-center">{{index+1}}</Col>
            <Col span="2">{{item.deviceType==1?'接种柜':'冷藏柜'}}</Col>
            <Col span="3">{{item.alias||'--'}}</Col>
            <Col span="5">{{item.unitName||'--'}}</Col>
            <Col span="5">{{item.createDate}}</Col><!--2019-9-29 ~ 2019-9-29 -->
            <Col span="2">{{item.total||'--'}}</Col>
            <Col span="2">{{item.total-item.surplus||'--'}}</Col>
            <Col span="2">{{item.surplus||'--'}}</Col>
            <Col span="2" class="view-detail"><div @click="routerTo(item._id)">查看详情</div></Col>
        </Row>
        <Row>
            <Page :total="total" show-elevator :current="active" @on-change="indexChange" :page-size="10"/>
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
            search_active:1,
            select_type:{
                0:{
                    name:'请选择'
                },
                1:{
                    name:'接种柜'
                },
                2:{
                    name:'冷藏柜'
                }
            }
        }
    },
    methods:{
        queryInouts(){
            this.search_active=1
            this.$api.get('/inout/queryInouts',{size:10,page:this.active,test:0}).then(res=>{
                let data=res.data.rs
                for(let i=0;i<data.length;i++){
                    data[i].createDate=moment(data[i].createDate).format('YYYY年MM月DD日HH:mm:ss')
                }
                this.total=res.data.total
                this.lists=data
            })
        },
        search_queryInouts(){
            this.active=1
            let formdata={
                size:10,
                page:this.search_active,
                deviceType:this.select==0?'':this.select,
                unitName:this.unitName==''?'':this.unitName,
                begin:this.dateTime[0],
                end:this.dateTime[1],
                test:0
            }
            this.$api.get('/inout/queryInouts',formdata).then(res=>{
                let data=res.data.rs
                for(let i=0;i<data.length;i++){
                    data[i].createDate=moment(data[i].createDate).format('YYYY年MM月DD日HH:mm:ss')
                }
                this.total=res.data.total
                this.lists=data
            })
        },
        indexChange(i){
            this.active=i
            this.queryInouts()
        },
        dateChange(daterange){
            this.dateTime=daterange;
            this.search_queryInouts()
        },
        routerTo(_id){
            this.$router.push({path:'/inout/inoutDetail',query:{_id:_id}})
        },
    },
    created(){
        this.queryInouts()
    },
}
</script>
<style lang="less" scoped>

</style>