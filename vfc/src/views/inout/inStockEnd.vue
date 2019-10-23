<template>
    <div style="position:relative">
        <div class="container">
            <div class="inStockTitle">
                    <img src="/static/img/backHome.png" @click="returnMain()" class="back">
                    <img src="/static/img/succeed.png" class="succeed">
                    <p class="headP">疫苗入库完成</p>
                    <p class="backHomeP" @click="returnMain()">返回主页</p>
            </div>
            <div class="main">
                <div class="title">
                    <div class="index">
                        序号
                    </div>
                    <div class="vaccineName">
                        疫苗名称
                    </div>
                    <div class="code">
                        批次号
                    </div>
                    <div class="coordinate">
                        有效期
                    </div>
                    <div class="count">
                        入库数量
                    </div>
                </div>
                <div class="table">
                    <div v-for="(item,index) in tableDatas" class="tableData">
                        <div class="index">
                            <span v-if="index>8">{{index+1}}</span>
                            <span v-if="index<8 || index ==8">0{{index+1}}</span>
                        </div>
                        <div class="vaccineName">
                            {{item.name}}
                        </div>
                        <div class="code">
                            {{item.batchNo}}
                        </div>
                        <div class="coordinate">
                            {{dateformat(item.expiry)}}
                        </div>
                        <div class="count">
                            <p class="countInput">{{item.count}}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import {mapGetters} from 'vuex';
    import moment from 'moment';

    export default {
        data() {
            return {
                tableDatas: []
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device',
            })
        },
        components:{},
        methods: {
            dateformat(val){
                return moment(val).format('YYYY-MM-DD HH:mm:ss');
            },
            returnMain: function(){
                this.$router.push('/main');
            }
        },
        mounted() {
            this.tableDatas = this.$route.query.datas;
        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/inStockEnd.less";
</style>
