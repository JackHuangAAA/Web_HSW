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
                                    <div class="cut" @click="delKind(index,mainIndex)"></div>
                                </div>
                                <div class="selectBlock" v-for="(item,index) in main.select"  v-if="item.ifShow" :class="{select1: index == 0,select2: index ==1,select3: index ==2,select4: index ==3,select5: index ==4,select6: index ==5}">
                                    <Select v-model="item.code" style="width:calc(100% - 20px)" :label-in-value="true" @on-change="changedAdd(index,mainIndex,$event)">
                                        <Option v-for="item in kindList" :value="item.code" :key="item.code">{{ item.name }}</Option>
                                    </Select>
                                    <img src="/static/img/add.png" class="addImg" @click="addKind(index,mainIndex)">
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
                cabineData: [],
                kindList: [],
                vaccineName:''
            };
        },
        computed: {
            ...mapGetters({
                device: "device"
            })
        },
        methods: {
            async queryDrawerByCondition(){
                this.cabineData = [];
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    device: this.device._id
                });
                let array = res.data;
                for (let i = 0; i < 12; i++) {
                    let num = array[i].vaccine.length, vaccine = array[i].vaccine, added=[],select = [];
                    if (num > 0) {
                        for (let k = 0; k < num; k++) {
                            let obj = {};
                            //obj.drawerId = array[i]._id; //抽屉id
                            obj.vaccineId = vaccine[k]._id; //疫苗id
                            obj.name = vaccine[k].name;
                            obj.code = vaccine[k].code;
                            obj.surplus = vaccine[k].surplus;
                            obj.ifShow = true;
                            added.push(obj);
                        }
                        //select显示规则：
                        //1个值，显示第0个select；2个值，显示第1个select
                        //2个值，显示第2个select；4个值，显示第3个select
                        //3个值，显示第4个select；最多显示6个值
                        for(let x=0;x<6;x++){
                            if(x==(num-1) && num!=6){
                                select.push({ifShow:true,ifAdd:false,value: ''});
                            }else{
                                select.push({ifShow:false,ifAdd:false,value: ''});
                            }
                        }
                        array[i].added = added;
                        array[i].select = select;
                    }else{
                        for(let k=0;k<6;k++){
                            added.push({ifShow:false});
                            if(k==5){
                                //无数据显示最后一个select（第5个）
                                select.push({ifShow:true,ifAdd:false,value: ''}); //显示select
                            }else{
                                select.push({ifShow:false,ifAdd:false,value: ''});//不显示select
                            }
                        }
                        array[i].added = added;
                        array[i].select = select;
                    }
                    this.cabineData.push(array[i]);
                }
            },
            async queryVaccineKinds(){
                let res = await this.$api.get("/zcy/queryVaccineKinds");
                this.kindList = res.data;
            },
            async addKind(index,mainIndex){
                if(this.cabineData[mainIndex].select[index].ifAdd){
                    let drawer = this.cabineData[mainIndex];
                    //判断同抽屉是否已经存在要增加的疫苗
                    let types = drawer.added;
                    for(let r=0;r<types.length;r++){
                        if(types[r].code == drawer.select[index].code){
                            this.$Message.info({
                                top: 300,
                                content: '该疫苗在抽屉已经存在',
                                duration: 10,
                                closable: true
                            });
                            drawer.select[index].code = '';
                            this.vaccineName='';
                            return false;
                        }
                    }
                    //增加疫苗信息到DB
                    await this.$api.post("/drawer/modifyDrawerById", {
                        id: drawer._id,
                        vaccine: {
                            device: this.device._id,   //设备
                            code: drawer.select[index].code,   //疫苗编号
                            name: this.vaccineName  //疫苗名称
                        }
                    });
                    this.queryDrawerByCondition();
                }
                this.$forceUpdate();
            },
            async delKind(index,mainIndex){
                //删除疫苗信息从DB
                let drawer = this.cabineData[mainIndex];
                let surplus = drawer.added[index].surplus;
                if(surplus>0){
                    this.$Message.info({
                        top: 300,
                        content: '该疫苗库存大于0，不允许删除',
                        duration: 10,
                        closable: true
                    });
                    return false;
                }else{
                    await this.$api.post("/drawer/modifyDrawerByIdDec", {
                        id: drawer._id,
                        vaccineId:  drawer.added[index].vaccineId //疫苗id
                    });
                    this.queryDrawerByCondition();
                    this.$forceUpdate();
                }
            },
            changedAdd: function(index,mainIndex,event){
                if(event==undefined){ return false;}//删除时，阻止异常报错
                this.vaccineName = event.label;//新选择的疫苗名称
                this.cabineData[mainIndex].select.forEach(item =>{
                    item.ifAdd = false;
                });
                this.cabineData[mainIndex].select[index].ifAdd = true;
            }
        },
        mounted() {
            this.queryVaccineKinds();
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
