<template>
    <div class="datetime">
        <div class="timeslot">
            <slot></slot>
        </div>
        <div class="time"> 
            {{date}}
        </div>
    </div>
</template>
<script>
import moment from 'moment'
export default {
    name:'datetime',
    data(){
        return{
            date:''
        }
    },
    created(){
        this.getTime()
    },
    methods:{
        getTime(){
            setInterval(()=>{
                let day=moment().format('d');
                switch(day){
                    case '1':
                        day='星期一'
                        break;
                    case '2':
                        day='星期二'
                        break;
                    case '3':
                        day='星期三'
                        break;
                    case '4':
                        day='星期四'
                        break;
                    case '5':
                        day='星期五'
                        break;
                    case '6':
                        day='星期六'
                        break;
                    default:
                        day='星期天'
                        break;
                }
                this.date=moment().format('YYYY-MM-DD HH:mm:ss')+' '+day;
                
            },1000)
            
        }
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/color.less';
.datetime{
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-right: 19px;
  padding-left: 17px;
  box-shadow:0px 2px 6px 0px rgba(232,232,232,1);
  background: #fff;
}
.timeslot{
    font-size: 16px;
    color: #FDAA26;
}
.time{
    color: @black;
    font-size: 14px;
}
</style>

