<template>
    <div class="layout">
        <div :class="{'inf':index == 0,'infCut': index%2==1,'infCutTwo':index%2 ==0}" v-for="(item,index) in datas" :key="item.code">
            <div class="code">
                {{item.code}}
            </div>
            <div class="name">
                {{item.name}}
            </div>
            <div class="position">
                {{item.position}}
            </div>
            <!-- <div class="call">
                {{item.call}}
            </div> -->
        </div>
    </div>
</template>
<script>
import io from  'socket.io-client';

export default {
    data () {
        return {
            socket: io.connect("/"),
            datas:[]
        }    
    },
    methods: {
        registerSocket(){
            this.socket.emit("register", JSON.stringify({code:'queue_1'}));
        },
        freshDatas(){
            this.socket.on('test', data => {
                console.log('tttt----'+data);
                this.queryQueue();
            });
        },
        queryQueue(){
            this.datas = [{code: '0089',name:'黄江华',position:'请到1号接种台',call:'正在呼叫'},
                {code: '0090',name:'李梦琪',position:'等待接种',call:'等待叫号'}];
        }
    },

    mounted(){
        //建立socket连接
        this.registerSocket();
        //监听事件，刷新数据
        this.freshDatas();
        //查询队列中待接种数据
        //this.queryQueue();
        __app.$emit("setTitle",{title:"叫号显示屏",deviceId:"CN0001"})
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/callNumber.less';
</style>