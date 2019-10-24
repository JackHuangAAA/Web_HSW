const routers = [{
    path: '/',
    component: (resolve) => require(['@/views/home.vue'], resolve),
    children: [
        { path: '/50x', final: true, component: (resolve) =>  require(['@/views/error/50x.vue'], resolve) },
        { path: '/main', component: (resolve) => require(['@/views/main.vue'], resolve) },
        { path: '/setting/setting', component: (resolve) => require(['@/views/setting/setting.vue'], resolve) },
        { path: '/alarm/alarm', component: (resolve) => require(['@/views/alarm/alarm.vue'], resolve) },
        { path: '/stock/stock', component: (resolve) => require(['@/views/stock/stock.vue'], resolve) },
        { path: '/stock/stockDetail', component: (resolve) => require(['@/views/stock/stockDetail.vue'], resolve) },
        { path: '/inout/flowing', component: (resolve) => require(['@/views/inout/flowing.vue'], resolve) },
        { path: '/inout/inStock', component: (resolve) => require(['@/views/inout/inStock.vue'], resolve) },
        { path: '/inout/inPosition', component: (resolve) => require(['@/views/inout/inPosition.vue'], resolve) },
        { path: '/inout/inStockEnd', component: (resolve) => require(['@/views/inout/inStockEnd.vue'], resolve) },
        { path: '/inout/outStock', component: (resolve) => require(['@/views/inout/outStock.vue'], resolve) },
        { path: '/inout/outStockEnd', component: (resolve) => require(['@/views/inout/outStockEnd.vue'], resolve) }
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