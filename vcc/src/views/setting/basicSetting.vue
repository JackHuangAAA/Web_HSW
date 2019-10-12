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
      <Select v-model="province"
              @on-change="changeProvince()">
        <Option v-for="d in district"
                :value="d.name">{{d.name}}</Option>
      </Select>
      </Col>
      <Col span="6"
           push="1">
      <Select v-model="city"
              @on-change="changeCity()">
        <Option v-for="city in citys"
                :value="city.name">{{city.name}}</Option>
      </Select>
      </Col>
      <Col span="6"
           push="1">
      <Select v-model="county"
              @on-change="changeCounty()">
        <Option v-for="county in countys"
                :value="county.name">{{county.name}}</Option>
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
      <Select v-model="cabinetNo">
        <Option v-for="list in cabinetNoList"
                :value="list.value">{{list.value}}</Option>
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
      <Select v-model="unit">
        <Option v-for="u in unitlist"
                :value="u.code">{{u.name}}</Option>
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
      <Button type="primary"
              @click="submit()">保存</Button>
    </Row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "basicSetting",
  data() {
    return {
      provinces: [],
      citys: [],
      countys: [],
      alias: "",
      provinceName: "",
      cityName: "",
      countyName: "",
      district: [],
      cabinetNo: "",
      cabinetNoList: "",
      unit: "",
      unitList: ""
    };
  },
  computed: {
    ...mapGetters({
      device: "device"
    })
  },
  created() {
    this.getinit();
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.provinceName = this.device.address.provinceName;
      this.cityName = this.device.address.cityName;
      this.countyName = this.device.address.countyName;
      this.alias = this.device.alias;
      this.cabinetNo = this.device.cabinetNo;
    },
    getinit() {
      this.queryDistrict();
      this.queryCabinetNo();
      this.queryUnit();
    },
    async queryUnit() {
      let res = await this.$api.get("/zcy/queryUnit");
      this.unitlist = res.data;
    },
    async queryDistrict() {
      let res = await this.$api.get("/zcy/queryDistrict");
      let district = res.data;
      this.district.push(district);
    },
    async queryCabinetNo() {
      let res = await this.$api.get("/zcy/queryCabinetNo");
      this.cabinetNoList = res.data;
    },
    changeProvince() {
      let citys = [];
      for (let d of this.district) {
        if (d.name === this.province) citys = d.child;
      }
      this.citys = citys;
    },
    changeCity() {
      let countys = [];
      for (let c of this.citys) {
        if (c.name === this.city) countys = c.child;
      }
      this.countys = countys;
    },
    changeCounty() {},
    submit() {
      
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