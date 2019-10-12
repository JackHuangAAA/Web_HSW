<template>
  <div class="basicSetting bsmain card">
    <Row class="bs-row"
         type="flex"
         :gutter="8"
         align="middle">
      <Col span="4"
           justify="end">
      <p class="bs-title">
        所在区域
      </p>
      </Col>
      <Col span="6"
           push="1">
      <Select :value="device.address.provinceName"
              :disabled="true">
        <Option :value="device.address.provinceName">{{device.address.provinceName}}</Option>
      </Select>
      </Col>
      <Col span="6"
           push="1">
      <Select :value="device.address.cityName"
              :disabled="true">
        <Option :value="device.address.cityName">{{device.address.cityName}}</Option>
      </Select>
      </Col>
      <Col span="6"
           push="1">
      <Select :value="device.address.countyName"
              :disabled="true">
        <Option :value="device.address.countyName">{{device.address.countyName}}</Option>
      </Select>
      </Col>
    </Row>
    <Row class="bs-row"
         align="middle"
         type="flex">
      <Col span="4"
           justify="end">
      <p class="bs-title">
        所在接种台
      </p>
      </Col>
      <Col span="18"
           push="1">
      <Select v-model="device.cabinetNo"
              :disabled="true">
        <Option :value="device.cabinetNo">{{device.cabinetNo}}</Option>
      </Select>
      </Col>
    </Row>
    <Row class="bs-row"
         align="middle"
         type="flex">
      <Col span="4"
           justify="end">
      <p class="bs-title">
        所在单位
      </p>
      </Col>
      <Col span="18"
           push="1">
      <Select v-model="device.unitName"
              :disabled="true">
        <Option :value="device.unitName">{{device.unitName}}</Option>
      </Select>
      </Col>
    </Row>
    <Row class="bs-row"
         align="middle"
         type="flex">
      <Col span="4"
           justify="end">
      <p class="bs-title">
        设备编号
      </p>
      </Col>
      <Col span="18"
           push="1">
      <Input v-model="alias"
             size="large"
             placeholder="设备编号" />
      </Col>
    </Row>
    <Row type="flex"
         justify="center">
      <p class="bs-info">更改基本信息后请点击保存按钮</p>
    </Row>
    <Row type="flex"
         class="bs-submit"
         justify="center">
      <Button type="primary">保存</Button>
    </Row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "basicSetting",
  data() {
    return {
      alias: ""
    };
  },
  computed: {
    ...mapGetters({
      device: "device"
    })
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.alias = this.device.alias;
    }
  }
};
</script>

<style lang="less">
.bs-submit {
  margin-top: 40px;
  button {
    width: 175px;
    height: 52px;
  }
}
.basicSetting {
}
.bs-row {
  height: 50px;
  margin-bottom: 5%;
}
.bs-title {
  text-align: end;
  font-size: 16px;
  font-weight: bold;
  color: rgba(62, 73, 85, 1);
}
.bs-info {
  font-size: 18px;
  font-weight: 400;
  color: rgba(130, 130, 130, 1);
}
</style>