<template>
    <div class="finger">
        <div class="finger-save">保存</div>
        <div class="finger-title">放置手指</div>
        <div class="finger-message">将手指放置在指纹仪上，识别完成后移开，并重复此步骤</div>
    </div>
</template>
<script>
export default {
    name:'fingerInput',
    data(){
        return{

        }
    }
}
</script>
<style lang="less" scoped>
.fingerprint{
  background: #fff;
  position: relative;
  padding: 0px 33px 0px 31px;
  color: #3E4955;
  font-weight: bold;
  font-size: 16px;
}
</style>