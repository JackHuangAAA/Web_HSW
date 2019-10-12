<template>
    <div>
        <div>
            <nav-header :lists="indexlist"
                :active="1" :comeback="1">
                <div class="complete-navtitle">
                    <img :src="complete"/>
                    <div>疫苗出库完成</div>
                </div>
            </nav-header>
        </div>
        <div class="card">
            <scan-table order="2"></scan-table>
        </div>
        <Row>
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
import scan from '_a/scan.png';
import complete from '_a/complete.png';
import moment from 'moment'
export default {
    name:'outcomplete',
    data(){
        return{
            scan,
            complete,
            now
        }
    },
    created(){
        this.getTime()
    },
    methods:{
        getTime(){
            this.now=moment().format('YYYY-MM-DD HH:mm:ss')
        }
    },
    components:{
        navHeader,
        datetime,
        scanTable,
    }
}
</script>
<style lang="less" scoped>
.complete-navtitle{
    display: flex;
}
.complete-navtitle div{
    margin-left: 27px;
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
</style>
