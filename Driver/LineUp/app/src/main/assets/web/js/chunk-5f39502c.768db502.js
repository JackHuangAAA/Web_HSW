(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-5f39502c"],{"32ef":function(e,t,s){},"34f0":function(e,t,s){"use strict";var n=s("32ef"),c=s.n(n);c.a},5358:function(e,t,s){"use strict";var n=function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{},[s("div",{staticClass:"complete-info"},[s("div",{staticClass:"complete-info-row"},[s("div",[e._v("疫苗："),s("span",[e._v(e._s(e.user?e.user.vaccine.name:""))])]),s("div",[e._v("批次号："),s("span",[e._v(e._s(e.user?e.user.vaccine.batchNo:""))])]),s("div",[e._v("疫苗剂量："),s("span",[e._v(e._s(e.user?e.user.vaccine.dosage:""))])]),s("div",[e._v("监管码："),s("span",[e._v(e._s(e.user?e.user.vaccine.supervisionCode:""))])])]),s("div",{staticClass:"complete-info-row"},[s("div",[e._v("有效期："),s("span",[e._v("2019-09-18 12:30:28   至  "+e._s(e.user?e.user.vaccine.expiry:""))])]),s("div",[e._v("生产厂家："),s("span",[e._v(e._s(e.user?e.user.vaccine.product:""))])]),s("div",[e._v("费用："),s("span",[e._v("Y"+e._s(e.user?e.user.vaccine.cost:""))])])])])])},c=[],r=(s("8e6e"),s("ac6a"),s("456d"),s("bd86")),i=s("2f62");function a(e,t){var s=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),s.push.apply(s,n)}return s}function o(e){for(var t=1;t<arguments.length;t++){var s=null!=arguments[t]?arguments[t]:{};t%2?a(s,!0).forEach((function(t){Object(r["a"])(e,t,s[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(s)):a(s).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(s,t))}))}return e}var u={data:function(){return{data:{}}},props:{datas:Object},computed:o({},Object(i["c"])({user:"user",device:"device"})),methods:o({},Object(i["b"])({saveUser:"saveUser",saveDevice:"saveDevice"})),mounted:function(){}},v=u,f=(s("b382"),s("2877")),p=Object(f["a"])(v,n,c,!1,null,"8fe16eb8",null);t["a"]=p.exports},"7fca":function(e,t,s){},b382:function(e,t,s){"use strict";var n=s("7fca"),c=s.n(n);c.a},c772:function(e,t,s){"use strict";s.r(t);var n=function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"container"},[s("div",{staticClass:"personInf"},[s("p",[e._v("基本信息")]),s("div",[s("span",{staticClass:"infTitle"},[e._v("姓名:")]),s("span",{staticClass:"infContent"},[e._v(e._s(e.user?e.user.name:""))]),s("span",{staticClass:"infTitle"},[e._v("性别:")]),s("span",{staticClass:"infContent"},[e._v(e._s(e.user?e.user.sex:""))]),s("span",{staticClass:"infTitle"},[e._v("年龄:")]),s("span",{staticClass:"infContent"},[e._v(e._s(e.user?e.user.age:"")+"周岁")]),s("span",{staticClass:"infTitle"},[e._v("距离上次接种时间:")]),s("span",{staticClass:"infContent day"},[e._v(e._s(e.user?e.user.intervalTime:"")+"天")])])]),s("div",{staticClass:"vaccineInf"},[s("p",[e._v("疫苗信息")]),s("detail",{staticClass:"detail"})],1),s("div",{staticClass:"btn-box"},[s("div",{staticClass:"cancel",on:{click:e.back}},[e._v("取消")]),s("div",{staticClass:"confirm",on:{click:function(t){return e.confirm()}}},[e._v("打印信息")])]),s("p",{staticClass:"confirmTip"},[e._v("请将疫苗本放入打印机，点击打印信息按钮")])])},c=[],r=(s("8e6e"),s("ac6a"),s("456d"),s("7f7f"),s("bd86")),i=s("2f62"),a=s("5358"),o=s("c1df"),u=s.n(o);function v(e,t){var s=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),s.push.apply(s,n)}return s}function f(e){for(var t=1;t<arguments.length;t++){var s=null!=arguments[t]?arguments[t]:{};t%2?v(s,!0).forEach((function(t){Object(r["a"])(e,t,s[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(s)):v(s).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(s,t))}))}return e}var p={data:function(){return{customer:{}}},components:{detail:a["a"]},computed:f({},Object(i["c"])({user:"user",device:"device"})),methods:f({},Object(i["b"])({saveUser:"saveUser",saveDevice:"saveDevice"}),{confirm:function(){var e=this;this.$device.printBook({num:1,need_name:!0,vaccine:this.user.vaccine.name,vaccine_date:u()(this.user.vaccine.date).format("YYYYMMDD"),lot_no:this.user.vaccine.batchNo,vaccine_unit:this.device.unitName,vaccine_site:"左手臂",signature:this.user.name}).then((function(t){console.log("打印返回结果"+JSON.stringify(t)),e.$router.push("/print/printEnd")})),console.log("这里是末尾")},back:function(){this.$router.push("/print/printMain")}}),mounted:function(){}},l=p,d=(s("34f0"),s("2877")),b=Object(d["a"])(l,n,c,!1,null,"fa0e388c",null);t["default"]=b.exports}}]);