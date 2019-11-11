const routers = [{
    path: '/',
    component: (resolve) => require(['@/views/home.vue'], resolve),
    children: [
        { path: '/50x', final: true, component: (resolve) =>  require(['@/views/error/50x.vue'], resolve) },
        { path: '/main', component: (resolve) => require(['@/views/main.vue'], resolve) },
        { path: '/monitor/device', component: (resolve) => require(['@/views/monitor/detail.vue'], resolve) },
        { path: '/temperature/temperature', component: (resolve) => require(['@/views/temperature/temperature.vue'], resolve) },
        { path: '/stock/stock', component: (resolve) => require(['@/views/stock/stock.vue'], resolve) },
        { path: '/stock/stockDetail', component: (resolve) => require(['@/views/stock/stockDetail.vue'], resolve) },
        { path: '/inout/inout', component: (resolve) => require(['@/views/inout/inout.vue'], resolve) },
        { path: '/inout/inoutDetail', component: (resolve) => require(['@/views/inout/inoutDetail.vue'], resolve) },
        { path: '/system/user', component: (resolve) => require(['@/views/system/user.vue'], resolve) },
        { path: '/system/role', component: (resolve) => require(['@/views/system/role.vue'], resolve) },
        { path: '/system/param', component: (resolve) => require(['@/views/system/param.vue'], resolve) },
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