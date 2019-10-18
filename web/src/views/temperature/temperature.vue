<template>
    <div class="main-table">
        <Row>
            <Col span="5" class="main-table-title">温度运行监控</Col>
            <Col span="6" class="main-table-search">
                <div class="main-table-search-lab">接种单位:</div>                    
                <input v-model="unitName" placeholder="" @keyup.enter="search_queryTemperatures()"/>
            </Col>
            <Col span="7" class="main-table-box">
                <div class="main-table-box-lab">疫苗柜类型:</div>                    
                <Select v-model="select">
                    <Option v-for="(item,index) in select_type" :value="index" :key="index">{{ item.name }}</Option>
                </Select>
            </Col>
            <Col span="6" class="main-table-box">
                <div>时间:</div>
                <DatePicker :value="dateTime" format="yyyy/MM/dd" type="daterange" placement="bottom-end" placeholder="Select date" style="width: 200px"></DatePicker>
            </Col>
        </Row>      
        <Row class="main-table-head">
            <Col span="2" class="id-center">序号</Col>
            <Col span="2">疫苗柜类型</Col>
            <Col span="3">疫苗柜编号</Col>
            <Col span="5">所在单位</Col>
            <Col span="3">运行温度</Col>
            <Col span="4">温度状态</Col>
            <Col span="5">时间</Col>
        </Row>
        <Row v-for="(item,index) of lists" :key="index" class="main-table-body">
            <Col span="2" class="id-center">{{index+1}}</Col>
            <Col span="2">{{item.deviceType==1?'接种柜':'冷藏柜'}}</Col>
            <Col span="3">Y750230-64368</Col>
            <Col span="5">{{item.unitName||'--'}}</Col>
            <Col span="3" :class="{abnormal:item.degree[0].value<0 || item.degree[0].value>5}">{{item.degree[0].value+'℃'||'不明'}}</Col>
            <Col span="4" :class="{abnormal:item.degree[0].value<0 || item.degree[0].value>5}">{{item.degree[0].value>=0 && item.degree[0].value<=5?'正常':'异常'}}</Col>
            <Col span="5">{{item.createDate}}</Col>
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
            dateTime: ['2019-09-01', '2019-10-15'],
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
        queryTemperatures(){
            this.search_active=1
            this.$api.get('/temperature/queryTemperatures',{page:this.active,size:10,test:0}).then(res=>{
                let data=res.data.rs
                for(let i=0;i<data.length;i++){
                    // data[i].createDate=moment(data[i].createDate).format('YYYY年MM月DD日HH:mm:ss')
                }
                this.lists=data
                this.total=res.data.total
                console.log(this.lists)
            })
        },
        search_queryTemperatures(){
            this.active=1
            let formdata={
                page:this.active,
                size:10,
                deviceType:this.select==0?'':this.select,
                unitName:this.unitName==''?'':this.unitName,
                begin:this.dateTime[0]||'',
                end:this.dateTime[1]||'',
                test:0
            }
            this.$api.get('/temperature/queryTemperatures',formdata).then(res=>{
                let data=res.data.rs
                for(let i=0;i<data.length;i++){
                    data[i].createDate=moment(data[i].createDate).format('YYYY年MM月DD日HH:mm:ss')
                }
                this.lists=data
                this.total=res.data.total
                console.log(this.lists)
            })
        },
        indexChange(i){
            this.active=i
            this.queryTemperatures()
        },
    },
    created(){
        this.queryTemperatures()
    },
    watch:{

    }
}
</script>
<style lang="less" scoped>
@import '../../style/color';
@import '../../style/table';
</style>