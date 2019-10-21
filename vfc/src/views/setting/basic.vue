<!--设备基础-->
<template>
    <div class="basic">
        <Form ref="frmEdit" :model="formBasic" :rules="ruleValidate">
            <Row class="bs-row" type="flex" :gutter="8" align="middle">
                <Col span="4" justify="end">
                <p class="bs-title">
                    所在区域
                </p>
                </Col>
                <Col span="6" push="1">
                <FormItem prop="address.provinceCode">
                    <Select v-model="formBasic.address.provinceCode" :label-in-value="true" @on-change="changeProvince">
                        <Option v-for="province in provinces" :value="province.code">{{province.name}}</Option>
                    </Select>
                </FormItem>
                </Col>
                <Col span="6" push="1">
                <FormItem prop="address.cityCode">
                    <Select v-model="formBasic.address.cityCode" :label-in-value="true" @on-change="changeCity">
                        <Option v-for="city in citys" :value="city.code">{{city.name}}</Option>
                    </Select>
                </FormItem>
                </Col>
                <Col span="6" push="1">
                <FormItem prop="address.countyCode">
                    <Select v-model="formBasic.address.countyCode" :label-in-value="true" @on-change="changeCounty">
                        <Option v-for="county in countys" :value="county.code">{{county.name}}</Option>
                    </Select>
                </FormItem>
                </Col>
            </Row>
            <!-- <Row class="bs-row" align="middle" type="flex">
                <Col span="4" justify="end">
                <p class="bs-title">
                    所在接种台
                </p>
                </Col>
                <Col span="18" push="1">
                <FormItem prop="cabinetNo">
                    <Select v-model="formBasic.cabinetNo" :label-in-value="true">
                        <Option v-for="list in cabinetNos" :value="list.value">{{list.value}}</Option>
                    </Select>
                </FormItem>
                </Col>
            </Row> -->
            <Row class="bs-row" align="middle" type="flex">
                <Col span="4" justify="end">
                <p class="bs-title">
                    所在单位
                </p>
                </Col>
                <Col span="18" push="1">
                <FormItem prop="unitCode">
                    <Select v-model="formBasic.unitCode" :label-in-value="true" @on-change="changeUnit">
                        <Option v-for="u in units" :value="u.code">{{u.name}}</Option>
                    </Select>
                </FormItem>
                </Col>
            </Row>
            <Row class="bs-row" align="middle" type="flex">
                <Col span="4" justify="end">
                <p class="bs-title">
                    设备编号
                </p>
                </Col>
                <Col span="18" push="1">
                <FormItem prop="alias">
                    <Input v-model="formBasic.alias" size="large" placeholder="设备编号" />
                </FormItem>
                </Col>
            </Row>
            <Row type="flex" justify="center">
                <p class="bs-info">更改基本信息后请点击保存按钮</p>
            </Row>
            <Row type="flex" class="bs-submit" justify="center">
                <Button type="primary" @click="save('frmEdit')">保存</Button>
            </Row>
        </Form>
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
            formBasic: {
                alias: "",
                address: {
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
            },
            ruleValidate: {
                alias: [
                    { required: true, message: '请输入设备编号', trigger: 'blur' }
                ],
                cabinetNo: [
                    { required: true, type: 'string', message: '请选择接种台编号', trigger: 'change' }
                ],
                unitCode: [
                    { required: true, type: 'string', message: '请选择所在单位', trigger: 'change' }
                ],
                'address.provinceCode': [
                    { required: true, type: 'string', message: '请选择省份', trigger: 'change' }
                ],
                'address.cityCode': [
                    { required: true, type: 'string', message: '请选择市', trigger: 'change' }
                ],
                'address.countyCode': [
                    { required: true, type: 'string', message: '请选择区(县)', trigger: 'change' }
                ]
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
            //获取设备信息
            if (this.device.address) {// 如果设备基本信息已经初始化
                this.formBasic.alias = this.device.alias;
                this.formBasic.cabinetNo = this.device.cabinetNo;
                this.formBasic.unitCode = this.device.unitCode;
                //this.formBasic.unitName = this.device.unitName;
                this.formBasic.address.provinceCode = this.device.address.provinceCode;
                this.formBasic.address.cityCode = this.device.address.cityCode;
                this.formBasic.address.countyCode = this.device.address.countyCode;
                //this.formBasic.address.provinceName = this.device.address.provinceName;
                //this.formBasic.address.cityName = this.device.address.cityName;
                //this.formBasic.address.countyName = this.device.address.countyName;
            }
            await this.queryDistrict();
            await this.queryCabinetNo();
            await this.queryUnit();
        },
        async queryUnit() {
            let res = await this.$api.get("/zcy/queryUnit");
            this.units = res.data;
        },
        async queryDistrict() {
            let res = await this.$api.get("/zcy/queryDistrict");
            res = res.data;
            this.provinces.push({ name: res.name, code: res.code });
            this.citys = res.child;
            this.countys = res.child[0].child;
        },
        async queryCabinetNo() {
            let res = await this.$api.get("/zcy/queryCabinetNo");
            this.cabinetNos = res.data;
        },
        changeProvince(event) {
            this.formBasic.address.provinceName = event.label;
        },
        changeCity(event) {
            this.formBasic.address.cityName = event.label;
        },
        changeCounty(event) {
            this.formBasic.address.countyName = event.label;
        },
        changeUnit(event) {
            this.formBasic.unitName = event.label;
        },
        save(name) {
            this.$refs[name].validate((valid) => {
                if (valid) { // 新增操作
                    //新增类型时，型号保存到this.typeForm.models，点击保存按钮才保存数据
                    this.$api.post('/device/modifyDevice', {
                        id: this.device._id,
                        ...this.formBasic
                    }).then(response => {
                        this.$Message.success('修改设备归属成功!');
                    });

                }
            });
        }
    },
    mounted() {
        this.init();
    },
};
</script>

<style lang="less">
@import '~@/style/color.less';
@import "~@/style/basic.less";
</style>