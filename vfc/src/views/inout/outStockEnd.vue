<template>
    <div style="position:relative">
        <div class="container">
            <div class="inStockTitle">
                <img src="/static/img/gohome.png" class="back" @click="returnMain()">
                <img src="/static/img/succeed.png" class="succeed">
                <p class="headP">疫苗出库完成</p>
                <!-- <p class="backHomeP" @click="returnMain()">返回主页</p> -->
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
                    <div class="code2">
                        有效期
                    </div>
                    <div class="coordinate">
                        是否失效
                    </div>
                    <div class="count">
                        出库数量
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
                        <div class="code2">
                            {{item.expiry}}
                        </div>
                        <div class="coordinate">
                            {{item.invalid=='异常'?'异常':'正常'}}
                        </div>
                        <div class="count">
                            <p class="countInput">{{item.count}}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <audio src="/static/audio/outStockEnd.mp3" autoplay></audio>
    </div>
</template>
<script>
    import {mapGetters} from 'vuex'
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
    @import "~@/style/outStockEnd.less";
</style>
