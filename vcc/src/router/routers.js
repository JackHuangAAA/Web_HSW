import Main from '@/views/main.vue'
import vcc from './vcc'
const routers = [
  {
    path: '/',
    component: Main,
    children: [
      ...vcc,
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
