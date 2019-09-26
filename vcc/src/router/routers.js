import Main from '@/views/main.vue'
import iviotp from './iviotp'
const routers = [
  {
    path: '/',
    component: Main,
    children: [
      ...iviotp,
      {
        path: '/50x',
        final: true,
        component: () => import('@/views/error/50x.vue')
      }
    ]
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
