import Vue from 'vue';
import Router from 'vue-router';
import HelloWorld from '@/views/HelloWorld.vue';

Vue.use(Router);

export default new Router({
  // mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: `/`,
      name: `HelloWorld`,
      component: HelloWorld,
    },
    {
      path: `/manage/member`,
      name: `ManageMember`,
      component: () =>
        import(/* webpackChunkName: "about" */ `@/views/manage/member/ManageMember.vue`),
    },
  ],
  scrollBehavior(to, from, savedPoisition) {
    return savedPoisition || { x: 0, y: 0 };
  },
});
