<template>
    <div>
        <div class="scantable" v-if="order==1">
            <Row class="scantable-head scantable-td">
                <Col span="3">序号</Col>
                <Col span="5">疫苗</Col>
                <Col span="6">批次号</Col>
                <Col span="5">位置信息</Col>
                <Col span="5">入库数量</Col>
            </Row>
            <Row class="scantable-td"
                :class="item%2==0?'scantable-selected':'scantable-select'"
                v-for="(item) of 6" :key="item">
                <Col span="3">{{item}}</Col>
                <Col span="5">狂犬疫苗</Col>
                <Col span="6">yHUG-7U940</Col>
                <Col span="5">（1，1）</Col>
                <Col span="5"><input type="text" disabled value="3" class="scantable-number"></Col>
            </Row>
        </div>
        <div class="scantable" v-else-if="order==2">
            <Row class="scantable-head scantable-td">
                <Col span="2">序号</Col>
                <Col span="4">疫苗名称</Col>
                <Col span="5">批次号</Col>
                <Col span="5">有效期</Col>
                <Col span="4">是否失效</Col>
                <Col span="4">出库数量</Col>
            </Row>
            <Row class="scantable-td"
                :class="item%2==0?'scantable-selected':'scantable-select'"
                v-for="(item) of 6" :key="item">
                <Col span="2">{{item}}</Col>
                <Col span="4">狂犬疫苗</Col>
                <Col span="5">yHUG-7U940</Col>
                <Col span="5">2019-09-18 12:30:28 </Col>
                <Col span="4" :class="item%3==0?{isNormal:true}:{}">正常</Col>
                <Col span="4"><input type="text" disabled value="3" class="scantable-number"></Col>
            </Row>
        </div>
    </div>
</template>
<script>
export default {
    name:'scanTable',
    data(){
        return{
            
        }
    },
    methods:{
        
    },
    props:{
        order:{
            type:String,
            default:''
        }
    },
    components:{
        
    }
}
</script>
<style lang="less" scoped>
.scantable{
    padding: 16px 19px 0px 17px;
    text-align: center; 
    min-height: 915px;
}
.scantable-head{
       
}
// 表格内容
.scantable-td{
    height:44px;
    line-height: 44px;
    margin-bottom: 6px;
    font-size: 15px;
    font-family:Microsoft YaHei;
    font-weight:400;
    color:rgba(31,40,51,1);
}
.scantable-selected{
    background:rgba(236,240,248,1);
}
.scantable-select{
    background:rgba(255,255,255,1);
    box-shadow:0px 0px 4px 0px rgba(255,42,0,1);
}
.scantable-number{
    width: 65px;
    height: 28px;
    text-align: center;
    background:rgba(255,255,255,1);
    border:1px solid rgba(220,220,220,1);
}
.isNormal{
    font-size:15px;
    font-family:Microsoft YaHei;
    font-weight:400;
    color:rgba(255,0,48,1);
}
</style>
