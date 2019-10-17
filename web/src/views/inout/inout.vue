<template>
    <div class="main-table">
        <Row>
            <Col span="7" class="main-table-title">出入库记录查询</Col>
            <Col span="5" class="main-table-search">
                <div class="main-table-search-lab">接种单位:</div>                    
                <input v-model="value1" placeholder="" />
            </Col>
            <Col span="6" class="main-table-box">
                <div class="main-table-box-lab">疫苗柜类型:</div>                    
                <Select v-model="value2">
                    <Option v-for="item in 3" :value="item" :key="item">{{ item }}</Option>
                </Select>
            </Col>
            <Col span="6" class="main-table-box">
                <div>时间:</div>
                <DatePicker :value="value3" format="yyyy/MM/dd" type="daterange" placement="bottom-end" placeholder="Select date" style="width: 200px"></DatePicker>
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
            <Col span="2">{{item.type==1?'接种柜':'冷藏柜'}}</Col>
            <Col span="3">Y750230-64368</Col>
            <Col span="5">{{item.unitName||'--'}}</Col>
            <Col span="5">2019-9-29 ~ 2019-9-29</Col>
            <Col span="2">300</Col>
            <Col span="2">100</Col>
            <Col span="2">200</Col>
            <Col span="2" class="view-detail">查看详情</Col>
        </Row>
        <Row>
            <Page :total="100" show-elevator :current="active"/>
        </Row>        
    </div>
</template>
<script>
import moment from 'moment'
export default {
    data(){
        return{
            value1:'',
            value2:'',
            value3: ['2016-01-01', '2016-02-15'],
            active:1,
            lists:[],

        }
    },
    methods:{
        queryInouts(){
            this.$api.get('/inout/queryInouts',{size:10,page:this.active,test:0}).then(res=>{
                let data=res.data.rs
                for(let i=0;i<data.length;i++){
                    data[i].createDate=moment(data[i].createDate).format('YYYY年MM月DD日HH:mm:ss')
                }
                this.lists=data
            })
        },
        routerTo(){
            this.$router.push({})
        },
    },
    created(){
        this.queryInouts()
    },
}
</script>
<style lang="less" scoped>

</style>