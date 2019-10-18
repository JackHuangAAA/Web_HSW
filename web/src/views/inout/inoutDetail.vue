<template>
    <div class="main-table main-tb">
        <Row>
            <Col span="18" class="stockDetail-table-title">Y750230-64368<span>冷藏柜</span><i>西湖区蒋村街道社区卫生服务中心</i><i>2019-9-20</i><i>~</i><i>2019-9-20</i></Col>
            <Col span="6" class="main-table-box">
                <div class="main-table-box-lab">类型:</div>                    
                <Select v-model="value1">
                    <Option v-for="item in 3" :value="item" :key="item">{{ item }}</Option>
                </Select>
            </Col>
        </Row>      
        <Row class="main-table-head">
            <Col span="3" class="id-center">序号</Col>
            <Col span="3">类型</Col>
            <Col span="4">操作人</Col>
            <Col span="6">操作时间</Col>
            <Col span="5">疫苗数量</Col>
            <Col span="3">操作</Col>
        </Row>
        <Row  v-for="(item,index) of list" :key="index">
            <Row class="main-table-body">
                <Col span="3" class="id-center">{{index+1}}</Col>
                <Col span="3">入库</Col>
                <Col span="4">黄旭佳</Col>
                <Col span="6">2019-9-20 10:38:28</Col>
                <Col span="5" :class="{abnormal:true}">100</Col>
                <Col span="3" class="open-detail"><div @click="()=>{$set(item,'isShow',!item.isShow)}">{{item.isShow==false?'展开详情':'收起'}}</div></Col>
            </Row>
            <Row span="24" class="show" :class="{none:!item.isShow,}">
                <Row class="detail-table-head">
                    <Col span="3" class="id-center">序号</Col>
                    <Col span="3">疫苗名称</Col>
                    <Col span="4">批次号</Col>
                    <Col span="6">生产厂家</Col>
                    <Col span="5">有效期</Col>
                    <Col span="3">入库数量</Col>
                </Row>
                <Row v-for="item of 6" :key="item" class="detail-table-body">
                    <Col span="3" class="id-center">{{item}}</Col>
                    <Col span="3">百白破疫苗</Col>
                    <Col span="4">Y750230-64368</Col>
                    <Col span="6">北京天坛生物制品股份有限公司</Col>
                    <Col span="5">2019-09-18 12:30:28</Col>
                    <Col span="3">80</Col>
                </Row>
            </Row>
        </Row>
        <Row>
            <div class="comeback" @click="routerTo()">返回</div>
            <Page :total="100" show-elevator :current="active"/>
        </Row>        
    </div>
</template>
<script>
export default {
    data(){
        return{
            value1:'',
            value2:1,
            active:2,
            list:[
                {'num':1},
                {'num':2},
                {'num':3},
                {'num':4},
            ]
        }
    },
    created(){
        this.getlist()
    },
    methods:{
        queryInouts(){
            this.$api.get('/inout/queryInouts',{}).then(res=>{
                
            })
        },
        getlist(){
            for(let i=0;i<4;i++){
                this.$set(this.list[i],"isShow",false)
            }
            console.log(this.list)
        },
        routerTo(){
            this.$router.go(-1)
        },
    }
}
</script>
<style lang="less" scoped>
</style>