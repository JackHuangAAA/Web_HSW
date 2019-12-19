<template>
    <div class="layout" v-if="nextVaccination.sort">
        <div class="title"><span class="titleSpan">请<span style="color:#FF9600;">{{nextVaccination.sort?nextVaccination.sort:''}}</span>号 <span style="color:#FF9600;margin-left:1.7rem;margin-right:1.7rem;">{{nextVaccination.name?nextVaccination.name:''}}</span> 接种</span></div>
        <div class="vaccineInf">
            <p class="vaccineInfTitle">{{nextVaccination.vaccine?nextVaccination.vaccine.name:''}}</p>
            <div class="information">
                生产厂家：<span>{{nextVaccination.vaccine?nextVaccination.vaccine.product:''}}</span>
            </div>
            <div class="code">
                批次号：<span>{{nextVaccination.vaccine?nextVaccination.vaccine.batchNo:''}}</span>
            </div>
            <div class="date">
                有效期：<span>{{nextVaccination.vaccine?'2019-09-18 12:30 至 '+nextVaccination.vaccine.date:''}}</span>
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
            nextVaccination: {}
        }    
    },
    methods: {
        registerSocket(){
            this.socket.emit("register", JSON.stringify({code:'DK0001'}));
        },
        freshDatas(){
            this.socket.on('NextVaccination', data => {
                this.nextVaccination = data.data;
                let len=String(this.nextVaccination.sort).length;
                switch(len){
                    case 1:
                        this.$set(this.nextVaccination,'sort',"000"+this.nextVaccination.sort);
                        break;
                    case 2:
                        this.$set(this.nextVaccination,'sort',"00"+this.nextVaccination.sort);
                        break;
                    case 3:
                        this.$set(this.nextVaccination,'sort',"0"+this.nextVaccination.sort);
                        break;
                }
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