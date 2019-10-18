<template>
  <div class="basicSetting">
    <Row class="bs-row"
         type="flex"
         :gutter="8"
         align="middle">
      <Col span="4"
           justify="end">
      <p class="bs-title area">
        所在区域
      </p>
      </Col>
      <Col span="6"
           push="1">
      <Select v-model="provinceVal">
        <Option v-for="item in provinces"
                :value="item"
                :key="item">{{item}}</Option>
      </Select>
      </Col>
      <Col span="6"
           push="1">
      <Select v-model="cityVal">
        <Option v-for="(item,index) in city"
                :value="index"
                :key="index">{{index}}</Option>
      </Select>
      </Col>
      
      <Col span="6"
           push="1">
      <Select v-model="areaVal">
        <Option v-for="item in area"
                :value="item"
                :key="item">{{item}}</Option>
      </Select>
      </Col>
    </Row>
    <!-- <Row class="bs-row"
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
      <Select v-model="model">
        <Option v-for="item in list"
                :value="value"
                :key="item"></Option>
      </Select>
      </Col>
    </Row> -->
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
      <Select v-model="unitVal">
        <Option v-for="item in unit"
                :value="item['code']"
                :key="item['code']" @click="()=>{console.log()}">{{item['name']}}</Option>
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
      <Input v-model="deviceName"
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
import cities from "./cities.js"
export default {
  name: 'basicSetting',
  data () {
    return {
      unit:[],
      unitVal:'',
      cities,//城市数据源
      provinces:[],
      provinceVal:'',
      city:[],
      cityVal:'',
      area:[],
      areaVal:'',
      deviceName:'',//设备名称
    }
  },
  created(){
    this.queryUnit()
    this.queryCities()
  },
  methods:{
    // 获取单位信息
    queryUnit(){
      let data=this.$api.get("/zcy/queryUnit")
      data.then(rs=>{
        console.log(rs)
        let unit=rs.data.filter( el =>el["name"])
        this.unit=unit;
        console.log(this.unit)
        this.unitVal=rs.data[0]['name']
      })
    },
    //获取地区信息
    queryCities(){
      _.forEach(this.cities,(val,key)=>{
        this.provinces.push(key)
      })
    },
    //监听省份
    provinceChange(){
      for(let item in this.cities){
        if(this.provinceVal== item){
          this.city=this.cities[this.provinceVal]
        }
      }
    },
    //监听城市
    cityChange(){
      for(let item in this.city){
        if(this.cityVal== item){
          this.area=this.city[this.cityVal]
          console.log(this.area)
        }
      }
    },
  },
  watch:{
    provinceVal(){
      this.provinceChange()
    },
    cityVal(){
      this.cityChange()
    },
  }
}
</script>

<style lang="less">
.ivu-tabs .ivu-tabs-bar {
  border-width: 0;
}
.ivu-tabs-nav .ivu-tabs-tab{
  margin-left: 14px;
  margin-right: 0;
}
.bs-submit {
  margin-top: 40px;
  button {
    width: 175px;
    height: 52px;
    font-size: 18px;
  }
}
.basicSetting {
  height: 100%;
  max-height: "~calc('100% - 80px')";
  width: 100%;
  padding: 5% 23%;
  background: #fff;
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
.area{
  margin-right: -7%;
}
</style>