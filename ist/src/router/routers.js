const routers = [
    {
        path: '/1',
        component: (resolve) => require(['@/views/printHome.vue'], resolve),   //补打一体机主页
        children:[
            { path: '/print/printMain', component: (resolve) => require(['@/views/print/printMain.vue'], resolve) },//打印主页
            { path: '/print/printInf', component: (resolve) => require(['@/views/print/printInf.vue'], resolve) },  //打印
            { path: '/print/printEnd', component: (resolve) => require(['@/views/print/printEnd.vue'], resolve) }   //打印成功
        ]
    },
    {
        path: '/',
        component: (resolve) => require(['@/views/home.vue'], resolve),
        children: [
            { path: '/main', component: (resolve) => require(['@/views/main.vue'], resolve) },  //智能接种主页
            { path: '/register/register', component: (resolve) => require(['@/views/register/register.vue'], resolve) }, //挂号、选择疫苗信息
            { path: '/register/free', component: (resolve) => require(['@/views/register/free.vue'], resolve) }, //选择免费疫苗信息
            { path: '/pay/pay', component: (resolve) => require(['@/views/pay/pay.vue'], resolve) }, //付费方式选择
            { path: '/pay/payQrcode', component: (resolve) => require(['@/views/pay/payQrcode.vue'], resolve) },//扫码支付
            { path: '/pay/finish', component: (resolve) => require(['@/views/pay/finish.vue'], resolve) } //支付完成
        ]
    },
    {
        path: '*',
        final: true,
        component: (resolve) => require(['@/views/error/404.vue'],resolve)
    }

];

export default routers;