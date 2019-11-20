<template>
    <div class="layout">
        <div :class="{'inf':index == 0,'infCut': index%2==1,'infCutTwo':index%2 ==0}" v-for="(item,index) in inf" :key="item.code">
            <div class="code">
                {{item.code}}
            </div>
            <div class="name">
                {{item.name}}
            </div>
            <div class="position">
                {{item.position}}
            </div>
            <div class="call">
                {{item.call}}
            </div>
        </div>
    </div>
</template>
<script>
export default {
    data () {
        return {
            inf:[{code: '0089',name:'黄江华',position:'请到1号接种台',call:'正在呼叫'},
                 {code: '0090',name:'李梦琪',position:'',call:'等待叫号'},
                 {code: '0091',name:'李梦琪',position:'',call:'等待叫号'},
                 {code: '0092',name:'李梦琪',position:'',call:'等待叫号'},
                 {code: '0093',name:'李梦琪',position:'',call:'等待叫号'},
                 {code: '0094',name:'李梦琪',position:'',call:'等待叫号'}]
        }    
    },
    mounted(){

    }
}
</script>
<style lang="less" scoped>
@import '~@/style/callNumber.less';
</style>