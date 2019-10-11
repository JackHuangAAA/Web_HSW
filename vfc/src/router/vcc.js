import Main from '@/views/main.vue'
export default [
  {
    path: '/home',
    name: 'home',
    meta: {
      title: '主页',
      icon: 'home'
    },
    component: () => import('@/views/home/home.vue')
  },
  {
    path: '/inventory',
    name: 'inventory',
    redirect: '/vaccine/inventorys',
    meta: {
      title: '库存',
      icon: 'inventory',
      main: 'inventorys'
    }
  },
  {
    path: '/alarm',
    name: 'alarm',
    meta: {
      title: '报警',
      icon: 'alarm'
    },
    component: () => import('@/views/alarm/alarm.vue')
  },
  {
    path: '/setting',
    name: 'setting',
    meta: {
      title: '设置',
      icon: 'setting'
    },
    component: () => import('@/views/setting/setting.vue')
  }
]
