<!--区域划分-->
<template>
    <div>
        <Row class="bs-row" type="flex" :gutter="8" align="middle">
            <Col span="4" justify="end">
            <p class="bs-title">
                所在区域1111
            </p>
            </Col></Row>

    </div>
</template>

<script>
    import { mapGetters } from "vuex";
    export default {
        data() {
            return {

            };
        },
        computed: {
            ...mapGetters({
                device: "device"
            })
        },
        methods: {
            async getVaccint(){
                let vaccine = await this.$api.get("/vaccine/queryVaccine", {
                    device: this.device._id
                });
            }
        },
        mounted() {

        },
    };
</script>

<style lang="less">
    @import "~@/style/region.less";
</style>