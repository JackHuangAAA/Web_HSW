<!--区域划分-->
<template>
    <div class="container">
        <div class="mainTop">
                    <p class="ctOne">抽屉1</p>
                    <p class="ctLeft">左</p>
                    <img src="/static/img/l-r.png" class="l_r">
                    <p class="ctRight">右</p>
                    <p class="ctTwo">抽屉2</p>
                </div>
                <div class="mainBottom">
                    <div class="mainBottomLeft">
                        <p class="ctTop">上</p>
                        <img src="/static/img/t-b.png" class="t_b">
                        <p class="ctBottom">下</p>
                    </div>
                    <div class="mainBottomRight">
                        <div class="index">
                                <p class="indexBlock" v-for="(item,index) in row"><span class="indexSpan">第{{index+1}}行</span></p>
                        </div>
                        <div class="cabines">
                            <div class="cabine" v-for="(item,index) in cabineData">
                                <!-- <Input v-model="value" placeholder="选择疫苗名称" class="chooseVaccine"/> -->
                                <div class="noVaccine">
                                    <Select v-model="value" class="chooseVaccine">
                                        <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                    </Select>
                                </div>
                                <div class="vaccineName">
                                    麻疹疫苗
                                </div>
                                <div class="vaccineNameTwo">
                                    狂犬疫苗
                                </div>
                                <img src="/static/img/add.png" class="addImg addImg2" @click="addVaccine()">
                                <img src="/static/img/del.png" class="delImg delImg2">
                                <img src="/static/img/del.png" class="delImgTwo delImgTwo2">
                            </div>
                        </div>
                    </div>
                    <div class="finish">
                        <div class="finishButton">
                            完成
                        </div>
                    </div>
                </div>
    </div>
</template>

<script>
    import { mapGetters } from "vuex";
    export default {
        data() {
            return {
                row: 0,
                value: '',
                cabineData: [{},{},{},{},{},{},{},{}],
                cityList: [{value:22,label:22},{value:33,label:33}]
            };
        },
        computed: {
            ...mapGetters({
                device: "device"
            })
        },
        methods: {
            addVaccine: function(){

            }
        },
        mounted() {

        },
    };
</script>

<style lang="less" scoped>
    @import "~@/style/region.less";
</style>