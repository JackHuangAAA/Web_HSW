<template>
  <div @click="test()" style="position:relative;height:100%">
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
      message:'',
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
      console.log('login----------'+_id);
      //let user1={"finger":["F-999","F-888"],"_id":"5d89b7af3b11e1a1733ef870","code":"admin","name":"小米","type":1,"createDate":"2019-06-05T07:50:04.054Z","updateDate":"2019-07-01T07:01:57.595Z","status":1,"lastLogin":"2019-10-23T06:46:56.217Z","token":"5B7DD7911E000000"}
      let user =await this.$api.get("/user/queryUserByCondition", {_id:_id});
      if(!_.isEmpty(JSON.stringify(user))){
        this.saveUser(JSON.stringify(user.data[0]));
        await this.$api.post("/user/modifyUserByCode", JSON.parse(JSON.stringify(user)));
        this.$router.push('/');
      }else{
        this.message = '登陆用户不存在';
      }
      
    },
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
          if(JSON.parse(JSON.stringify(this.user))._id==_id){
            this.message='登录成功'
            this.$router.push('/')
          }else{
            //指纹用户id和设备已有的用户id不同
            console.log("指纹用户id和设备已有的用户id不同======================》")
            console.log(this.user._id+"本地设备用户id")
            console.log(_id+"用户id")
          }
        }else{
          this.login(_id)
        }
      }else if(data.type==3){
        this.message=data.msg
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