<template>
  <div style="position:relative;height:100%">
    <img src="/static/img/fplogin.png" class="fpbg">
    <P class="fingerTip">使用指纹一键登录</P>
    <div class="fplogin-message">{{message}}</div>
  </div>
</template>

<script>
import {mapGetters,mapActions} from 'vuex'
export default {
  name: "fplogin",
  data() {
    return {
      message:''
    };
  },
  computed:{
    ...mapGetters({
      user: 'user',
    })
  },
  methods: {
    ...mapActions({
      saveUser: 'saveUser',
      saveDevice: 'saveDevice'
    }),
    disclick() {
      this.$emit("click");
    },
    // 登录指纹验证
    fingerLogin(){
      this.$device.fingerSearch()
    },
    //根据id查询用户信息，存储用户信息
    async login(_id){
      let user =await this.$api.get("/user/queryUserByCondition", {_id:_id});
      if(!_.isEmpty(user)){
        this.saveUser(user.data[0]);
        await this.$api.post("/user/modifyUserByCode", user);//重点，勿删除
        this.$device.openDoor().then(res=>{
          console.log("开门结果 result:"+ JSON.stringify(res.rsp));
          //结果为true门打开了
        })
        this.$router.push('/');
      }else{
        this.message = '登陆用户不存在';
      }
    }
  },
  mounted(){
    // 设备反馈监听
    this.$device.subscribe('FINGER_MESSAGE', (data) => {
      if(data.type==1){//type为1失败2成功3重新调用指纹验证
        this.message=data.msg;
      }else if(data.type==2){
        //data.msg.tag为id
        let _id=JSON.parse(data.msg).tag
        if(this.user!=null){
          if(this.user._id==_id){
            this.message='登录成功'
            this.$api.post("/user/modifyUserByCode", this.user);
            this.$device.openDoor().then(res=>{
              console.log("开门结果 result:"+ JSON.stringify(res.rsp));
            });
            this.$router.push('/')
          }
        }else{
          this.login(_id)
        }
      }else if(data.type==3){
        this.message=data.msg;
        this.fingerLogin()
      }
    });
    //激活指纹模块
    this.fingerLogin()
  },
};
</script>

<style lang="less">
  @import "~@/style/fplogin.less";
</style>