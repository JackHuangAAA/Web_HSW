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
            <Col span="3" class="id-center">序号</Col>
            <Col span="9">疫苗名称</Col>
            <Col span="9">库存余量</Col>
            <Col span="3">状态描述</Col>
        </Row>
        <Row v-for="item of 10" :key="item" class="main-table-body">
            <Col span="3" class="id-center">{{item}}</Col>
            <Col span="9">冷藏柜</Col>
            <Col span="9" :class="{abnormal:true}">库存缺少、问题疫苗</Col>
            <Col span="3" class="view-detail">查看详情</Col>
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
            value1:''
        }
    }
}
</script>
<style lang="less" scoped>

</style>