<template>
    <div class="fingerAdd">
        <div class="finger-save" @click="savehandle()">保存</div>
        <div class="fingerAdd-conent">
            <div class="fingerAdd-title">放置手指</div>
            <div class="fingerAdd-info">将手指放置在指纹仪上，识别完成后移开，并重复此步骤</div>
            <img src="/static/img/fingerEntry.png" alt="">
        </div>
        <div class="fingerAdd-footer">
            <div v-if="!noticeState" class="fingerAdd-notice">{{notice}}</div>
            <div v-if="noticeState" class="fingerAdd-succeed">
                <img src="/static/img/succeed.png" alt=""/>
                <div>指纹录入成功，请保存</div>
            </div>
        </div>
    </div>
</template>
<script>
import {mapGetters} from 'vuex'
export default {
    data(){
        return{
            noticeState:false,//提示信息状态
            notice:''
        }
    },
    computed:{
        ...mapGetters({
                user: 'user',
            }),
    },
    methods:{
        
        savehandle(){
            this.register()
            // this.$emit('save',true)
        },
        // 指纹录入
        register(){
            this.$device.fingerRegister({userId:this.user._id}).then(res => {
                console.log('register1----'+JSON.stringify(res));
                console.log('register2----'+JSON.stringify(res));
            })
        },
    },
    mounted(){
        // 设备反馈监听
        this.$device.subscribe('FINGER_MESSAGE', (data) => {
            console.log("------------------------"+JSON.stringify(data))
            this.notice=data.msg
            console.log("--------------------------------------"+this.notice)
            if(data.type==2){//type=1持续录入2完成
                this.noticeState=true
                let to=setTimeout(()=>{
                    this.$emit('save',true)
                    clearTimeout(to)
                },1500)
            }else{
                this.noticeState=false
            }
        });
        // 执行指纹录入方法
        this.register()
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
@import '~@/style/fingerEntry.less';
</style>