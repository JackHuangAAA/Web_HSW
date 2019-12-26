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
            queue : []
        }    
    },
    methods: {
        registerSocket(){
            this.socket.emit("register", JSON.stringify({code:'CN0001'}));
        },
        freshDatas(){
            this.socket.on('UpdateQueueStatus', data => {
                console.log("UpdateQueueStatus result"+JSON.stringify(data));
                this.queryQueue().then(()=>{

                });
                if(this.queue.length>0){
                    this.$device.audio({audio_text:`${this.queue[0].sort}号${this.queue[0].name}${this.queue[0].position}`});
                }
            });
            this.socket.on("NextVaccination",data=>{
                console.log("NextVaccination result "+JSON.stringify(data));
                if(this.queue.length>0){
                    this.$device.audio({audio_text:`${this.queue[0].sort}号${this.queue[0].name}${this.queue[0].position}`});
                }
            })
        },
        async queryQueue(){
            let queue = await this.$api.get('/queue/queryQueueByCondition',{status:1});
            this.queue = queue.data;
            //console.log('res======'+JSON.stringify(queue));
            for(let i =0;i<this.queue.length;i++){
                this.$set(this.queue[i],'position',"等待接种");
                let len=String(this.queue[i].sort).length;
                switch(len){
                    case 1:
                        this.$set(this.queue[i],'sort',"000"+this.queue[i].sort);
                        break;
                    case 2:
                        this.$set(this.queue[i],'sort',"00"+this.queue[i].sort);
                        break;
                    case 3:
                        this.$set(this.queue[i],'sort',"0"+this.queue[i].sort);
                        break;
                }
            }
            this.$set(this.queue[0],'position',"请到1号接种台");
            //console.log(this.queue)
        }
    },

    mounted(){
        //建立socket连接
        this.registerSocket();
        //监听事件，刷新数据，首位改为呼叫
        this.freshDatas();
        //查询队列中待接种数据,全部为等待
        this.queryQueue().then(()=>{

        });
        __app.$emit("setTitle",{title:"叫号显示屏",deviceId:"CN0001"})

    }
}
</script>
<style lang="less" scoped>
@import '~@/style/callNumber.less';
</style>