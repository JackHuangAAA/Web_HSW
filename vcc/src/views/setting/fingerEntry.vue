<template>
    <div class="fingerAdd">
        <div class="finger-save" @click="savehandle()">保存</div>
        <div class="fingerAdd-conent">
            <div class="fingerAdd-title">放置手指</div>
            <div class="fingerAdd-info">将手指放置在指纹仪上，识别完成后移开，并重复此步骤</div>
            <img src="/static/img/fingerEntry.png" alt="">
        </div>
        <div class="fingerAdd-footer">
            <div v-if="!noticeState" class="fingerAdd-notice">提示信息</div>
            <div v-if="noticeState" class="fingerAdd-succeed">
                <img src="/static/img/succeed.png" alt=""/>
                <div>指纹录入成功，请保存</div>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    data(){
        return{
            noticeState:false,//提示信息状态
        }
    },
    methods:{
        savehandle(){
            this.$emit('save',true)
        },
    },
    mounted(){
        this.$device.subscribe('FINGER_MESSAGE', (data) => {
            console.log(data)
        });
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
@import '~@/style/fingerEntry.less';
</style>