<template>
  <div class="inbound">
    <div class="bound-header">
      <nav-header :lists="indexlist"
                  :active="1" :comeback="0">请选择出库疫苗</nav-header>
    </div>
    <div class="bound-content">
      <scan-table order="2">
      </scan-table>
    </div>
    <div class="scantable-buttons">
      <div class="scantable-button" @click="routerto('outcomplete')">出库完成</div>
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
import indexlist from './indexlist.js'
import navHeader from '_c/main/NavHeader';
import scanTable from '_c/main/scanTable';
import datetime from '_c/datetime';
import close from '_a/close.png'
import alarm from '_a/alarm.png'
import scan from '_a/scan.png';
export default {
  name: 'outbound',
  methods:{
    closentc(){
      this.noticejudge=false
    },
    routerto(link){
      this.$router.push({name:link})
    },
  },
  components:{
    navHeader,
    scanTable,
    datetime,
  },
  data () {
    return {
      indexlist,
      scan,
      close,
      alarm,
      noticejudge:true,
      order:0
    }
  }
}
</script>

<style lang="less">
@import '~@/style/main/notice.less';
.inbound {
  height: 100%;
}
.bound-header {
  height: 70px;
}
.bound-content {
  background: #fff;
}
.datetime-slot{
  display: flex;
}
.datetime-slot div{
  margin-left: 13px;
}
// 按钮
.scantable-buttons{
  display: flex;
  justify-content: flex-end;
}
.scantable-button{
  width: 175px;
  height: 52px;
  font-size: 18px;
  line-height: 52px;
  color: #fff;
  background:rgba(42,122,255,1);
  text-align: center;
  cursor: pointer;
  margin-top: 15px;
  margin-right: 19px;
}
</style>