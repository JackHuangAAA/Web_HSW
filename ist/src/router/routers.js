const routers = [
    { path: '/main', component: (resolve) => require(['@/views/main.vue'], resolve) },
    {
        path: '/',
        component: (resolve) => require(['@/views/home.vue'], resolve),
        children: [
        ]
    }, {
        path: '*',
        final: true,
        component: (resolve) => require(['@/views/error/404.vue'],resolve)
}];

export default routers;