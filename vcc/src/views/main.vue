<template>
    <div class="container">
        <div class="vaccineStatus">
            <div class="vaccineStatusTitle">
                <span class="vaccineStatusTitleTip">疫苗库存状态</span>
                <div class="redBlock"></div>
                <p class="bj">报警</p>
                <div class="yellowBlock"></div>
                <p class="yj">预警</p>
            </div>
            <div class="vaccineContent">
                <div v-for="(item,index) in vaccineData" class="vaccineStatusShow" 
                v-bind:class='{warning:item.vaccineOneCount == 0 || item.vaccineTwoCount == 0,tips:item.vaccineOneCount <10 || item.vaccineTwoCount < 10}'> 
                        <div class="vaccineLeft">
                            <p class="vaccineOneName">{{item.vaccineOneName}}</p>
                            <p class="vaccineOneCount">{{item.vaccineOneCount || 0}}支</p>
                        </div>
                        <div class="vaccineRight">
                            <p class="vaccineTwoName">{{item.vaccineTwoName}}</p>
                            <p class="vaccineTwoCount">{{item.vaccineTwoCount || 0}}支</p>
                        </div>
                </div>
            </div>
        </div>
        <div class="cabineStatus">
            <div class="temperature">
                <img src="/static/img/temperature.png">
                <p class="temP">11</p>
                <p class="temStatus">正常</p>
                <p class="roomTem">室温： 26℃</p>
                <p class="tem1">{{temperature-3}}</p>
                <p class="tem2">{{temperature-2}}</p>
                <p class="tem3">{{temperature-1}}</p>
                <p class="tem4">{{temperature}}</p>
                <p class="tem5">{{temperature+1}}</p>
                <p class="tem6">{{temperature+2}}</p>
                <p class="tem7">{{temperature+3}}</p>
            </div>
            <div class="abnormalTemperature">
                <p class="cardOne"><span style="font-size:2.25rem;margin-right:5px;">3</span>/次</p>
                <p class="cardTwo">温度异常</p>
                <img class="cardImg" src="/static/img/warning.png">
            </div>
            <div class="peopleCount">
                <p class="cardOne"><span style="font-size:2.25rem;margin-right:5px;">20</span>/人</p>
                <p class="cardTwo">接种顾客</p>
                <img class="cardImg" src="/static/img/customer.png">
            </div>
            <div class="addButton">
                疫苗入库
            </div>
            <div class="outButton">
                疫苗出库
            </div>
        </div>
    </div>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex'
    import enumTool from  '@/enum'
    import moment from 'moment'
    import io from  'socket.io-client'

    export default {
        data() {
            return {
                temperature: -12,
                vaccineData:[{vaccineOneName:'狂犬疫苗',vaccineTwoName:'百白破疫苗',vaccineOneCount:'100',vaccineTwoCount:'100'},
                {vaccineOneName:'狂犬疫苗',vaccineTwoName:'百白破疫苗',vaccineOneCount:'100',vaccineTwoCount:'100'},
                {vaccineOneName:'狂犬疫苗',vaccineTwoName:'百白破疫苗',vaccineOneCount:'100',vaccineTwoCount:'0'},
                {vaccineOneName:'狂犬疫苗',vaccineTwoName:'百白破疫苗',vaccineOneCount:'5',vaccineTwoCount:'100'}
                ]
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                currentMenu: 'currentMenu',
            })
        },
        components:{},
        methods: {
            ...mapActions({
                saveUser: 'saveUser',
                setCurrentMenu: 'setCurrentMenu'
            })

        },
        mounted() {

        }
    }
</script>

<style lang="less" scoped>
    @import "~@/style/main.less";
</style>
