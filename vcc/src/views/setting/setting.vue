<template>
  <Row style="height:100%">
    <Col class="tabs-style"
         span="24">
    <Tabs :animated="false">
      <TabPane label="标签一">
        <basic-setting style="height:100%"></basic-setting>
      </TabPane>
      <TabPane label="标签二">标签二的内容</TabPane>
      <TabPane label="标签三">标签三的内容</TabPane>
    </Tabs>
    </Col>
  </Row>
</template>

<script>
import basicSetting from './basicSetting'
export default {
  name: 'setting',
  components: {
    basicSetting
  },
  data () {
    return {

    }
  }
}
</script>

<style lang="less">
@import "~@/style/color";
.tabs-style {
  .ivu-tabs-bar {
    margin-bottom: 0px;
  }
  .ivu-tabs {
    height: 100%;
  }
  height: 100%;
}
.tabs-style .ivu-tabs-tab {
  padding: 12px 48px;
  width: 142px;
  height: 50px;
  background-color: @white;
  font-size: 16px;
  font-weight: bold;
}
.tabs-style .ivu-tabs-tab-active {
  height: 60px;
  border-radius: 10px 10px 0 0;
}
.tabs-style .ivu-tabs-content,
.ivu-tabs-tabpane {
  border-radius: 10px;
  height: 100%;
}
.tabs-style .ivu-tabs-ink-bar {
  height: 4px;
  background-color: @white!important;
  width: 142px !important;
}
</style>