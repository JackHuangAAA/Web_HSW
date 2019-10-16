<template>
    <div class="main-table">
        <Row>
            <Col span="13" class="main-table-title">疫苗柜运行监控</Col>
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
        </Row>      
        <Row class="main-table-head">
            <Col span="2" class="id-center">序号</Col>
            <Col span="3">疫苗柜类型</Col>
            <Col span="4">疫苗柜编号</Col>
            <Col span="6">所在单位</Col>
            <Col span="3">当前温度</Col>
            <Col span="3">运行状态</Col>
            <Col span="3">原因</Col>
        </Row>
        <Row v-for="item of 10" :key="item" class="main-table-body">
            <Col span="2" class="id-center">{{item}}</Col>
            <Col span="3">1</Col>
            <Col span="4">1</Col>
            <Col span="6">1</Col>
            <Col span="3" :class="{abnormal:true}">1</Col>
            <Col span="3" :class="{abnormal:true}">异常</Col>
            <Col span="3">1</Col>
        </Row>
        <Row>
            <Page :total="100" show-elevator :current="active"/>
        </Row>        
    </div>
</template>
<script>
export default {
    data(){
        return{
            value1:'',
            value2:'',
            active:2,
        }
    }
}
</script>
<style lang="less" scoped>
@import '../../style/color';
@import '../../style/table';
</style>
