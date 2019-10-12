<template>
    <div>
        <div>
            <nav-header :lists="indexlist"
                :active="2" :comeback="0">请将入库疫苗扫码</nav-header>
        </div>
        <div class="card">
            <scan-table order="1"></scan-table>
        </div>
        <div class="scantable-buttons">
            <div class="scantable-continue scantable-button" @click="()=>{this.$router.go(-1)}">继续</div>
            <div class="scantable-complete scantable-button" @click="routerto('complete')">入库完成</div>
        </div>
        <!-- 警告提示 -->
        <div class="scan-notice" v-show="noticejudge">
            <Row class="scantable-close-btn">
                <img :src="close" @click="closentc()"/>
            </Row>
            <Row>
                <div class="scantable-notice-info">
                    <img :src="alarm"/>
                    <div>
                        <div class="scantable-notice-s">百白破疫苗</div>
                        <div>YH5683098批次有效期失效</div>
                        <div>请将此疫苗报废处理</div>
                    </div>
                </div>
                <div class="scantable-notice-sure" @click="closentc()">确定</div>
            </Row>
        </div>
        <div class="scan-notice-bg" @click="closentc()" v-show="noticejudge"></div>
        <Row>
            <datetime>
                <div class="datetime-slot">
                    <img :src="scan"/>
                    <div>提示：将疫苗条码对准识别口扫码</div>
                </div>
            </datetime>
        </Row>
    </div>
</template>
<script>
import indexlist from '../inbound/indexlist'
import navHeader from '_c/main/NavHeader';
import scanTable from '_c/main/scanTable';
import datetime from '_c/datetime';
import close from '_a/close.png'
import alarm from '_a/alarm.png'
import scan from '_a/scan.png';
export default {
    name:'scanList',
    data(){
        return{
            scan,
            close,
            alarm,
            noticejudge:true,
            order:0,
            indexlist
        }
    },
    
    methods:{
        closentc(){
            this.noticejudge=false
        },
        routerto(link){
            this.$router.push({name:link})
        }
    },
    components:{
        navHeader,
        scanTable,
        datetime,
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/main/notice.less';
.datetime-slot{
  display: flex;
}
.datetime-slot div{
  margin-left: 13px;
}
// 按钮
.scantable-buttons{
    padding:0 17px 17px 17px;
    display: flex;
    justify-content: space-between;
    margin-top: 10px;
    text-align: center;
}
.scantable-button{
    width: 175px;
    height: 52px;
    font-size: 18px;
    line-height: 52px;
    color: #fff;
    cursor: pointer;
}
.scantable-continue{
    background:rgba(3,197,199,1);
}
.scantable-complete{
    background:rgba(42,122,255,1);
}

</style>
