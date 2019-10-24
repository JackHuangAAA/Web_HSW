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
      this.$device.fingerSearch().then(res=>{
        console.log('register1----'+JSON.stringify(res));
        console.log('register2----'+JSON.stringify(res));
      })
    },
    //根据id查询用户信息，存储用户信息
    async login(_id){
      console.log('login----------'+_id);
      //let user1={"finger":["F-999","F-888"],"_id":"5d89b7af3b11e1a1733ef870","code":"admin","name":"小米","type":1,"createDate":"2019-06-05T07:50:04.054Z","updateDate":"2019-07-01T07:01:57.595Z","status":1,"lastLogin":"2019-10-23T06:46:56.217Z","token":"5B7DD7911E000000"}
      let user =await this.$api.get("/user/queryUserByCondition", {_id:_id});
      console.log('====================');
      console.log(JSON.stringify(user))
      console.log(JSON.stringify(user.data[0]));
      console.log(user);
      console.log('*************');
      if(!_.isEmpty(JSON.stringify(user))){
        console.log(JSON.parse(JSON.stringify(user)).data[0]);
        this.saveUser(JSON.stringify(user.data[0]));
        await this.$api.post("/user/modifyUserByCode", JSON.parse(JSON.stringify(user)));
        //this.$Message.info("登录成功")
        this.$router.push('/');
      }else{
        this.message = '登陆用户不存在';
      }
      
    },
    async test(){
      let user1={"finger":["F-999","F-888"],"_id":"5d89b7af3b11e1a1733ef870","code":"admin","name":"小米","type":1,"createDate":"2019-06-05T07:50:04.054Z","updateDate":"2019-07-01T07:01:57.595Z","status":1,"lastLogin":"2019-10-23T06:46:56.217Z","token":"5B7DD7911E000000"}
      //let user =await this.$api.get("/user/queryUserByCondition", {_id:_id});
      if(!_.isEmpty(user1)){
        this.saveUser(user1);
        await this.$api.post("/user/modifyUserByCode", user1);
        //this.$Message.info("登录成功")
        this.$router.push('/');
      }else{
        this.message = '登陆用户不存在';
      }
    }
  },
  mounted(){
    
    // 设备反馈监听
    this.$device.subscribe('FINGER_MESSAGE', (data) => {
      console.log("fingerLogin================>"+JSON.stringify(data))
      if(data.type==1){//type为1失败2成功3重新调用指纹验证
         this.message=data.msg;
      }else if(data.type==2){
        //data.msg.tag为id
        
        let _id=JSON.parse(data.msg).tag
        console.log("登录成功============================>"+_id)
        console.log('shif:user---' + this.user);
        console.log('shif:user---' + JSON.stringify(this.user));
        if(this.user!=null){
          console.log("本地已有用户信息==============>")
          console.log(this.user)
          console.log(JSON.stringify(this.user))
          console.log(this.user._id)
          console.log(JSON.parse(JSON.stringify(this.user))._id)
          console.log("==========================**********************************")
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
          console.log("本地信息不存在,根据_id查询用户保存信息跳转==================>")
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