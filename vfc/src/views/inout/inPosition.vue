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
    import {mapGetters} from 'vuex'
    export default {
        data() {
            return {
                cabineDatas: [],
                index: '1',
                row: 6,
                checkDrawerId:'',
                datas: null
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
            next(){
                this.$router.push({ path: '/inout/inStockEnd', query: { datas: this.datas }});
            }
        },
        mounted() {
            this.datas = this.$route.query.datas;
            this.queryDrawerByCondition();
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
