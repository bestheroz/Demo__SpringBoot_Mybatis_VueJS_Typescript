import envs from "@/constants/envs";
import router from "@/router";
import { axiosInstance } from "@/utils/apis";

export function logout(): void {
  try {
    axiosInstance.delete(`${envs.API_HOST}api/auth/logout`).then();
  } catch (e) {
    console.error(e);
  }
  router.push("/login").then();
}

export function refreshToken(accessToken: string): void {
  window.localStorage.setItem("accessToken", accessToken);
}

export function saveToken(token: {
  accessToken: string;
  refreshToken: string;
}): void {
  window.localStorage.setItem("accessToken", token.accessToken);
  window.localStorage.setItem("refreshToken", token.refreshToken);
}

export async function needLogin(): Promise<void> {
  if (router.currentRoute.path !== "/login") {
    await router.push("/login?login=need");
  }
}
