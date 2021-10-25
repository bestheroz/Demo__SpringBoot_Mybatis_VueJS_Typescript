import router from "@/router";
import axios from "axios";
import envs from "@/constants/envs";
import { ApiDataResult, axiosInstance, deleteApi, getApi } from "@/utils/apis";
import { AdminConfig, RoleMenuMap } from "@/definitions/models";
import { Drawer, SelectItem } from "@/definitions/types";
import { defaultAdminConfig } from "@/definitions/defaults";
import { cloneDeep, debounce } from "lodash-es";
import store from "@/store";
import { toastError } from "@/utils/alerts";

export async function goSignInPage(): Promise<void> {
  if (router.currentRoute.path !== "/sign-in") {
    await router.replace("/sign-in");
  }
}

export async function getAccessToken(
  accessToken: string,
  refreshToken: string,
): Promise<string | undefined> {
  try {
    const response = await axios
      .create({
        baseURL: envs.API_HOST,
        headers: {
          contentType: "application/json",
          Authorization: accessToken,
          AuthorizationR: refreshToken,
        },
      })
      .get<ApiDataResult<string>>("api/sign-in/refresh-token");
    return response.data.data;
  } catch (e: unknown) {
    if (axios.isAxiosError(e)) {
      const statusCode = e.response?.status || 500;
      if (statusCode === 401 || e.message === "Invalid token specified!") {
        await goSignInPage();
      } else if ([403, 404, 500].includes(statusCode)) {
        toastError(e.message);
      } else {
        console.warn(`Missing Status Code: ${statusCode}`);
        toastError(e.message);
      }
    } else {
      console.error(e);
    }
  }
}

const uploadConfigHandler = debounce((config: AdminConfig) => {
  try {
    axiosInstance
      .post<AdminConfig, ApiDataResult<AdminConfig>>("/api/mine/config", config)
      .then();
  } catch (e) {
    console.error(e);
  }
}, 1000);
export function uploadConfig(config: AdminConfig): void {
  uploadConfigHandler(config);
}

export async function getYourConfig(): Promise<AdminConfig> {
  const response = await getApi<AdminConfig>("mine/config");
  return response.data.primaryColor ? response.data : defaultAdminConfig();
}

export async function getAdminCodes(): Promise<SelectItem<number>[]> {
  const response = await getApi<SelectItem<number>[]>("admins/codes/");
  return response.data || [];
}

export function signOut(): void {
  try {
    deleteApi("sign-out", false).then();
  } catch (e) {
    console.error(e);
  }
  router.replace("/sign-in").then();
}

export function getCurrentAuthority(
  path = router.currentRoute.fullPath,
): RoleMenuMap {
  return store.getters.flatAuthorities.find(
    (roleMenuMap: RoleMenuMap) => roleMenuMap.menu.url === path,
  );
}

export function getDrawersFromRoleMenuMaps(
  roleMenuMaps: RoleMenuMap[],
): Drawer[] {
  return (roleMenuMaps || []).map((roleMenuMap) => {
    return {
      id: roleMenuMap.menu.id || 0,
      name: roleMenuMap.menu.name,
      type: roleMenuMap.menu.type,
      icon: roleMenuMap.menu.icon,
      url: roleMenuMap.menu.url,
      children: getDrawersFromRoleMenuMaps(roleMenuMap.children || []),
    };
  });
}
export function getFlatRoleMenuMaps(
  roleMenuMaps: RoleMenuMap[],
): RoleMenuMap[] {
  if (!roleMenuMaps || roleMenuMaps.length === 0) {
    return [];
  }
  const cloneRoleMenuMaps = cloneDeep(roleMenuMaps);
  let result: RoleMenuMap[] = [];
  for (const roleMenuMap of cloneRoleMenuMaps) {
    if ((roleMenuMap.children || []).length > 0) {
      result = [...result, ...getFlatRoleMenuMaps(roleMenuMap.children || [])];
      roleMenuMap.children = [];
    }
    result = [...result, roleMenuMap];
  }
  return result;
}
