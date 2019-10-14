<template>
    <div id="app" class="container">
        <router-view></router-view>
    </div>
</template>
<style lang="less" scoped>
    @import "style/vars";
    
    #app{
        width: 100%;
        margin: 0;
        height: 100%;
        min-width: @viewport-width;
    }
</style>
<script>
    export default {
        mounted() {
        },
        beforeDestroy() {

        },
        methods: {
        }
    };
</script>