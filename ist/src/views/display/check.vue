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
                <div :class="{personInf:index%2==0,personInfHeight:index%2==1}" v-for="(item,index) in datas">
                    <div class="phone">
                        {{item.code}}
                    </div>
                    <div class="name">
                        {{item.name}}
                    </div>
                    <div class="time">
                        {{item.time}}分钟
                    </div>
                </div>
            </div>
        </div>
        <div class="right">
            <img src="static/img/check.png">
        </div>
    </div>
</template>
<script>
import io from  'socket.io-client';

export default {
    data () {
        return {
            socket: io.connect("/"),
            datas: []
        }    
    },
    methods: {
        registerSocket(){
            this.socket.emit("register", JSON.stringify({code:'queue_3'}));
        },
        freshDatas(){
            this.socket.on('test', data => {
                console.log('tttt----'+data);
                this.queryQueue();
            });
        },
        queryQueue(){

            this.datas = [{code:'0088',name:'黄江华',time:'30'},
                {code:'0089',name:'李梦琪',time:'25'}];
        }
    },
    mounted(){
        //建立socket连接
        this.registerSocket();
        //监听事件，刷新数据
        this.freshDatas();
        //查询队列中待接种数据
        //this.queryQueue();
        __app.$emit("setTitle","留观显示屏")
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/check.less';
</style>