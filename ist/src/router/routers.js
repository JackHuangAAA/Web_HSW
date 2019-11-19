const routers = [
    { path: '/main', component: (resolve) => require(['@/views/main.vue'], resolve) },
    {
        path: '/',
        component: (resolve) => require(['@/views/home.vue'], resolve),
        children: [
            { path: '/register/register', component: (resolve) => require(['@/views/register/register.vue'], resolve) },
            { path: '/complete/complete', component: (resolve) => require(['@/views/complete/complete.vue'], resolve) },
            { path: '/pay/pay', component: (resolve) => require(['@/views/pay/pay.vue'], resolve) }
        ]
    }, {
        path: '*',
        final: true,
        component: (resolve) => require(['@/views/error/404.vue'],resolve)
}];

export default routers;