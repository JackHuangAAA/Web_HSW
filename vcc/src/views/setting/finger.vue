<!--指纹管理-->
<template>
    <div class="fingerprint card">
        <div class="fingerprint-add" v-if="this.user.finger.length<2" @click="addFinger()">新增</div><!---->
        <div class="fingerprint-clear">清除所有</div>
        <Table :columns="cols" :data="datas" size="small" :highlight-row="false" :disabled-hover='false'></Table>
        <div align="center">
            <!-- <Button type="primary" @click="register">register</Button> -->
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <Button type="dashed" @click="verify">verify</Button>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <Button type="text" @click="delAll">delAll</Button>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <Button type="success" @click="totalAll">totalAll</Button>
        </div>
    </div>
</template>

<script>
    import { mapGetters } from "vuex";
    export default {
        data() {
            return {
                datas: [],
                cols: [
                {
                    type: 'index',
                    width: 150
                },{
                    title: '指纹CODE',
                    key: 'finger'
                },{
                    title: '操作',
                    width: 145,
                    render: (h, params) => {
                         return h('div', [
                                h('Button', {
                                    props: {
                                        size: 'small'
                                    },
                                    style: {
                                        border: '1px solid #3399ff',
                                        'font-size': '14px',
                                        'margin-right':'10px'
                                    },
                                    on: {
                                        click: () => {
                                            this.delFinger(params.row);
                                        }
                                    }
                                },'删除')
                         ]);
                    }
                    }],
                data: []
            };
        },
        computed: {
            ...mapGetters({
                user: "user",
                device: "device"
            })
        },
        methods: {
            addFinger(){
                this.$emit('add',false)
            },
            getFingerInfo() {
                let data = [];
                for (let f of this.user.finger) {
                    let t = {};
                    t.finger = f;
                    data.push(t);
                }
                this.datas = data
            },
            delFinger(row){
                this.$device.fingerRegister('DEL_ONE_TEMPLATE').then(res => {
                    console.log('delFinger1----'+JSON.stringify(res));
                    console.log('delFinger2----'+JSON.stringify(res));
                })
            },
            
            verify(){
                console.log('verify000000000000----');
                this.$device.fingerVerify('VERIFY').then(res => {
                    console.log('verify1----'+JSON.stringify(res));
                    console.log('verify2----'+JSON.stringify(res));
                })
            },
            delAll(){
                console.log('delAll000000000000----');
                this.$device.fingerDelAll('VERIFY').then(res => {
                    console.log('delAll1----'+JSON.stringify(res));
                    console.log('delAll2----'+JSON.stringify(res));
                })
            },
            totalAll(){
                console.log('totalAll000000000000----');
                this.$device.fingerQuerySum('VERIFY').then(res => {
                    console.log('totalAll1----'+JSON.stringify(res));
                    console.log('totalAll2----'+JSON.stringify(res));
                })
            },
        },
        mounted() {
            this.getFingerInfo();
        }
    };
</script>
<style lang="less">
    @import '~@/style/color.less';
    @import "~@/style/finger.less";
</style>