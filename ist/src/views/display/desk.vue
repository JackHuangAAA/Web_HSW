<template>
    <div class="layout">
        <div class="title"><span class="titleSpan">请<span style="color:#FF9600;">089</span>号 <span style="color:#FF9600;margin-left:1.7rem;margin-right:1.7rem;">黄江华</span> 接种</span></div>
        <div class="vaccineInf">
            <p class="vaccineInfTitle">脊髓灰质炎疫苗</p>
            <div class="information">
                生产厂家：<span>中科生物制药股份有限公司</span>
            </div>
            <div class="code">
                批次号：<span>HU5747908488</span>
            </div>
            <div class="date">
                有效期：<span>2019-09-18 12:30:28   至  2022-12-29 12:20:19</span>
            </div>
        </div>
    </div>
</template>
<script>
import io from  'socket.io-client';

export default {
    data () {
        return {
            socket: io.connect("http://localhost:9996"),
            vaccine: null
        }    
    },
    methods: {
        registerSocket(){
            this.socket.emit("register", JSON.stringify({code:'IST0001D'}));
        },
        freshDatas(){
            this.socket.on('test', data => {
                console.log('tttt----'+data);
                this.vaccine = '';
            });
        }
    },
    mounted(){
        //建立socket连接
        this.registerSocket();
        //监听事件，刷新数据
        this.freshDatas();
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/desk.less';
</style>