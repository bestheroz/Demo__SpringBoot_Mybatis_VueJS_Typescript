import Vue from "vue";
import Router, { Route } from "vue-router";
import { NavigationGuardNext, RouteConfig } from "vue-router/types/router";
import store from "@/store";
import { goSignInPage } from "@/utils/commands";

Vue.use(Router);

const requireAuth =
  () => async (_to: Route, _from: Route, next: NavigationGuardNext) => {
    if (store.getters.loggedIn) {
      return next();
    }
    return goSignInPage();
  };

const routes = (): RouteConfig[] => {
  const management: RouteConfig[] = [
    {
      path: "/management/code",
      beforeEnter: requireAuth(),
      component: () => import("@/views/management/code/CodePage.vue"),
    },
    {
      path: "/management/menu",
      beforeEnter: requireAuth(),
      component: () => import("@/views/management/menu/MenuPage.vue"),
    },
    {
      path: "/management/role",
      beforeEnter: requireAuth(),
      component: () => import("@/views/management/role/RolePage.vue"),
    },
    {
      path: "/management/role-menu",
      beforeEnter: requireAuth(),
      component: () => import("@/views/management/role/menu/RoleMenuPage.vue"),
    },
    {
      path: "/management/admin",
      beforeEnter: requireAuth(),
      component: () => import("@/views/management/admin/AdminPage.vue"),
    },
  ];
  const developer: RouteConfig[] = [
    {
      path: "/developer/picker",
      beforeEnter: requireAuth(),
      component: () => import("@/views/developer/picker/PickerPage.vue"),
    },
    {
      path: "/developer/file-uploader",
      beforeEnter: requireAuth(),
      component: () =>
        import("@/views/developer/file/upload/FileUploaderPage.vue"),
    },
    {
      path: "/developer/text-editor",
      beforeEnter: requireAuth(),
      component: () => import("@/views/developer/editor/TextEditorPage.vue"),
    },
  ];
  const error: RouteConfig[] = [
    {
      path: "/error",
      component: () => import("@/views/error/UnexpectedPage.vue"),
      meta: {
        layout: "error",
      },
    },
    {
      path: "/error/404",
      component: () => import("@/views/error/NotFoundPage.vue"),
      meta: {
        layout: "error",
      },
    },
  ];

  return [
    {
      path: "/",
      beforeEnter: requireAuth(),
      redirect: "/blank",
    },
    {
      path: "/blank",
      beforeEnter: requireAuth(),
      component: () => import("@/views/Home.vue"),
    },
    {
      path: "/sign-in",
      component: () => import("@/views/sign/in/SignIn.vue"),
      meta: {
        layout: "auth",
      },
    },
    ...management,
    ...developer,
    ...error,
    {
      path: "*",
      redirect: "/error/404",
    },
  ];
};

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  scrollBehavior(_to: Route, _from: Route, savedPosition) {
    return savedPosition || { x: 0, y: 0 };
  },
  routes: routes(),
});
