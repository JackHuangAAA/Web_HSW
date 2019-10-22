<template>
  <div @click="disclick()" style="position:relative;height:100%">
    <img src="/static/img/fplogin.png" class="fpbg">
    <P class="fingerTip">使用指纹一键登录</P>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
export default {
  name: "fplogin",
  data() {
    return {};
  },
  computed:{
    ...mapGetters({
      user: 'user',
    })
  },
  methods: {
    disclick() {
      this.$emit("click");
    },
    // 登录指纹验证
    fingerLogin(){
      this.$device.fingerSearch().then(res=>{
        console.log('register1----'+JSON.stringify(res));
        console.log('register2----'+JSON.stringify(res));
      })
    },
  },
  mounted(){
    // 设备反馈监听
    this.$device.subscribe('FINGER_MESSAGE', (data) => {
      console.log("fingerLogin================>"+JSON.stringify(data))
      if(data.type==1){//type为1失败2成功3重新调用指纹验证
         this.$Message.info(data.msg);
      }else if(data.type==2){
        //data.msg.tag为id
        this.$Message.info("登录成功")
        console.log("登录成功============================>"+JSON.parse(data.msg).tag)
        let _id=JSON.parse(data.msg).tag
        if(this.user._id){
          if(this.user_id==_id){
            this.$router.push('/main')
          }else{
            //指纹用户id和设备用户id不同
          }
        }else{
          //根据id查询用户信息，存储用户信息
          let user = this.$api.post("/user/modifyUserByCode", {_id});
          this.saveUser(user.data)
        }
      }else if(data.type==3){
        this.$Message.info(data.msg)
        this.fingerLogin()
      }
    })
    this.fingerLogin()
  },
};
</script>

<style lang="less">
  @import "~@/style/fplogin.less";
</style>