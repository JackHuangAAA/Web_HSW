(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-0aca4848"],{4639:function(e,t,s){"use strict";var r=s("72a3"),n=s.n(r);n.a},5358:function(e,t,s){"use strict";var r=function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{},[s("div",{staticClass:"complete-info"},[s("div",{staticClass:"complete-info-row"},[s("div",[e._v("疫苗："),s("span",[e._v(e._s(e.user?e.user.vaccine.name:""))])]),s("div",[e._v("批次号："),s("span",[e._v(e._s(e.user?e.user.vaccine.batchNo:""))])]),s("div",[e._v("疫苗剂量："),s("span",[e._v(e._s(e.user?e.user.vaccine.dosage:""))])]),s("div",[e._v("监管码："),s("span",[e._v(e._s(e.user?e.user.vaccine.supervisionCode:""))])])]),s("div",{staticClass:"complete-info-row"},[s("div",[e._v("有效期："),s("span",[e._v("2019-09-18 12:30:28   至  "+e._s(e.user?e.user.vaccine.expiry:""))])]),s("div",[e._v("生产厂家："),s("span",[e._v(e._s(e.user?e.user.vaccine.product:""))])]),s("div",[e._v("费用："),s("span",[e._v("Y"+e._s(e.user?e.user.vaccine.cost:""))])])])])])},n=[],c=(s("8e6e"),s("ac6a"),s("456d"),s("bd86")),i=s("2f62");function a(e,t){var s=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),s.push.apply(s,r)}return s}function o(e){for(var t=1;t<arguments.length;t++){var s=null!=arguments[t]?arguments[t]:{};t%2?a(s,!0).forEach((function(t){Object(c["a"])(e,t,s[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(s)):a(s).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(s,t))}))}return e}var u={data:function(){return{data:{}}},props:{datas:Object},computed:o({},Object(i["c"])({user:"user",device:"device"})),methods:o({},Object(i["b"])({saveUser:"saveUser",saveDevice:"saveDevice"})),mounted:function(){}},v=u,p=(s("b382"),s("2877")),d=Object(p["a"])(v,r,n,!1,null,"8fe16eb8",null);t["a"]=d.exports},"72a3":function(e,t,s){},"7fca":function(e,t,s){},b382:function(e,t,s){"use strict";var r=s("7fca"),n=s.n(r);n.a},da82:function(e,t,s){"use strict";s.r(t);var r=function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"mt"},[s("div",{staticClass:"complete"},[s("div",{staticClass:"countDown"},[s("img",{attrs:{src:"/static/img/clock.png",alt:""}}),s("div",[e._v("倒计时00:"+e._s(e.zero?"0":"")+e._s(e.count))])]),e._m(0),s("div",{staticClass:"complete-detail"},[s("detail")],1),s("div",[s("div",{staticClass:"complete-notice"},[e._v("请前往等待区，等待接种")]),s("div",{staticClass:"complete-message"},[e._v("您当前处于"+e._s(this.sort?this.sort:"")+"号，前面还有"+e._s(this.queueLength?this.queueLength:"")+"位")])]),s("div",{staticClass:"complete-confirm",on:{click:function(t){return e.back()}}},[e._v("返回首页")])])])},n=[function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"complete-success"},[s("img",{attrs:{src:"/static/img/success.png",alt:""}}),s("div",[e._v("支付成功!")])])}],c=(s("8e6e"),s("ac6a"),s("456d"),s("7f7f"),s("55dd"),s("96cf"),s("3b8d")),i=s("bd86"),a=s("2f62"),o=s("5358");function u(e,t){var s=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),s.push.apply(s,r)}return s}function v(e){for(var t=1;t<arguments.length;t++){var s=null!=arguments[t]?arguments[t]:{};t%2?u(s,!0).forEach((function(t){Object(i["a"])(e,t,s[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(s)):u(s).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(s,t))}))}return e}var p={data:function(){return{zero:!1,count:10,timer:"",sort:2,queueLength:1}},components:{detail:o["a"]},computed:v({},Object(a["c"])({user:"user",device:"device"})),methods:v({},Object(a["b"])({saveUser:"saveUser",saveDevice:"saveDevice"}),{countdown:function(){var e=this;this.timer=setInterval((function(){e.count--,e.count<10&&(e.zero=!0),0==e.count&&(clearInterval(e.timer),e.$router.push("/main"))}),1e3)},back:function(){clearInterval(this.timer),this.saveUser(null),this.$router.push("/main")},initData:function(){this.customerVaccine=this.user},queryQueue:function(){var e=Object(c["a"])(regeneratorRuntime.mark((function e(){var t,s;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,this.$api.get("/queue/queryQueueByCondition");case 2:return t=e.sent,s=t.data?t.data.length:0,this.sort_now=0!=s?t.data[s-1].sort:0,this.sort=this.sort_now+1,e.next=8,this.$api.get("/queue/queryQueueByCondition",{status:1});case 8:return t=e.sent,this.queueLength=t.data.length?t.data.length:0,console.log("thissort==================>"+JSON.stringify(this.sort)),console.log("thissort==================>"+JSON.stringify(this.user)),this.$device.printTicket({title:"疫苗注射排队",number:String(this.sort),current:this.queueLength}),e.next=15,this.$api.post("/queue/saveQueue",{sort:this.sort,code:this.user.customer.code,name:this.user.customer.name,sex:this.user.customer.sex,age:this.user.customer.age,vaccine:{name:this.user.vaccine.name,code:this.user.vaccine.code,product:this.user.vaccine.product,batchNo:this.user.vaccine.batchNo,dosage:this.user.vaccine.dosage,supervisionCode:this.user.vaccine.supervisionCode,expiry:this.user.vaccine.expiry,cost:this.user.vaccine.cost,count:1,date:new Date},status:1});case 15:case"end":return e.stop()}}),e,this)})));function t(){return e.apply(this,arguments)}return t}()}),mounted:function(){this.countdown(),this.initData(),this.queryQueue()}},d=p,l=(s("4639"),s("2877")),h=Object(l["a"])(d,r,n,!1,null,"a5c714e8",null);t["default"]=h.exports}}]);