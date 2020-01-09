<!--扫描提示-->
<template>
    <div class="scanTip">
        <Row class="scanTip-header">
            <div class="scanTip-header-title">请将抽屉中疫苗全部取出</div>
            <div class="scanTip-step">
                <div class="Steps"> 
                    <div class="Steps-item"
                        v-for="(list,index) in lists">
                        <div style="position:relative">
                            <div class="step-line" :class="{'step-line-active':index<=active-1}" v-if="index<lists.length-1"></div>
                        </div> 
                        <div class="Steps-index"
                            :class="{'Steps-index-active':index<=active}">{{index+1}}</div>
                        <div class="Steps-step"
                            :class="{'Steps-active':index<=active}">{{list.title}}</div>
                    </div>
                </div>
            </div>
        </Row>
        <div class="scanTip-main">
            <div class="scanTip-main-notice">
                <img src="/static/img/succeed.png" alt="">
                <div>抽屉锁已打开，请将疫苗全部取出</div>
            </div>
            <div class="scanTip-main-button" @click="finishOutStock">完成</div>
            <div class="scanTip-main-message">疫苗取出完毕请点击完成按钮</div>
        </div>
        
    </div>
</template>
<script>
    import {mapGetters} from 'vuex'
    import uuid from 'uuid/v1';

    export default {
        data(){
            return{
                commonData: null,
                lists:[
                    { title: '选择出库疫苗' },
                    { title:'取出疫苗' },
                    { title: '出库完成' }
                ],
                active:1,
                openDawerIds:[]
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device'
            })
        },
        methods: {
            //查询指定抽屉id的抽屉疫苗数据
            async queryDrawerByCondition(){
                let res = await this.$api.get("/drawer/queryDrawerByCondition", {
                    device: this.device._id,
                    ids: this.openDawerIds
                });
                return res.data;
            },
            async saveManyInout(params){
                await this.$api.post("/inout/saveManyInout", params);
            },
            async finishOutStock(){
                let datas = await this.queryDrawerByCondition();
                //准备出库记录数据
                let vaccineIds = [], result = [], batchId = uuid();//batchId为一次流水id(1次流水可能有多条入库记录)
                for(let i=0;i<datas.length;i++){
                    let vaccine = datas[i].vaccine;
                    for(let k=0;k<vaccine.length;k++){
                        let temp = {}; //关键
                        temp.batchId = batchId;
                        temp = _.assign(temp, this.commonData); //关键
                        temp.x = datas[i].x;
                        temp.y = datas[i].y;
                        temp.code = vaccine[k].code;
                        temp.name = vaccine[k].name;
                        temp.total = vaccine[k].total;
                        temp.surplus = vaccine[k].surplus;
                        vaccineIds.push(vaccine[k]._id);
                        result.push(temp);
                    }
                }
                //增加出库记录
                await this.saveManyInout(result);
                this.$router.push({ path: '/inout/detail', query: { action: 'out',ids: vaccineIds} });
            }
        },
        mounted() {
            this.openDawerIds = this.$route.query.openDrawerIds;
            this.commonData = {
                type: 2, //2:出库
                user: this.user._id,
                device: this.device._id,
                deviceType: 1, //1:接种柜
                unitCode: this.device.unitCode,
                unitName: this.device.unitName
            };
        }
    }
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
@import '~@/style/scanTip.less';
</style>