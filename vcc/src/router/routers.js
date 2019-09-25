import Main from '@/views/main.vue'
const routers = [
  {
    path: '/',
    component: Main
    // children: [
    //   {
    //     path: '/50x',
    //     final: true,
    //     component: () => import('@/views/error/50x.vue')
    //   }
    //   //   {
    //   //     path: '/main',
    //   //     component: resolve => require(['@/views/main.vue'], resolve)
    //   //   },
    //   //   {
    //   //     path: '/system/user',
    //   //     component: resolve => require(['@/views/system/user.vue'], resolve)
    //   //   }
    // ]
  },
  {
    path: '/main',
    final: true,
    component: () => import('@/views/main.vue')
  },
  {
    path: '/login',
    final: true,
    component: () => import('@/views/login.vue')
  },
  {
    path: '*',
    final: true,
    component: () => import('@/views/error/404.vue')
  }
]

export default routers
