<template>
    <div id="app" class="container">
        <router-view></router-view>
    </div>
</template>
<style lang="less">
    // @import "style/vars";
    html,body{
        height: 100%;
    }
    #app{
        width: 100%;
        height: 100%;
        margin: 0;
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