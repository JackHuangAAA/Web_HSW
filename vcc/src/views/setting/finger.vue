<!--指纹管理-->
<template>
    <div class="fingerprint card">
        <div class="fingerprint-add" v-if="this.user.finger.length<2" @click="addFinger()">新增</div><!---->
        <div class="fingerprint-clear" @click="delAll()">清除所有</div>
        <Table :columns="cols" :data="datas" size="small" :highlight-row="false" :disabled-hover='false'></Table>
    </div>
</template>

<script>
    import { mapGetters, mapActions } from "vuex";
    export default {
        data() {
            return {
                datas: [],
                cols: [
                {
                    type: 'index',
                    width: 300
                },{
                    title: '指纹CODE',
                    key: 'finger'
                },
                // {
                //     title: '操作',
                //     width: 145,
                //     render: (h, params) => {
                //          return h('div', [
                //                 h('Button', {
                //                     props: {
                //                         size: 'small'
                //                     },
                //                     style: {
                //                         border: '1px solid #3399ff',
                //                         'font-size': '14px',
                //                         'margin-right':'10px'
                //                     },
                //                     on: {
                //                         click: () => {
                //                             this.delFinger(params.row);
                //                         }
                //                     }
                //                 },'删除')
                //          ]);
                //     }
                //   }
                ],
                data: []
            };
        },
        computed: {
            ...mapGetters({
                user: "user",
                device: "device"
            })
        },
        watch:{
            '$route'(){
                this.getFingerInfo()
            }
        },
        methods: {
            ...mapActions({
                saveUser: 'saveUser',
            }),
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
            //删除所有指纹
            delAll(){
                console.log('delAll000000000000----');
                console.log(JSON.stringify(this.user))
                this.$device.fingerDelAll({userId:this.user._id}).then(res => {
                    if(JSON.parse(res.rsp).code==0){
                    this.queryUserByCondition({id:this.user._id,finger:[]})
                    this.user.finger=[]
                    this.getFingerInfo()
                }
                })
            },
            //指纹数据更新
            async queryUserByCondition(params){
                console.log("entry")
                let res=await this.$api.post('/user/modifyUserById',params)
                if(res.data.ok==1){
                    this.user.finger=[]
                }
            }
        },
        mounted() {
            console.log(this.user.finger)
            this.getFingerInfo();
        }
    };
</script>
<style lang="less">
    @import '~@/style/color.less';
    @import "~@/style/finger.less";
</style>