import Vue from "vue";
import Router, { Route } from "vue-router";
import { NavigationGuardNext } from "vue-router/types/router";
import store from "@/store";

Vue.use(Router);

const requireAuth = () => async (
  _to: Route,
  _from: Route,
  next: NavigationGuardNext,
) => {
  if (store.getters.loggedIn) {
    return next();
  }
  return store.dispatch("needLogin");
};

const routes = () => {
  const admin = [
    {
      path: "code",
      component: () => import("@/views/admin/code/Code.vue"),
    },
    {
      path: "menu",
      component: () => import("@/views/admin/menu/Menu.vue"),
    },
    {
      path: "member-menu",
      component: () => import("@/views/admin/member/menu/MemberMenu.vue"),
    },
    {
      path: "member",
      component: () => import("@/views/admin/member/Member.vue"),
    },
  ];
  const developer = [
    {
      path: "picker",
      component: () => import("@/views/developer/picker/Picker.vue"),
    },
  ];

  return [
    {
      path: "/login",
      component: () => import("@/views/index/IndexNoDrawer.vue"),
      children: [
        {
          name: "Login",
          path: "",
          component: () => import("@/views/login/Login.vue"),
        },
      ],
    },
    {
      path: "/index",
      component: () => import("@/views/index/Index.vue"),
      beforeEnter: requireAuth(),
      children: [
        {
          path: "",
          component: () => import("@/views/Home.vue"),
        },
      ],
    },
    {
      path: "/",
      component: () => import("@/views/Redirect.vue"),
    },
    {
      path: "/admin",
      component: () => import("@/views/index/Index.vue"),
      beforeEnter: requireAuth(),
      children: admin,
    },
    {
      path: "/developer",
      component: () => import("@/views/index/Index.vue"),
      beforeEnter: requireAuth(),
      children: developer,
    },
    {
      path: "/error",
      component: () => import("@/views/index/IndexNoDrawer.vue"),
      children: [
        {
          name: "Error",
          path: "",
          component: () => import("@/views/error/Error500.vue"),
        },
        {
          name: "403 Forbidden",
          path: "403",
          component: () => import("@/views/error/Error403.vue"),
        },
        {
          name: "404 Page not found",
          path: "404",
          component: () => import("@/views/error/Error404.vue"),
        },
      ],
    },
    {
      path: "*",
      component: () => import("@/views/index/IndexNoDrawer.vue"),
      children: [
        {
          path: "",
          component: () => import("@/views/error/Error404.vue"),
        },
      ],
    },
  ];
};

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: routes(),
});
