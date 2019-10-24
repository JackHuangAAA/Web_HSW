<!--指纹管理-->
<template>
    <div class="fingerprint card">
        <!-- <Table :columns="cols" :data="datas" size="small" :highlight-row="false" :disabled-hover='false'></Table>
        <div align="center">
            <Button type="primary" @click="register">register</Button>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <Button type="dashed" @click="verify">verify</Button>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <Button type="text" @click="delAll">delAll</Button>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <Button type="success" @click="totalAll">totalAll</Button>
        </div> -->
        <div class="fingerprint-add" v-if="this.user.finger.length<2" @click="addFinger()">新增</div><!---->
        <div class="fingerprint-clear" @click="modal1=true">清除所有</div>
        <!-- <Table :columns="cols" :data="datas" size="small" :highlight-row="false" :disabled-hover='false'></Table> -->
        <Modal
            v-model="modal1"
            title="消息提示"
            @on-ok="ok"
            @on-cancel="cancel">
            <p>是否确认删除所有指纹</p>
        </Modal>
    </div>
</template>

<script>
    import { mapGetters, mapActions } from "vuex";
    export default {
        data() {
            return {
                modal1:false,
                fingerState:false,
                // datas: [],
                // cols: [
                // {
                //     type: 'index',
                //     width: 150
                // },{
                //     title: '指纹CODE',
                //     key: 'finger'
                // },{
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
                //     }],
                // data: []
            };
        },
        computed: {
            ...mapGetters({
                user: "user",
                device: "device"
            })
        },
        methods: {
            ...mapActions({
                saveUser: 'saveUser',
            }),
            ok(){
                this.delAll()
            },
            cancel(){
                this.modal1=false
            },
            //若指纹数小于2 跳到录入页面
            addFinger(){
                if(this.user.finger.length<2){
                    this.fingerState=true
                    this.$emit('add',false)
                }else{
                    this.fingerState=false
                }
            },
            //指纹数小于2 重复录入
            FingerCountinue(){
                if(this.fingerState=true){
                    this.addFinger()
                }
            },
            //删除所有指纹
            delAll(){
                console.log('delAll000000000000----');
                console.log(JSON.stringify(this.user))
                this.$device.fingerDelAll({userId:this.user._id}).then(res => {
                    if(JSON.parse(res.rsp).code==0){
                    this.queryUserByCondition({id:this.user._id,finger:[]})
                    this.user.finger=[]
                    // this.getFingerInfo()
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
            // getFingerInfo() {
            //     let data = [];
            //     for (let f of this.user.finger) {
            //         let t = {};
            //         t.finger = f;
            //         data.push(t);
            //     }
            //     this.datas = data
            // },
            // delFinger(row){
            //     this.$device.fingerRegister('DEL_ONE_TEMPLATE').then(res => {
            //         console.log('delFinger1----'+JSON.stringify(res));
            //         console.log('delFinger2----'+JSON.stringify(res));
            //     })
            // },
            // register(){
            //     console.log('register000000000000----');
            //     this.$device.fingerRegister('REGISTER').then(res => {
            //         console.log('register1----'+JSON.stringify(res));
            //         console.log('register2----'+JSON.stringify(res));
            //     })
            // },
            // verify(){
            //     console.log('verify000000000000----');
            //     this.$device.fingerVerify('VERIFY').then(res => {
            //         console.log('verify1----'+JSON.stringify(res));
            //         console.log('verify2----'+JSON.stringify(res));
            //     })
            // },
            // delAll(){
            //     console.log('delAll000000000000----');
            //     this.$device.fingerDelAll('VERIFY').then(res => {
            //         console.log('delAll1----'+JSON.stringify(res));
            //         console.log('delAll2----'+JSON.stringify(res));
            //     })
            // },
            // totalAll(){
            //     console.log('totalAll000000000000----');
            //     this.$device.fingerQuerySum('VERIFY').then(res => {
            //         console.log('totalAll1----'+JSON.stringify(res));
            //         console.log('totalAll2----'+JSON.stringify(res));
            //     })
            // },
        },
        mounted() {
            console.log(this.user.finger)
            // this.getFingerInfo();
            this.FingerCountinue()
        }
    };
</script>
<style lang="less">
    @import '~@/style/color.less';
    @import "~@/style/finger.less";
</style>