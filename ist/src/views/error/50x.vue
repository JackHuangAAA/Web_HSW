
<template>
    <div :style="{height: h}">
        <div class="tip">
            <div class="info">
                <div class="title">对不起，出错了！</div>
                <p>{{message}}</p>
            </div>
            <img src="/static/img/50x.png">
        </div>
    </div>
</template>
<style lang="less" scoped>
    .tip{
        padding-top: 140px;
        text-align: center;
        .info{
            display: inline-block;
            vertical-align: top;
            margin-left: 60px;
            margin-top: 60px;
            text-align: left;
            .title{
                font-size: 24px;
                font-weight: bold;
                color: #4c81e4;
                margin-bottom: 20px;
            }
            p{
                font-size: 20px;
                color: #f3b927;
            }
        }
    }
</style>
<script>
    export default{
        data(){
            return{
                message: null,
            }
        },
        computed:{
            h(){
                return  document.documentElement.clientHeight - 106 + 'px';
            }
        },
        mounted(){
            if(this.$route.query) {
                this.message = this.$route.query.message;
            }
        }
    }
</script>
