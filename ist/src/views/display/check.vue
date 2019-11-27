<template>
    <div class="layout">
        <div class="left">
            <div class="personTitle">
                <div class="phone">
                    号码
                </div>
                <div class="name">
                    姓名
                </div>
                <div class="time" style="color: #3E4955">
                    剩余时间
                </div>
            </div>
            <div class="personInfContent">
                <div :class="{personInf:index%2==0,personInfHeight:index%2==1}" v-for="(item,index) in queue">
                    <div class="phone">
                        {{item.code}}
                    </div>
                    <div class="name">
                        {{item.name}}
                    </div>
                    <div class="time">
                        {{item.time>=30?'已完成':30-item.time+'分钟'}}
                    </div>
                </div>
            </div>
        </div>
        <div class="right">
            <img src="/static/img/check.png">
        </div>
    </div>
</template>
<script>
import io from  'socket.io-client';
import moment from 'moment';
export default {
    data () {
        return {
            socket: io.connect("/"),
            queue: [],
            timer:null
        }    
    },
    methods: {
        registerSocket(){
            this.socket.emit("register", JSON.stringify({code:'IST0001C'}));
        },
        freshDatas(){
            this.socket.on('VaccinationCheck', data => {
                console.log(data);
                this.queryQueue();
            });
        },
        async queryQueue(){
            let time_now = new Date();
            let time_30 = time_now.getTime()-31*60*1000;
            time_30 = new Date(time_30);
            let queue = await this.$api.get('/queue/queryQueueByCondition',{status:0,finishDate:time_30});
            this.queue = queue.data;
            this.time();
        },
        time(){
            for(let i =0;i<this.queue.length;i++){
                let timeFinish =moment(this.queue[i].finishDate).diff(moment()._d);
                let time=Math.abs(Math.floor(timeFinish/60000));
                this.$set(this.queue[i],'time',time);
                if(this.queue[i].time>=31){
                    this.queue.shift(i);
                }
                // this.queue[i].time=30-parseInt((time_now.getTime()-timeFinish.getTime())/(60*1000));
            }
        }
    },
    mounted(){
        //建立socket连接
        this.registerSocket();
        //监听事件，刷新数据

        this.freshDatas();
        //查询队列中待接种数据
        this.queryQueue();
        this.timer=setInterval(()=>{
            this.time();
        },60000)
        //this.queryQueue();
        __app.$emit("setTitle",{title:"留观显示屏",deviceId:"CK0001"})

    }
}
</script>
<style lang="less" scoped>
@import '~@/style/check.less';
</style>