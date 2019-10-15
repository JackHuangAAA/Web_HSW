<!--设备基础-->
<template>
    <div>
        <Row class="bs-row" type="flex" :gutter="8" align="middle">
            <Col span="4" justify="end">
                <p class="bs-title">
                    所在区域
                </p>
            </Col>
            <Col span="6" push="1">
                <Select v-model="form.address.provinceCode" :label-in-value="true" @on-change="changeProvince">
                    <Option v-for="province in provinces" :value="province.code">{{province.name}}</Option>
                </Select>
            </Col>
            <Col span="6" push="1">
                <Select v-model="form.address.cityCode" :label-in-value="true" @on-change="changeCity">
                    <Option v-for="city in citys" :value="city.code">{{city.name}}</Option>
                </Select>
            </Col>
            <Col span="6" push="1">
                <Select v-model="form.address.countyCode" :label-in-value="true" @on-change="changeCounty">
                    <Option v-for="county in countys" :value="county.code">{{county.name}}</Option>
                </Select>
            </Col>
        </Row>
        <Row class="bs-row" align="middle" type="flex">
            <Col span="4" justify="end">
                <p class="bs-title">
                    所在接种台
                </p>
            </Col>
            <Col span="18" push="1">
                <Select v-model="form.cabinetNo" :label-in-value="true">
                    <Option v-for="list in cabinetNos" :value="list.value">{{list.value}}</Option>
                </Select>
            </Col>
        </Row>
        <Row class="bs-row" align="middle" type="flex">
            <Col span="4" justify="end">
                <p class="bs-title">
                    所在单位
                </p>
            </Col>
            <Col span="18" push="1">
                <Select v-model="form.unitCode" :label-in-value="true" @on-change="changeUnit">
                    <Option v-for="u in units" :value="u.code">{{u.name}}</Option>
                </Select>
            </Col>
        </Row>
        <Row class="bs-row" align="middle" type="flex">
            <Col span="4" justify="end">
                <p class="bs-title">
                    设备编号
                </p>
            </Col>
            <Col span="18" push="1">
                <Input v-model="form.alias" size="large" placeholder="设备编号" />
            </Col>
        </Row>
        <Row type="flex" justify="center">
            <p class="bs-info">更改基本信息后请点击保存按钮</p>
        </Row>
        <Row type="flex" class="bs-submit" justify="center">
            <Button type="primary" @click="save()">保存</Button>
        </Row>
    </div>
</template>

<script>
    import { mapGetters } from "vuex";
    export default {
        data() {
            return {
                provinces: [],
                citys: [],
                countys: [],
                cabinetNos: [],
                units: [],
                form:{
                    alias: "",
                    address:{
                        provinceCode: "",
                        provinceName: "",
                        cityCode: "",
                        cityName: "",
                        countyCode: "",
                        countyName: ""
                    },
                    cabinetNo: "",
                    unitCode: "",
                    unitName: ""
                }
            };
        },
        computed: {
            ...mapGetters({
                device: "device"
            })
        },
        methods: {
            async init() {
                await this.queryDistrict();
                await this.queryCabinetNo();
                await this.queryUnit();
                //获取设备信息
                this.form.alias = this.device.alias;
                this.form.address.provinceCode = this.device.address.provinceCode;
                this.form.address.cityCode = this.device.address.cityCode;
                this.form.address.countyCode = this.device.address.countyCode;
                this.form.cabinetNo = this.device.cabinetNo;
                this.form.unitCode = this.device.unitCode;
            },

            async queryUnit() {
                let res = await this.$api.get("/zcy/queryUnit");
                this.units = res.data;
            },
            async queryDistrict() {
                let res = await this.$api.get("/zcy/queryDistrict");
                res = res.data;
                this.provinces.push({name:res.name,code:res.code});
                this.citys = res.child;
                this.countys = res.child[0].child;
            },
            async queryCabinetNo() {
                let res = await this.$api.get("/zcy/queryCabinetNo");
                this.cabinetNos = res.data;
            },
            changeProvince(event) {
                this.form.address.provinceName = event.label;
            },
            changeCity(event) {
                this.form.address.cityName = event.label;
            },
            changeCounty(event){
                this.form.address.countyName = event.label;
            },
            changeUnit(event){
                this.form.unitName = event.label;
            },
            save(){
                //this.$refs[name].validate((valid) => {
                    //if (valid) { // 新增操作
                        //新增类型时，型号保存到this.typeForm.models，点击保存按钮才保存数据

                            this.$api.post('/device/modifyDevice', {
                                id: this.device._id,
                                ...this.form
                            }).then(response => {
                                this.$Message.success('修改设备归属成功!');
                            });

                    //}
                //});
            }

        },
        mounted() {
            this.init();
        },
    };
</script>

<style lang="less">
    @import "~@/style/basic.less";
</style>