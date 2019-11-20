const routers = [
    {
        path: '/displayHome',
        component: (resolve) => require(['@/views/displayHome.vue'], resolve),
        children: [
            { path: '/displayHome/callNumber', component: (resolve) => require(['@/views/display/callNumber.vue'], resolve) },
            { path: '/displayHome/desk', component: (resolve) => require(['@/views/display/desk.vue'], resolve) },
            { path: '/displayHome/check', component: (resolve) => require(['@/views/display/check.vue'], resolve) }
        ]
    },
    {
        path: '/',
        component: (resolve) => require(['@/views/home.vue'], resolve),
        children: [
            { path: '/main', component: (resolve) => require(['@/views/main.vue'], resolve) },
            { path: '/register/register', component: (resolve) => require(['@/views/register/register.vue'], resolve) },
            { path: '/complete/complete', component: (resolve) => require(['@/views/complete/complete.vue'], resolve) },
            { path: '/pay/pay', component: (resolve) => require(['@/views/pay/pay.vue'], resolve) },
            { path: '/pay/payQrcode', component: (resolve) => require(['@/views/pay/payQrcode.vue'], resolve) },
            { path: '/print/printInf', component: (resolve) => require(['@/views/print/printInf.vue'], resolve) },
            { path: '/print/printEnd', component: (resolve) => require(['@/views/print/printEnd.vue'], resolve) }
        ]
    }, {
        path: '*',
        final: true,
        component: (resolve) => require(['@/views/error/404.vue'],resolve)
},
{ path: '/print/printMain', component: (resolve) => require(['@/views/print/printMain.vue'], resolve) }
];

export default routers;