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
                        <div class="vaccineBox">
                            <div class="vaccineBox-list" v-for="(item,i) of item.vaccine">
                                <div class="vaccineBox-list-title">{{item.name}}</div>
                                <div class="vaccineBox-list-del" @click="delVaccine(index,i)">
                                    <img src="/static/img/del.png">
                                </div>                                        
                            </div>
                            <Select class="chooseVaccine" :label-in-value="true" @on-change="select(index,$event)" v-if="item.vaccine.length<4">
                                <Option v-for="item in kindList" :value="item.code" :key="item.code">{{ item.name }}</Option>
                            </Select>
                            <img src="/static/img/add.png" class="boxAdd" @click="addVaccine(index)" v-if="item.vaccine.length<4">
                        </div>
                    </div> 
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
                cabineData: [],
                kindList: [],
                vaccineOneName:'',
                vaccineTwoName:''
            };
        },
        computed: {
            ...mapGetters({
                device: "device"
            })
        },
        methods: {
            async addVaccine(index){
                console.log("1111111111111111111111")
                let drawer = this.cabineData[index];
                if(drawer.name=="" || drawer.code==""){
                    return false;
                }
                console.log("222222222222222222222222222")
                if(this.cabineData[index].vaccine !== "" && this.cabineData[index].vaccine !== undefined){
                    let temp = this.cabineData[index].vaccine;
                    for(let i=0;i<temp.length;i++){
                        if(temp[i].code == this.cabineData[index].code){
                            this.$Message.error({
                                content: '同一个抽屉不允许设置相同的疫苗',
                                duration: 10,
                                closable: true
                            });
                            return false;
                        } 
                    }
                }
                console.log("33333333333333333333333333")
                await this.$api.post("/drawer/modifyDrawerById", {
                    id: drawer._id,
                    vaccine: {
                        device: this.device._id,   //设备
                        code: drawer.code,   //疫苗编号
                        name: drawer.name  //疫苗名称
                    }
                });
                console.log("4444444444444444444444444444")
                this.queryDrawerByCondition();
            },
            async delVaccine(index,i){
                let drawer = this.cabineData[index];
                if(drawer.vaccine[i].surplus>0){
                    this.$Message.info({
                        top: 300,
                        content: '该疫苗数量大于0，不允许删除',
                        duration: 10,
                        closable: true
                    });
                    return false;
                }
                //删除疫苗信息从DB
                let res = await this.$api.post("/drawer/modifyDrawerByIdDec", {
                        id: drawer._id,
                        vaccineId:  drawer.vaccine[i]._id//疫苗id
                    }
                );
                this.queryDrawerByCondition();
            },
            select(index, event){
                this.cabineData[index].name=event.label;
                this.cabineData[index].code=event.value;
            },
            async queryVaccineKinds(){
                let res = await this.$api.get("/zcy/queryVaccineKinds");
                this.kindList = res.data;
            },
            async queryDrawerByCondition(){
                this.cabineData = [];
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    device: this.device._id
                });
                let data=res.data;
                for(let i=0;i<data.length;i++){
                    this.$set(data[i],'code','');
                    this.$set(data[i],'name','');
                }
                this.cabineData=data
                this.row=this.cabineData.length/2
            }
        },
        mounted() {
            this.queryVaccineKinds();
            this.queryDrawerByCondition();
            console.log(this.cabineData)
        },
    };
</script>

<style lang="less" scoped>
    @import "~@/style/region.less";
</style>