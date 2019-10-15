<!--扫描提示-->
<template>
    <div class="scanTip">
        <Row class="scanTip-header">
            <div class="scanTip-header-title">请将抽屉中疫苗全部取出</div>
            <div class="scanTip-step">
                <div class="Steps"> 
                    <div class="Steps-item"
                        v-for="(list,index) in lists">
                        <div class="Steps-index"
                            :class="{'Steps-index-active':index<=active}">{{index+1}}</div>
                        <div class="Steps-step"
                            :class="{'Steps-active':index<=active}">{{list.title}}</div>
                        <div style="position:relative">
                            <div class="step-line" :class="{'step-line-active':index<=active-1}" v-if="index<lists.length-1"></div>
                        </div>          
                    </div>
                </div>
            </div>
        </Row>
        <div class="scanTip-main">
            <div class="scanTip-main-notice">
                <img src="/static/img/succeed.png" alt="">
                <div>抽屉锁已打开，请将疫苗全部取出</div>
            </div>
            <div class="scanTip-main-button">完成</div>
            <div class="scanTip-main-message">疫苗取出完毕请点击完成按钮</div>
        </div>
    </div>
</template>
<script>
export default {
    data(){
        return{
            lists:[{ title: '选择出库疫苗' }, {title:'取出疫苗'},{ title: '出库完成' }],
            active:1
        }
    }    
}
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
@import '~@/style/scanTip.less';
</style>