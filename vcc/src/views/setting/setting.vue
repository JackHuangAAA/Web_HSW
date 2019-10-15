<!--设置页面-->
<template>
    <Row>
        <Col span="24">
            <Tabs :animated="false">
               <TabPane label="设置">
                    <basic></basic>
                </TabPane>
                <TabPane label="指纹录入">
                    <finger></finger>
                </TabPane>
                <TabPane label="疫苗区域划分">
                    <region></region>
                </TabPane>
            </Tabs>
        </Col>
    </Row>
</template>

<script>
    import { mapGetters } from "vuex";
    import basic from "./basic";
    import finger from "./finger";
    import region from "./region";
    export default {
        components: {
            basic,
            finger,
            region
        },
        data() {
            return {};
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device'
            })
        },
        methods: {
        },
        mounted() {

        }
    };
</script>
<style lang="less">
    @import '~@/style/color.less';
    @import "~@/style/setting.less";
</style>