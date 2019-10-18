<template>
    <div style="position:relative">
        <div class="container">
            <div class="inStockTitle">
                    <img src="/static/img/succeed.png" class="succeed">
                    <p class="headP">疫苗入库完成</p>
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
                        位置信息
                    </div>
                    <div class="count">
                        入库数量
                    </div>
                </div>
                <div class="table">
                    <div v-for="(item,index) in tableDatas" @click="selectVaccine(index)" class="tableData" :class="{clicked: clickIndex==index}">
                        <div class="index">
                            <span v-if="index>8">{{index+1}}</span>
                            <span v-if="index<8 || index ==8">0{{index+1}}</span>
                        </div>
                        <div class="vaccineName">
                            {{item.vaccineName}}
                        </div>
                        <div class="code">
                            {{item.code}}
                        </div>
                        <div class="coordinate">
                            {{item.site}}
                        </div>
                        <div class="count">
                            <p class="countInput">11</p>
                        </div>
                    </div>
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
                tableDatas: [{vaccineName: '百白破疫苗',code:'yHUG-7U940',site: '(1,1)',count:0},{vaccineName:'狂犬疫苗',code:'yHUG-7U940',site: '(1,1)',count:0}],
                clickIndex: 0,
                ifTip: false
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
            back(){
                this.$router.push('/inout/inStock');
            },
            selectVaccine: function(index){
                this.clickIndex = index;
                this.ifTip = true;
            }
        },
        mounted() {

        }
    };
</script>
<style lang="less" scoped>
    @import "~@/style/inStockEnd.less";
</style>
