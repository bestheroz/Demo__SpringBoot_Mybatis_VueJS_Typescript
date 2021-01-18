import axios, { AxiosError } from "axios";
import envs from "@/constants/envs";
import { errorPage } from "@/utils/errors";
import store from "@/store";
import { toastError, toastSuccess } from "@/utils/alerts";

export const axiosInstance = axios.create({
  baseURL: envs.API_HOST,
  headers: {
    contentType: "application/json",
    Authorization: store.getters.accessToken,
  },
});

axiosInstance.interceptors.request.use(
  function (config) {
    config.headers.Authorization = store.getters.accessToken;
    return config;
  },
  function (error) {
    alertAxiosError(error);
    return Promise.reject(error);
  },
);
axiosInstance.interceptors.response.use(
  function (response) {
    return response;
  },
  async function (error: AxiosError) {
    if (error?.message === "Network Error") {
      toastError("Service Unavailable");
      return;
    }
    if (error?.response) {
      if (error.response.headers.refreshtoken === "must") {
        const refreshToken = await apiRefreshToken(error);
        return refreshToken && axios.request(refreshToken.config);
      }
      if ([400, 401].includes(error.response?.status)) {
        await store.dispatch("needLogin");
        return;
      } else if ([403, 404, 500].includes(error.response.status)) {
        await errorPage(error.response.status);
        return;
      }
    }
    if (process.env.NODE_ENV === "development") {
      alertAxiosError(error);
    }
    console.warn(error);
    return Promise.reject(error);
  },
);

export interface ApiDataResult<T> {
  code: string;
  data?: T;
  message: string;
  paginationTotalLength?: number;
}

export async function getApi<T>(url: string): Promise<ApiDataResult<T>> {
  const response = await axiosInstance.get<ApiDataResult<T>>(`api/${url}`);
  return response?.data;
}

export async function postApi<T>(
  url: string,
  data: T,
  alert = true,
): Promise<ApiDataResult<T>> {
  const response = await axiosInstance.post<ApiDataResult<T>>(
    `api/${url}`,
    data,
  );
  // response.status === 201
  if (alert) {
    alertResponseMessage(response?.data);
  }
  return response?.data;
}

export async function putApi<T>(
  url: string,
  data: T,
  alert = true,
): Promise<ApiDataResult<T>> {
  const response = await axiosInstance.put<ApiDataResult<T>>(
    `api/${url}`,
    data,
  );
  // response.status === 200
  if (alert) {
    alertResponseMessage(response?.data);
  }
  return response?.data;
}

export async function patchApi<T>(
  url: string,
  data: T,
  alert = true,
): Promise<ApiDataResult<T>> {
  const response = await axiosInstance.patch<ApiDataResult<T>>(
    `api/${url}`,
    data,
  );
  // response.status === 200
  if (alert) {
    alertResponseMessage(response?.data);
  }
  return response?.data;
}

export async function deleteApi<T>(
  url: string,
  alert = true,
): Promise<ApiDataResult<T>> {
  const response = await axiosInstance.delete(`api/${url}`);
  // response.status === 204
  if (alert) {
    alertResponseMessage(response?.data);
  }
  return response?.data;
}

export async function getCodesApi<SelectItem>(
  codeGroup: string,
): Promise<SelectItem[]> {
  const item = window.localStorage.getItem(`code__${codeGroup}`);
  if (item) {
    return JSON.parse(item);
  } else {
    try {
      const response = await axiosInstance.get<ApiDataResult<SelectItem[]>>(
        `api/codes/${codeGroup}`,
      );
      const result = response?.data?.data || [];
      if (result.length > 0) {
        window.localStorage.setItem(
          `code__${codeGroup}`,
          JSON.stringify(result),
        );
      }
      return result;
    } catch (error) {
      // console.warn(getErrorResult(error).message);
      return [];
    }
  }
}

export async function getVariableApi<T = string>(
  variable: string,
): Promise<T | null> {
  const item = window.localStorage.getItem(`variable__${variable}`);
  if (item) {
    return JSON.parse(item);
  } else {
    try {
      const response = await axiosInstance.get<ApiDataResult<T>>(
        `api/variables/${variable}`,
      );
      const result = response?.data?.data || null;
      if (result) {
        window.localStorage.setItem(
          `variable__${variable}`,
          JSON.stringify(result),
        );
      }
      return result;
    } catch (error) {
      // console.warn(getErrorResult(error).message);
      return null;
    }
  }
}

function alertResponseMessage(data: ApiDataResult<unknown>): void {
  if (data.code.startsWith("S")) {
    toastSuccess(data.message);
  } else {
    toastError(data.message);
  }
}

const axiosInstanceForExcel = axios.create({
  baseURL: envs.API_HOST,
  responseType: "blob",
  headers: {
    Authorization: store.getters.accessToken,
    "Content-Type":
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
  },
});

axiosInstanceForExcel.interceptors.request.use(
  function (config) {
    config.headers.Authorization = store.getters.accessToken;
    return config;
  },
  function (error) {
    alertAxiosError(error);
    return Promise.reject(error);
  },
);

axiosInstanceForExcel.interceptors.response.use(
  function (response) {
    return response;
  },
  async function (error: AxiosError) {
    if (error?.message === "Network Error") {
      toastError("Service Unavailable");
      return;
    }
    if (error?.response) {
      if ([400, 401].includes(error.response?.status)) {
        if (error.response.headers.refreshtoken === "must") {
          const refreshToken = await apiRefreshToken(error);
          return refreshToken && axios.request(refreshToken.config);
        }
        await store.dispatch("needLogin");
        return;
      } else if (
        error.response.status === 404 &&
        error.response.headers.refreshtoken === "must"
      ) {
        // 로컬환경때문에 추가
        const refreshToken = await apiRefreshToken(error);
        return refreshToken && axios.request(refreshToken.config);
      }
      // 404는 그냥... 로그보면서 판단하자
      if ([403, 500].includes(error.response.status)) {
        await errorPage(error.response.status);
        return;
      }
    }
    alertAxiosError(error);
    return Promise.reject(error);
  },
);

export async function getExcelApi(url: string): Promise<void> {
  const response = await axiosInstanceForExcel.get<Blob>(`api/${url}`);
  const newUrl = window.URL.createObjectURL(
    new Blob([response?.data], { type: response.headers["content-type"] }),
  );
  const tempLink = document.createElement("a");
  tempLink.style.display = "none";
  tempLink.href = newUrl;
  tempLink.setAttribute(
    "download",
    response.headers["content-disposition"]
      .split("=")
      .pop()
      .split(";")
      .join(""),
  );
  document.body.appendChild(tempLink);
  tempLink.click();
  document.body.removeChild(tempLink);
  window.URL.revokeObjectURL(newUrl);
}

async function apiRefreshToken(error: AxiosError) {
  try {
    await store.dispatch("reissueAccessToken");
  } catch (e) {
    if (e.response?.status === 401) {
      await store.dispatch("needLogin");
      return;
    }
  }
  error.config.headers.Authorization = store.getters.accessToken;
  error.config.headers.AuthorizationR = store.getters.refreshToken;
  return error;
}

export function alertAxiosError(e: AxiosError): void {
  e.response && toastError(e?.response?.data?.message || "System Error");
}
