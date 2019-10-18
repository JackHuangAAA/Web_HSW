<template>
  <div class="fingerprint">
    <div type="success" class="finger-add">
      新增
    </div>
    <Row>
      <Col span="9" class="finger-font">序号</Col>
      <Col span="11" class="finger-font">指纹</Col>
      <Col span="4" class="finger-font">操作</Col>
    </Row>
    <Row class="finger-row-bg" v-for="(item) of 6" :key="item">
      <Col span="9" class="finger-td">{{item}}</Col>
      <Col span="11" class="finger-td">手指{{item}}</Col>
      <Col span="4" class="finger-td"><div class="finger-del">删除</div></Col>
    </Row>
  </div>
</template>

<script>
export default {
  name: 'fingerprint',
  data () {
    return {

    }
  }
}
</script>

<style lang="less">
.fingerprint{
  background: #fff;
  position: relative;
  padding: 0px 33px 0px 31px;
  color: #3E4955;
  font-weight: bold;
  font-size: 16px;
}
.finger-add{
  width: 97px;
  height: 40px;
  font-size: 14px;
  background: #00BFD0;
  color: #fff;
  text-align: center;
  line-height: 40px;
  cursor: pointer;
  position: absolute;
  right: 19px;
  top: -55px;
}
.finger-font{
  padding: 28px 14px 11px 14px;
}
.finger-td{
  padding: 17px 0 16px 21px;
}
.finger-td:nth-child(2n){
  padding: 17px 0 16px 14px;
}
.finger-td:nth-child(3n){
  padding: 17px 0 16px 0px;
}
.finger-row-bg{
  background: #ECF0F8;
  margin-top: 6px;
}
.finger-del{
  background: #fff;
  border:1px solid rgba(220,220,220,1);
  color: #2A7AFF;
  font-size: 14px;
  width: 65px;
  height: 28px;
  text-align: center;
  line-height: 28px;
  cursor: pointer;
}
</style>