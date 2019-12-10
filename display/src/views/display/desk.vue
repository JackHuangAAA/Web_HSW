<template>
    <div class="layout">
        <div class="title"><span class="titleSpan">请<span style="color:#FF9600;">{{nextVaccination?nextVaccination.sort:''}}</span>号 <span style="color:#FF9600;margin-left:1.7rem;margin-right:1.7rem;">{{nextVaccination?nextVaccination.name:''}}</span> 接种</span></div>
        <div class="vaccineInf">
            <p class="vaccineInfTitle">{{nextVaccination?nextVaccination.vaccine.name:''}}</p>
            <div class="information">
                生产厂家：<span>{{nextVaccination?nextVaccination.vaccine.product:''}}</span>
            </div>
            <div class="code">
                批次号：<span>{{nextVaccination?nextVaccination.vaccine.batchNo:''}}</span>
            </div>
            <div class="date">
                有效期：<span>2019-09-18 12:30 至 {{nextVaccination?nextVaccination.vaccine.date:''}}</span>
            </div>
        </div>
    </div>
</template>
<script>
import io from  'socket.io-client';

export default {
    data () {
        return {
            socket: io.connect("/"),
            nextVaccination: {
                sort:1,
                name:"王晓华",
                vaccine:{code: 'ym991',name: '卡介苗', batchNo:'B-998',supervisionCode: 'FT20190920001',date: "2020-06-05 12:00", product: '上海生物',dosage:'0.5ml/支',cost:'0.00'}
            }
        }    
    },
    methods: {
        registerSocket(){
            this.socket.emit("register", JSON.stringify({code:'DK0001'}));
        },
        freshDatas(){
            this.socket.on('NextVaccination', data => {
                this.nextVaccination = data.data;
            });
        }
    },
    mounted(){
        //建立socket连接
        this.registerSocket();
        //监听事件，刷新数据
        this.freshDatas();
        __app.$emit("setTitle",{title:"1号接种台",deviceId:"DK0001"})
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/desk.less';
</style>