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
        <!-- <Row class="main-table-head">
            <Col span="3" class="id-center">序号</Col>
            <Col span="3">类型</Col>
            <Col span="4">操作人</Col>
            <Col span="6">操作时间</Col>
            <Col span="5">疫苗数量</Col>
            <Col span="3">操作</Col>
        </Row>
        <Row  v-for="item of 10" :key="item" class="main-table-body">
            <Col span="3" class="id-center">{{item}}</Col>
            <Col span="3">入库</Col>
            <Col span="4">黄旭佳</Col>
            <Col span="6">2019-9-20 10:38:28</Col>
            <Col span="5" :class="{abnormal:true}">100</Col>
            <Col span="3" class="open-detail">展开详情</Col>
            <Col span="24" v-if="item==9">
                <div>
                    <Col span="3">序号</Col>
                    <Col span="3">疫苗名称</Col>
                    <Col span="4">批次号</Col>
                    <Col span="6">生产厂家</Col>
                    <Col span="5">有效期</Col>
                    <Col span="3">入库数量</Col>
                </div>
            </Col>
        </Row> -->
        <div class="list-row">
            <div>序号</div>
            <div>类型</div>
            <div>操作人</div>
            <div>操作时间</div>
            <div>疫苗数量</div>
            <div>操作</div>
        </div>
        <div v-for="(item,index) of list" :key="item.num"  class="list-column">
            <div class="list-row">
                <div span="3" class="id-center">{{item.num}}</div>
                <div span="3">入库</div>
                <div span="4">黄旭佳</div>
                <div span="6">2019-9-20 10:38:28</div>
                <div span="5" :class="{abnormal:true}">100</div>
                <div span="3" class="open-detail" @click="openShow(index)">展开详情</div>
            </div>
            <div class="list-row" :class="{none:item.isShow==false}">
                <div span="3" class="id-center">{{item.num}}</div>
                <div span="3">入库</div>
                <div span="4">黄旭佳</div>
                <div span="6">2019-9-20 10:38:28</div>
                <div span="5" :class="{abnormal:true}">100</div>
                <div span="3" class="open-detail">展开详情</div>
            </div>
        </div>
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
        getlist(){
            for(let i=0;i<4;i++){
                this.list[i]["isShow"]=false
            }
            console.log(this.list)
        },
        openShow(index){
            this.list[index]["isShow"]=!this.list[index]["isShow"]
            console.log(this.list)
        },
    }
}
</script>
<style lang="less" scoped>

</style>