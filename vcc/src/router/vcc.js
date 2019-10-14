export default [
  {
    path: '/home',
    meta: {
      title: '首页',
      icon: 'home'
    },
    component: () => import('@/views/home.vue')
  },
  {
    path: '/vaccination',
    meta: {
      title: '接种',
      icon: 'inoculate'
    },
    component: () => import('@/views/vaccination/vaccination.vue')
  },
  {
    path: '/inventory',
    meta: {
      title: '库存',
      icon: 'inventory',
      main: 'inventorys'
    }
  },
  {
    path: '/alarm',
    meta: {
      title: '报警',
      icon: 'alarm'
    },
    component: () => import('@/views/alarm/alarm.vue')
  },
  {
    path: '/setting',
    meta: {
      title: '设置',
      icon: 'setting'
    },
    component: () => import('@/views/setting/setting.vue')
  }
];
