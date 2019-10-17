<template>
    <div class="main-table main-tb">
        <Row>
            <Col span="19" class="stockDetail-table-title">Y750230-64368<span>冷藏柜</span><i>西湖区蒋村街道社区卫生服务中心</i></Col>
            <Col span="5" class="main-table-search">
                <div class="main-table-search-lab">疫苗名称:</div>                    
                <input v-model="value1" placeholder="" />
            </Col>
        </Row>      
        <Row class="main-table-head">
            <Col span="2" class="id-center">序号</Col>
            <Col span="10">疫苗名称</Col>
            <Col span="9">库存余量</Col>
            <Col span="3">状态描述</Col>
        </Row>
        <Row v-for="(item,index) of list" :key="index">
            <Row class="main-table-body">
                <div @click="()=>{$set(item,'isShow',!item.isShow)}" style="cursor:pointer">
                    <Col span="2" class="id-center">{{index+1}}</Col>
                    <Col span="10">冷藏柜</Col>
                    <Col span="9" :class="{abnormal:true}">100</Col>
                    <Col span="3" :class="{abnormal:true}">库存不足</Col>
                </div>
            </Row>
            <Row span="24" class="show" :class="{none:!item.isShow,}">
                <Row class="detail-table-head">
                    <Col span="2" class="id-center">序号</Col>
                    <Col span="2">疫苗名称</Col>
                    <Col span="3">批次号</Col>
                    <Col span="5">生成厂家</Col>
                    <Col span="5">有效期</Col>
                    <Col span="4">库存余量</Col>
                    <Col span="3">状态描述</Col>
                </Row>
                <Row class="detail-table-body">
                    <Col span="2" class="id-center">01</Col>
                    <Col span="2">乙肝疫苗</Col>
                    <Col span="3">Y750230-64368</Col>
                    <Col span="5">北京天坛生物制品股份有限公司</Col>
                    <Col span="5">2019-09-18 12:30:28</Col>
                    <Col span="4">100</Col>
                    <Col span="3">库存不足</Col>
                </Row>
            </Row>
        </Row>
        <Row>
            <div class="comeback">返回</div>
            <Page :total="100" show-elevator :current="active"/>
        </Row>        
    </div>
</template>
<script>
export default {
    data(){
        return{
            value1:'',
            active:3,
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
        getlist(){
            for(let i=0;i<4;i++){
                this.$set(this.list[i],"isShow",false)
            }
            console.log(this.list)
        },
    }
}
</script>
<style lang="less" scoped>

</style>