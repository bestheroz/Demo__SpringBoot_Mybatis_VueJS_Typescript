import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
  // mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: `/`,
      name: `Index`,
      component: () => import('@/views/Index.vue'),
    },
    {
      path: `/login`,
      name: `Login`,
      component: () => import('@/views/login/Login.vue'),
    },
    {
      path: `/manage/member`,
      name: `ManageMember`,
      component: () => import(`@/views/manage/member/ManageMember.vue`),
    },
  ],
  scrollBehavior(to, from, savedPoisition) {
    return savedPoisition || { x: 0, y: 0 };
  },
});
