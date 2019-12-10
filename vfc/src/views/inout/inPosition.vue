<!--入库对应位置展示-->
<template>
    <div style="position:relative">
        <div class="container">
            <div class="inStockTitle">
                <p class="headP">请将疫苗放入对应位置</p>
                <img src="/static/img/inCabinet2.png" class="vaccineIn">
            </div>
            <div class="main">
                <img src="/static/img/inPosition.png" class="mainImg">
                <p class="describe">请将疫苗放入冷藏柜中</p>
                <div class="yes" @click="next()">
                        入库完成
                    </div>
            </div>
        </div>
    </div>
</template>
<script>
    import {mapGetters} from 'vuex';
    import uuid from 'uuid/v1';

    export default {
        data() {
            return {
                datas: null,
                commonData: null,
                batchId:''
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
            async saveVaccine(){
                let res = await this.$api.post("/vaccine/saveManyVaccine", this.datas);
            },
            async saveManyInout(params){
                await this.$api.post("/inout/saveManyInout", params);
            },
            async next(){
                //增加入库记录
                let array = this.datas,result = [];
                for(let k=0;k<array.length;k++){
                    let temp = {}; //关键
                    temp.batchId = this.batchId;
                    temp = _.assign(temp, this.commonData); //关键
                    temp = _.assign(temp, array[k]);
                    result.push(temp);
                }
                console.log(result)
                if(!_.isEmpty(result)){
                    //完成数据入库
                    await this.saveVaccine();
                    await this.saveManyInout(result)
                    this.$router.push({ path: '/inout/inStockEnd', query: { datas: this.datas }});
                }
            }
        },
        mounted() {
            this.datas = this.$route.query.datas;
            this.batchId = uuid();
            this.commonData = {
                type: 1, //1:入库
                user: this.user._id,
                device: this.device._id,
                deviceType: 2, //2:冷藏柜
                unitCode: this.device.unitCode,
                unitName: this.device.unitName
            };
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/inPosition.less";
</style>
<style>
    .ivu-input{
        height: 2.5rem;
    }
</style>
