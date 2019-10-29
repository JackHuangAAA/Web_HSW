<template>
    <div class="main-table">
        <Row>
            <Col span="13" class="main-table-title">疫苗柜库存监控</Col>
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
            <Col span="1"><Button type="primary" class="search_btn" @click="search_queryDeviceStock">查询</Button></Col>
        </Row>      
        <Row class="main-table-head">
            <Col span="2" class="id-center">序号</Col>
            <Col span="3">设备类型</Col>
            <Col span="4">设备编号</Col>
            <Col span="5">所在单位</Col>
            <Col span="3">所在接种台</Col>
            <Col span="4">库存状态</Col>
            <Col span="3">操作</Col>
        </Row>
        <div class="table-body">
            <Row v-for="(item,index) of lists" :key="index" class="main-table-body">
                <Col span="2" class="id-center">{{index+1}}</Col>
                <Col span="3">{{item.type==1?'接种柜':'冷藏柜'}}</Col>
                <Col span="4">{{item.alias||'--'}}</Col><!--Y750230-64368-->
                <Col span="5">{{item.unitName||'不明'}}</Col>
                <Col span="3">1号接种台</Col>
                <Col span="4" :class="{abnormal:true}">{{item.flag==0?'正常':'库存缺少、问题疫苗'}}</Col>
                <Col span="3" class="view-detail"><div @click="routerTo(
                    item._id,
                    item.type==1?'接种柜':'冷藏柜',
                    item.alias?item.alias:'',
                    item.unitName)">查看详情</div></Col>
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
            active:1,
            lists:[],
            total:0,
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
            ]
        }
    },
    methods:{
        queryDeviceStock(){
            this.search_active=1;
            this.search_type=false;
            this.$api.get('/device/queryDeviceStock',{page:this.active,size:10,test:0}).then(res=>{
                let data=res.data;
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
                size:10,
                type:this.select==0?'':this.select,
                unitName:this.unitName==''?'':this.unitName,
                test:0
            }
            this.$api.get('/device/queryDeviceStock',formdata).then(res=>{
                let data=res.data;
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
        }
    },
    mounted(){
        this.queryDeviceStock();
    },

}
</script>
<style lang="less" scoped>

</style>