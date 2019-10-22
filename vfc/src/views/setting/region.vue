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
                            <div class="cabine" v-for="(main,mainIndex) in cabineData">
                                <div class = "added" v-for="(item,index) in main.added" v-if="item.ifShow">
                                    {{item.name}}
                                    <div class="cut" @click="cut(index,mainIndex)"></div>
                                </div>
                                <div class="selectBlock" v-for="(item,index) in main.select" v-if="item.ifShow" :class="{select1: index == 0,select2: index ==1,select3: index ==2,select4: index ==3,select5: index ==4,select6: index ==5}">
                                    <Select v-model="item.value" style="width:calc(100% - 20px)" @on-change="changedAdd(index,mainIndex)">
                                        <Option v-for="item in cityList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                    </Select>
                                    <img src="/static/img/add.png" class="addImg" @click="add(index,mainIndex)">
                                </div>
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
                row: 6,
                cabineData: [{},{},{},{},{},{},{},{}],
                cityList: [{value:22,label:22},{value:33,label:33}],
                added: [{name: '麻疹疫苗',ifShow:false},{name: '麻疹疫苗1',ifShow:false},{name: '麻疹疫苗2',ifShow:false},
                {name: '麻疹疫苗3',ifShow:false},{name: '麻疹疫苗4',ifShow:false},{name: '麻疹疫苗5',ifShow:false}],
                cityList: [{value:1,label:2}],
                select:[{ifShow:false},{ifShow:false},{ifShow:false},{ifShow:false},{ifShow:false},{ifShow:true}],
                model1: ''
            };
        },
        computed: {
            ...mapGetters({
                device: "device"
            })
        },
        methods: {
            async queryDrawerByCondition(){
                this.cabineDatas = [];
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    device: this.device._id
                });
                let array = res.data;
                for (let i = 0; i < 12; i++) {
                    let num = array[i].vaccine.length, vaccine = array[i].vaccine, temp = {};
                    temp.id = array[i]._id;
                    temp.single = false;
                    if (num > 0) {
                        for (let k = 0; k < num; k++) {
                            if (k == 0) {temp.nameOne = vaccine[k].name;}
                            if (k == 1) {temp.nameTwo = vaccine[k].name;}
                            if (k == 2) {temp.nameThree = vaccine[k].name;}
                            if (k == 3) {temp.nameFour = vaccine[k].name;}
                            if (k == 4) {temp.nameFive = vaccine[k].name;}
                            if (k == 5) {temp.nameSix = vaccine[k].name;}
                        }
                    } else {
                        temp.nameOne = '';
                    }
                    this.cabineDatas.push(temp);
                }
            },
            add: function(index,mainIndex){
                if(index == 5 && this.cabineData[mainIndex].select[5].ifAdd){
                    this.cabineData[mainIndex].select[0].ifShow = true;
                    this.cabineData[mainIndex].select[5].ifShow = false;
                    this.cabineData[mainIndex].added[0].ifShow = true;
                }
                if(index == 0 && this.cabineData[mainIndex].select[0].ifAdd){
                    this.cabineData[mainIndex].select[1].ifShow = true;
                    this.cabineData[mainIndex].select[0].ifShow = false;
                    this.cabineData[mainIndex].added[1].ifShow = true;
                }
                if(index == 1 && this.cabineData[mainIndex].select[1].ifAdd){
                    this.cabineData[mainIndex].select[2].ifShow = true;
                    this.cabineData[mainIndex].select[1].ifShow = false;
                    this.cabineData[mainIndex].added[2].ifShow = true;
                }
                if(index == 2 && this.cabineData[mainIndex].select[2].ifAdd){
                    this.cabineData[mainIndex].select[3].ifShow = true;
                    this.cabineData[mainIndex].select[2].ifShow = false;
                    this.cabineData[mainIndex].added[3].ifShow = true;
                }
                if(index == 3 && this.cabineData[mainIndex].select[3].ifAdd){
                    this.cabineData[mainIndex].select[4].ifShow = true;
                    this.cabineData[mainIndex].select[3].ifShow = false;
                    this.cabineData[mainIndex].added[4].ifShow = true;
                }
                if(index == 4 && this.cabineData[mainIndex].select[4].ifAdd){
                    this.cabineData[mainIndex].select[4].ifShow = false;
                    this.cabineData[mainIndex].added[5].ifShow = true;
                }
                this.$forceUpdate()
            },
            cut: function(index,mainIndex){
                if(index == 5){
                    this.cabineData[mainIndex].select[4].ifShow = true;
                    this.cabineData[mainIndex].added[5].ifShow = false;
                }
                if(index == 4 && this.cabineData[mainIndex].select[4].ifShow == true){
                    this.cabineData[mainIndex].select[4].ifShow = false;
                    this.cabineData[mainIndex].select[3].ifShow = true;
                    this.cabineData[mainIndex].added[4].ifShow = false;
                }
                if(index == 3 && this.cabineData[mainIndex].select[3].ifShow == true){
                    this.cabineData[mainIndex].select[3].ifShow = false;
                    this.cabineData[mainIndex].select[2].ifShow = true;
                    this.cabineData[mainIndex].added[3].ifShow = false;
                }
                if(index == 2 && this.cabineData[mainIndex].select[2].ifShow == true){
                    this.cabineData[mainIndex].select[2].ifShow = false;
                    this.cabineData[mainIndex].select[1].ifShow = true;
                    this.cabineData[mainIndex].added[2].ifShow = false;
                }
                if(index == 1 && this.cabineData[mainIndex].select[1].ifShow == true){
                    this.cabineData[mainIndex].select[1].ifShow = false;
                    this.cabineData[mainIndex].select[0].ifShow = true;
                    this.cabineData[mainIndex].added[1].ifShow = false;
                }
                if(index == 0 && this.cabineData[mainIndex].select[0].ifShow == true){
                    this.cabineData[mainIndex].select[0].ifShow = false;
                    this.cabineData[mainIndex].select[5].ifShow = true;
                    this.cabineData[mainIndex].added[0].ifShow = false;
                }
                this.cabineData[mainIndex].select[index].value = "";
                if(index == 0){
                    this.cabineData[mainIndex].select[5].value = "";
                    this.cabineData[mainIndex].select[5].ifAdd = false;
                }else{
                    this.cabineData[mainIndex].select[index-1].value = "";
                    this.cabineData[mainIndex].select[index-1].ifAdd = false;
                }
                this.$forceUpdate()
            },
            changedAdd: function(index,mainIndex){
                    this.cabineData[mainIndex].select.forEach(item =>{
                        item.ifAdd = false;
                    });
                    this.cabineData[mainIndex].select[index].ifAdd = true;
            }
        },
        mounted() {
            this.cabineData.forEach(item =>{
                // item.ifShowAdded = false;
                // item.ifShowSelect = false;
                item.added = [{name: '麻疹疫苗',ifShow:false},{name: '麻疹疫苗1',ifShow:false},{name: '麻疹疫苗2',ifShow:false},
                {name: '麻疹疫苗3',ifShow:false},{name: '麻疹疫苗4',ifShow:false},{name: '麻疹疫苗5',ifShow:false}];
                item.select = [{ifShow:false,ifAdd:false,value: ''},{ifShow:false,ifAdd:false,value: ''},{ifShow:false,ifAdd:false,value: ''},
                {ifShow:false,ifAdd:false,value: ''},{ifShow:false,ifAdd:false,value: ''},{ifShow:true,ifAdd:false,value: ''}];
            })
            //this.row = this.cabineData.length/2;
            this.queryDrawerByCondition();
        },
    };
</script>

<style lang="less" scoped>
    @import "~@/style/region.less";
</style>
<style>
    .ivu-tabs-tab:nth-child(4){
        width: 10rem!important
    }
</style>
