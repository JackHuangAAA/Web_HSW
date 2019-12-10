const routers = [
    {
        path: '/',               //显示器页面
        component: (resolve) => require(['@/views/home.vue'], resolve),
        children: [
            { path: '/display/queue', component: (resolve) => require(['@/views/display/queue.vue'], resolve) }, //叫号综合显示屏
            { path: '/display/desk', component: (resolve) => require(['@/views/display/desk.vue'], resolve) },   //一号登记台
            { path: '/display/check', component: (resolve) => require(['@/views/display/check.vue'], resolve) }  //留观显示屏
        ]
    },
    {
        path: '*',
        final: true,
        component: (resolve) => require(['@/views/error/404.vue'],resolve)
    }

];

export default routers;