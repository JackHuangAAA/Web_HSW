<!--指纹管理-->
<template>
    <div class="fingerprint card">
        <div class="fingerprint-add" v-if="this.user.finger.length<2" @click="addFinger()">新增</div>
        <div class="fingerprint-clear" @click="modal1=true">清除</div>
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
            };
        },
        computed: {
            ...mapGetters({
                user: "user",
                device: "device",
            })
        },
        watch:{
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
                if(this.fingerState==true){
                    this.addFinger()
                }
            },
            //删除所有指纹
            delAll(){
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
                let res=await this.$api.post('/user/modifyUserById',params)
                if(res.data.ok==1){
                    this.user.finger=[]
                }
            }
        },
        mounted() {
            this.FingerCountinue()
        }
    };
</script>
<style lang="less">
    @import '~@/style/color.less';
    @import "~@/style/finger.less";
</style>