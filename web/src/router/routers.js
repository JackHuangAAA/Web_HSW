const routers = [{
    path: '/',
    component: (resolve) => require(['@/views/home.vue'], resolve),
    children: [
        { path: '/50x', final: true, component: (resolve) =>  require(['@/views/error/50x.vue'], resolve) },
        { path: '/main',name:0, component: (resolve) => require(['@/views/main.vue'], resolve) },
        { path: '/monitor',name:1, component: (resolve) => require(['@/views/monitor/detail.vue'], resolve) },
        { path: '/temperature',name:3, component: (resolve) => require(['@/views/temperature/temperature.vue'], resolve) },
        { path: '/stock/stock',name:2, component: (resolve) => require(['@/views/stock/stock.vue'], resolve) },
        { path: '/stock/stockDetail',name:2, component: (resolve) => require(['@/views/stock/stockDetail.vue'], resolve) },
        { path: '/inout/inout',name:4, component: (resolve) => require(['@/views/inout/inout.vue'], resolve) },
        { path: '/inout/inoutDetail',name:4, component: (resolve) => require(['@/views/inout/inoutDetail.vue'], resolve) },
        { path: '/system/user', component: (resolve) => require(['@/views/system/user.vue'], resolve) }
    ]
}, {
    path: '/login',
    final: true,
    component: (resolve) => require(['@/views/login.vue'], resolve)
}, {
    path: '*',
    final: true,
    component: (resolve) => require(['@/views/error/404.vue'],resolve)
}];

export default routers;