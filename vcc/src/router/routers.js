import Main from '@/views/main.vue';
import vcc from './vcc';
const routers = [
  {
    path: '/',
    component: Main,
    redirect: '/home',
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
    path: '/vaccine',
    name: 'vaccine',
    component: Main,
    children: [
      {
        path: 'inventorys',
        name: 'inventorys',
        meta: {
          title: '库存'
        },
        component: () => import('@/views/vaccine/inventory/inventorys.vue')
      },
      {
        path: 'Details/:id',
        name: 'inventoryDetails',
        component: () =>
          import('@/views/vaccine/inventory/inventoryDetails.vue')
      },
      {
        path: 'inbound',
        name: 'inbound',
        component: () => import('@/views/vaccine/inbound/inbound.vue')
      },
      {
        path: 'checkin',
        name: 'checkin',
        component: () => import('@/views/vaccine/inbound/checkin.vue')
      },
      {
        path: 'outbound',
        name: 'outbound',
        component: () => import('@/views/vaccine/outbound/outbound.vue')
      },
      {
        path: 'opendrawer',
        name: 'opendrawer',
        component: () => import('@/views/vaccine/outbound/opendrawer.vue')
      },
      {
        path: 'complete/:type',
        name: 'complete',
        component: () => import('@/views/vaccine/complete/complete.vue')
      }
    ]
  },
  {
    path: '/test',
    final: true,
    component: () => import('@/views/home/thermometer/thermometer.vue')
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
];

export default routers;
