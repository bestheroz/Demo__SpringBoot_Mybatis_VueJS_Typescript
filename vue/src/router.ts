import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

const requireAuth = () => async (to: any, from: any, next: any) => {
  return next();
};

const routes = () => {
  const admin = [
    {
      path: 'code',
      component: () => import('@/views/admin/code/Code.vue'),
    },
    {
      path: 'menu',
      component: () => import('@/views/admin/menu/Menu.vue'),
    },
    {
      path: 'menu/authority',
      component: () => import('@/views/admin/menuauthority/MenuAuthority.vue'),
    },
    {
      path: 'member',
      component: () => import('@/views/admin/member/Member.vue'),
    },
  ];

  return [
    {
      path: '/login',
      component: () => import('@/views/index/IndexNoDrawer.vue'),
      children: [
        {
          name: 'Login',
          path: '',
          component: () => import('@/views/login/Login.vue'),
        },
      ],
    },
    {
      path: '/',
      component: () => import('@/views/Redirect.vue'),
    },
    {
      path: '/admin',
      component: () => import('@/views/index/Index.vue'),
      beforeEnter: requireAuth(),
      children: admin,
    },
    {
      path: '/error',
      component: () => import('@/views/index/IndexNoDrawer.vue'),
      children: [
        {
          name: 'Error',
          path: '',
          component: () => import('@/views/error/Error500.vue'),
        },
        {
          name: '403 Forbidden',
          path: '403',
          component: () => import('@/views/error/Error403.vue'),
        },
        {
          name: '500 Internal Server Error',
          path: '500',
          component: () => import('@/views/error/Error500.vue'),
        },
        {
          name: '503 Service Unavailable',
          path: '503',
          component: () => import('@/views/error/Error503.vue'),
        },
        {
          name: '404 Page not found',
          path: '404',
          component: () => import('@/views/error/Error404.vue'),
        },
      ],
    },
    {
      path: '*',
      component: () => import('@/views/index/IndexNoDrawer.vue'),
      children: [
        {
          path: '',
          component: () => import('@/views/error/Error404.vue'),
        },
      ],
    },
  ];
};

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: routes(),
});
