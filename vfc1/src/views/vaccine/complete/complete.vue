<template>
    <div>
        <div>
            <nav-header
                :active="1" :comeback="1">
                <div class="complete-navtitle">
                    <img :src="`/static/img/images/complete.png`"/>
                    <div>疫苗入库完成</div>
                </div>
            </nav-header>
        </div>
        <div class="comebackHome">
            <div @click="routerto('home')">返回主页</div>
        </div>
        <div class="card">
            <scan-table order="1"></scan-table>
        </div>
        <Row class="datetime">
            <datetime>
                <div class="datetime-slot">
                    <div>入库时间：{{now}}</div>
                </div>
            </datetime>
        </Row>
    </div>
</template>
<script>
import navHeader from '_c/main/NavHeader';
import datetime from '_c/datetime';
import scanTable from '_c/main/scanTable';
// import scan from '_a/scan.png';
// import complete from '_a/complete.png';
import moment from 'moment'
export default {
    name:'complete',
    data(){
        return{
            // scan,
            // complete,
            now
        }
    },
    created(){
        this.getTime()
    },
    methods:{
        getTime(){
            this.now=moment().format('YYYY-MM-DD HH:mm:ss')
        },
        routerto(link){
            this.$router.push({name:link})
        },
    },
    components:{
        navHeader,
        datetime,
        scanTable,
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
.complete-navtitle{
    display: flex;
}
.complete-navtitle div{
    margin-left: 27px;
}
.datetime{
    position: absolute;
    bottom: 0;
    height: 60px;
    width: 100%;
}
.datetime-slot{
    display: flex;
    color: #3E4955;
    font-family:Microsoft YaHei;
    font-weight:bold;
}
.datetime-slot div{
  margin-left: 13px;
}
.comebackHome{
    position: relative;
}
.comebackHome div{
    position: absolute;
    right: 13px;
    top: -71px;
    background: url('/static/img/images/backHome.png') no-repeat left;
    background-size: 140px 60px;
    width: 140px;
    height: 60px;
    line-height: 60px;
    padding-left: 16px;
    font-size:16px;
    font-family:Microsoft YaHei;
    font-weight:bold;
    color: @blue;
    cursor: pointer;
}
</style>
