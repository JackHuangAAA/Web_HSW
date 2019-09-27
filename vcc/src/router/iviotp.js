import Main from '@/views/main.vue'
export default [
  {
    path: '/home',
    name: 'home',
    meta: {
      title: '首页',
      icon: 'home'
    },
    component: () => import('@/views/iviotp/home.vue')
  },
  {
    path: '/inoculate',
    name: 'inoculate',
    meta: {
      title: '接种',
      icon: 'inoculate'
    },
    component: Main,
    children: [
      {
        path: '/home',
        component: () => import('@/views/iviotp/home.vue')
      }
    ]
  },
  {
    path: '/inventory',
    name: 'inventory',
    meta: {
      title: '库存',
      icon: 'inventory'
    },
    component: Main,
    children: [
      {
        path: '/home',
        component: () => import('@/views/iviotp/home.vue')
      }
    ]
  },
  {
    path: '/alarm',
    name: 'alarm',
    meta: {
      title: '报警',
      icon: 'alarm'
    },
    component: Main,
    children: [
      {
        path: '/home',
        component: () => import('@/views/iviotp/home.vue')
      }
    ]
  },
  {
    path: '/setting',
    name: 'setting',
    meta: {
      title: '设置',
      icon: 'setting'
    },
    component: Main,
    children: [
      {
        path: '/home',
        component: () => import('@/views/iviotp/home.vue')
      }
    ]
  }
]
