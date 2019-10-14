const routers = [{
    path: '/',
    component: (resolve) => require(['@/views/home.vue'], resolve),
    children: [
        { path: '/50x', final: true, component: (resolve) =>  require(['@/views/error/50x.vue'], resolve) },
        { path: '/main', component: (resolve) => require(['@/views/main.vue'], resolve) },
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