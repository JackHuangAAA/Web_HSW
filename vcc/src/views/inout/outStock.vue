<!--出库-->
<template>
    <div style="position:relative">
        <div class="container">
            <div class="inStockTitle">
                    <img src="/static/img/back.png" class="back" @click="back()">
                    <p class="headP">请将抽屉中疫苗全部取出</p>
                    <img src="/static/img/outCabineSetp1.png" class="vaccineIn">
            </div>
            <div class="main">
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
                            <div class="cabine" v-for="(item,index) in cabineDatas">
                                <Checkbox v-model="item.single" class="checkBox" v-if="item.nameOne"></Checkbox>
                                <div class="cabineLeft" v-if="item.nameOne">
                                    <p class="vaccineOneName">{{item.nameOne}}</p>
                                    <p class="vaccineOneCount">{{item.countOne||0}}支</p>
                                </div>
                                <div class="cabineRight" v-if="item.nameTwo">
                                    <p class="vaccineTwoName">{{item.nameTwo}}</p>
                                    <p class="vaccineTwoCount">{{item.countTwo||0}}支</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="finish">
                        <div class="finishButton" @click="openDrawer()">
                            完成
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import {mapGetters} from 'vuex';

    export default {
        data() {
            return {
                cabineDatas: [],
                index: '1',
                row: 5,
                addVaccineOne: '',
                addVaccineTwo: '',
                vaccineOneCount: "",
                vaccineTwoCount: "",
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device'
            })
        },
        components:{},
        methods: {
            async queryDrawerByCondition(){
                this.cabineDatas = [];
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    device: this.device._id
                });
                let array = res.data;
                for (let i = 0; i < 10; i++) {
                    let num = array[i].vaccine.length, vaccine = array[i].vaccine, temp = {};
                    temp.drawerId = array[i]._id;
                    temp.x = array[i].x;
                    temp.y = array[i].y;
                    if (num > 0) {
                        for (let k = 0; k < num; k++) {
                            temp.single = true;
                            if (k == 0) {
                                temp.nameOne = vaccine[k].name;
                                temp.countOne = vaccine[k].surplus;
                                temp.idOne = vaccine[k]._id;
                                temp.codeOne = vaccine[k].code;
                            }
                            if (k == 1) {
                                temp.nameTwo = vaccine[k].name;
                                temp.countTwo = vaccine[k].surplus;
                                temp.idTwo = vaccine[k]._id;
                                temp.codeTwo = vaccine[k].code;
                            }
                        }
                        temp.x=array[i].x;
                        temp.y=array[i].y;
                    } else {
                        temp.nameOne = '';
                        temp.countOne = '';
                        temp.nameTwo = '';
                        temp.countTwo = '';
                        temp.x=array[i].x;
                        temp.y=array[i].y;
                    }
                    this.cabineDatas.push(temp);
                }
            },
            back: function(){
                this.$router.push('/main');
            },
            //打开指定抽屉
            openDrawer(){
                //获取选中的的抽屉
                //打开柜子的参数  num
                let ids = [], position = '', array = this.cabineDatas;
                for(let n=0;n<array.length;n++) {
                    if(array[n].single){
                        ids.push(array[n].drawerId);
                        position+=','+array[n].x+'#'+array[n].y
                    }
                }
                //position不为空时，调用Android接口，打开抽屉
                console.log("openDrawer===================>")
                if(position!=''){
                    console.log("openDrawer2=====================>")
                    this.$device.openDrawer({num:position.slice(1)}).then(res=>{
                        console.log('res---------> result:' + JSON.stringify(res))
                        console.log('33--------->%j' + JSON.stringify(ids))
                        this.$router.push({ path: '/inout/scanTip', query: { openDrawerIds: ids} });
                    }).catch(err=>{
                        console.log('shif: '+JSON.stringify(err));
                    });
                }
            }
        },
        mounted() {
            this.queryDrawerByCondition();
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/outStock.less";
</style>
<style>
    .ivu-input{
        height: 2.5rem;
    }
</style>
