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
                <div :class="{personInf:index%2==0,personInfHeight:index%2==1}" v-for="(item,index) in personInf">
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
            <img src="/static/img/check.png">
        </div>
    </div>
</template>
<script>
export default {
    data () {
        return {
            personInf: [{code:'0089',name:'黄江华',time:'30'},
                        {code:'0089',name:'李梦琪',time:'25'},
                        {code:'0089',name:'王小明',time:'18'},
                        {code:'0089',name:'王小明',time:'18'},
                        {code:'0089',name:'王小明',time:'18'},{code:'0089',name:'王小明',time:'18'},
                        {code:'0089',name:'王小明',time:'18'},
                        {code:'0089',name:'王小明',time:'18'},
                        {code:'0089',name:'王小明',time:'18'}]
        }    
    },
    mounted(){

    }
}
</script>
<style lang="less" scoped>
@import '~@/style/check.less';
</style>