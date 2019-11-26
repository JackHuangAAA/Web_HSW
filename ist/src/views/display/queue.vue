<template>
    <div class="layout">
        <div :class="{'inf':index == 0,'infCut': index%2==1,'infCutTwo':index%2 ==0}" v-for="(item,index) in queue" :key="item.sort">
            <div class="code">
                {{item.sort}}
            </div>
            <div class="name">
                {{item.name}}
            </div>
            <div class="position">
                请到1号接种台
            </div>
            <div class="call">
                {{item.call}}
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
            queue : []
        }    
    },
    methods: {
        registerSocket(){
            this.socket.emit("register", JSON.stringify({code:'IST0001Q'}));
        },
        freshDatas(){
            this.socket.on('UpdateQueueStatus', data => {
                this.queryQueue();
                this.queue[0].call="正在呼叫";
            });
        },
        async queryQueue(){
            let queue = await this.$api.get('/queue/queryQueueByCondition',{status:1});
            this.queue = queue.data;
            for(let i =0;i<this.queue.length;i++){
                this.queue[i].call="等待叫号";
            }
            console.log(this.queue)
        }
    },

    mounted(){
        //建立socket连接
        this.registerSocket();
        //监听事件，刷新数据，首位改为呼叫
        this.freshDatas();
        //查询队列中待接种数据,全部为等待
        this.queryQueue();
        __app.$emit("setTitle",{title:"叫号综合显示屏",deviceId:"CN0001"})

    }
}
</script>
<style lang="less" scoped>
@import '~@/style/callNumber.less';
</style>