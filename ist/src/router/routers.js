const routers = [
    {
        path: '/22',               //显示器页面
        component: (resolve) => require(['@/views/displayHome.vue'], resolve),
        children: [
            { path: '/displayHome/callNumber', component: (resolve) => require(['@/views/display/callNumber.vue'], resolve) }, //叫号综合显示屏
            { path: '/displayHome/desk', component: (resolve) => require(['@/views/display/desk.vue'], resolve) },  //一号登记台
            { path: '/displayHome/check', component: (resolve) => require(['@/views/display/check.vue'], resolve) } //留观显示屏
        ]
    },
    {
        path: '/11',
        component: (resolve) => require(['@/views/print/printMain.vue'], resolve),   //补打一体机主页
        children:[
            { path: '/print/printInf', component: (resolve) => require(['@/views/print/printInf.vue'], resolve) },//打印
            { path: '/print/printEnd', component: (resolve) => require(['@/views/print/printEnd.vue'], resolve) }//打印成功
        ]
    },
    {
        path: '/',
        component: (resolve) => require(['@/views/home.vue'], resolve),
        children: [
            { path: '/main', component: (resolve) => require(['@/views/main.vue'], resolve) },  //智能接种主页
            { path: '/register/register', component: (resolve) => require(['@/views/register/register.vue'], resolve) }, //挂号信息
            { path: '/complete/complete', component: (resolve) => require(['@/views/complete/complete.vue'], resolve) },//免费选完疫苗  支付成功  人工付费
            { path: '/pay/pay', component: (resolve) => require(['@/views/pay/pay.vue'], resolve) },//付费
            { path: '/pay/payQrcode', component: (resolve) => require(['@/views/pay/payQrcode.vue'], resolve) },//扫码

        ]
    },
    {
        path: '*',
        final: true,
        component: (resolve) => require(['@/views/error/404.vue'],resolve)
    }

];

export default routers;