<template>
    <div class="main-table">
        <Row>
            <Col span="7" class="main-table-title">温度运行监控</Col>
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
            <Col span="2" class="id-center">序号</Col>
            <Col span="2">疫苗柜类型</Col>
            <Col span="3">疫苗柜编号</Col>
            <Col span="5">所在单位</Col>
            <Col span="3">运行温度</Col>
            <Col span="4">温度状态</Col>
            <Col span="5">时间</Col>
        </Row>
        <Row v-for="item of 10" :key="item" class="main-table-body">
            <Col span="2" class="id-center">{{item}}</Col>
            <Col span="2">冷藏柜</Col>
            <Col span="3">Y750230-64368</Col>
            <Col span="5">西湖区蒋村街道社区卫生服务中心</Col>
            <Col span="3" :class="{abnormal:true}">18℃</Col>
            <Col span="4" :class="{abnormal:true}">库存缺少、问题疫苗</Col>
            <Col span="5">2019年9月29日09:31:35</Col>
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
            value3: ['2016-01-01', '2016-02-15'],
            active:3
        }
    }
}
</script>
<style lang="less" scoped>
@import '../../style/color';
@import '../../style/table';
</style>