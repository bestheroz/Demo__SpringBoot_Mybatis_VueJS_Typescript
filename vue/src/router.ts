import Vue from 'vue';
import Router from 'vue-router';
import store from './store';
import { ApiDataResult } from '@/utils/apis';
import _ from 'lodash';
import { LoginVO, TableMemberVO } from '@/common/types';
import envs from '@/constants/envs';
import { alertError } from '@/utils/alerts';

Vue.use(Router);

const requireAuth = () => async (to: any, from: any, next: any) => {
  // if (!Vue.$storage.has('accessToken')) {
  //   next('/login');
  // } else {
  //   try {
  //     const response = await axios.post<
  //       ApiDataResult<LoginVO>
  //     >(`${envs.API_HOST}api/auth/verify`);
  //     if (_.startsWith(response.data.code, `S`)) {
  //       store.commit('loginToken', response.data.data);
  //     } else {
  //       store.commit('logout');
  //     }
  //   } catch (e) {
  //     alertError(e);
  //     if (e.response.status === 401) {
  //       store.commit('logout');
  //     } else if (e.message === 'Network Error') {
  //       next('/Code503');
  //     } else {
  //       next('/Code500');
  //     }
  //   }
  // }
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
      path: '*',
      component: () => import('@/views/index/IndexNoDrawer.vue'),
      children: [
        {
          name: '403 Forbidden',
          path: 'Code403',
          component: () => import('@/views/error/Error403.vue'),
        },
        {
          name: '500 Internal Server Error',
          path: 'Code500',
          component: () => import('@/views/error/Error500.vue'),
        },
        {
          name: '503 Service Unavailable',
          path: 'Code503',
          component: () => import('@/views/error/Error503.vue'),
        },
        {
          name: '404 Page not found',
          path: '',
          component: () => import('@/views/error/Error404.vue'),
        },
      ],
    },
  ];
};

export default new Router({
  base: process.env.BASE_URL,
  routes: routes(),
});
