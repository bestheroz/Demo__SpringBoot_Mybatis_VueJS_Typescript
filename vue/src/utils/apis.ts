import axios, { AxiosError } from 'axios';
import _ from 'lodash';
import store from '@/store';
import { alertError, alertSuccess, alertWarning } from '@/utils/alerts';
import envs from '@/constants/envs';
import { needLogin, refreshToken } from '@/utils/authentications';
import { errorPage } from '@/utils/errors';

export const axiosInstance = axios.create({
  baseURL: envs.API_HOST,
  headers: {
    contentType: 'application/json',
    Authorization: window.localStorage.getItem('accessToken'),
  },
});

axiosInstance.interceptors.request.use(
  function (config) {
    config.headers.Authorization = window.localStorage.getItem('accessToken');
    return config;
  },
  function (error) {
    alertAxiosError(error);
    return Promise.reject(error);
  },
);
axiosInstance.interceptors.response.use(
  function (response) {
    store.dispatch('resetTimer').then(() => {});
    return response;
  },
  async function (error: AxiosError) {
    if (error.message === 'Network Error') {
      alertWarning('Service Unavailable');
      return;
    }
    if (error.response) {
      if ([400, 401].includes(error.response.status)) {
        if (error.response.headers.refreshtoken === 'must') {
          return axios.request((await apiRefreshToken(error)).config);
        }
        await needLogin();
        return;
      } else if (
        error.response.status === 404 &&
        error.response.headers.refreshtoken === 'must'
      ) {
        // 로컬환경때문에 추가
        return axios.request((await apiRefreshToken(error)).config);
      }
      if ([403, 404, 500].includes(error.response.status)) {
        await errorPage(error.response.status);
        return;
      }
    }
    alertAxiosError(error);
    return Promise.reject(error);
  },
);

export interface ApiDataResult<T> {
  code: string;
  data?: T;
  message: string;
  paginationTotalLength?: number;
}

export interface requestKey {
  key: string | number;
  key2: string | number;
  key3?: string;
}

export async function getListApi<T>(url: string): Promise<ApiDataResult<T>> {
  const response = await axiosInstance.get<ApiDataResult<T>>(`api/${url}`);
  return response.data;
}

export async function getItemApi<T>(
  url: string,
  id?: number | undefined,
): Promise<ApiDataResult<T>> {
  const response = await axiosInstance.get<ApiDataResult<T>>(
    `api/${url}${id || ''}`,
  );
  return response.data;
}

export async function getApi<T>(url: string): Promise<ApiDataResult<T>> {
  const response = await axiosInstance.get<ApiDataResult<T>>(`api/${url}`);
  return response.data;
}

export async function postDataApi<T>(
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
    alertResponseMessage(response.data);
  }
  return response.data;
}

export async function putDataApi<T>(
  url: string,
  data: T,
  key: string | number | requestKey,
  alert = true,
): Promise<ApiDataResult<T>> {
  const response = await axiosInstance.put<ApiDataResult<T>>(
    `api/${url}${await makeUrlKey(key)}`,
    data,
  );
  // response.status === 200
  if (alert) {
    alertResponseMessage(response.data);
  }
  return response.data;
}

export async function patchDataApi<T>(
  url: string,
  data: T,
  key: string | number | requestKey,
  alert = true,
): Promise<ApiDataResult<T>> {
  const response = await axiosInstance.patch<ApiDataResult<T>>(
    `api/${url}${await makeUrlKey(key)}`,
    data,
  );
  // response.status === 200
  if (alert) {
    alertResponseMessage(response.data);
  }
  return response.data;
}

export async function deleteDataApi<T>(
  url: string,
  key: string | number | requestKey,
  alert = true,
): Promise<ApiDataResult<T>> {
  const response = await axiosInstance.delete(
    `api/${url}${await makeUrlKey(key)}`,
  );
  // response.status === 204
  if (alert) {
    alertResponseMessage(response.data);
  }
  return response.data;
}

export async function getCodeListApi<SelectItem>(
  codeGroup: string,
): Promise<SelectItem[]> {
  if (window.localStorage.getItem(`code__${codeGroup}`)) {
    return JSON.parse(window.localStorage.getItem(`code__${codeGroup}`)!);
  } else {
    try {
      const response = await axiosInstance.get<ApiDataResult<SelectItem[]>>(
        `api/codes/${codeGroup}`,
      );
      if (response && response.data && response.data.data) {
        const result = response.data.data || [];
        if (result.length > 0) {
          window.localStorage.setItem(
            `code__${codeGroup}`,
            JSON.stringify(result),
          );
        }
        // @ts-ignore
        return result;
      } else {
        return [];
      }
    } catch (error) {
      // console.warn(getErrorResult(error).message);
      return [];
    }
  }
}

export async function getVariableApi<T = string>(
  variable: string,
): Promise<T | null> {
  if (window.localStorage.getItem(`variable__${variable}`)) {
    return JSON.parse(window.localStorage.getItem(`variable__${variable}`)!);
  } else {
    try {
      const response = await axiosInstance.get<ApiDataResult<T>>(
        `api/variables/${variable}`,
      );
      const result = response.data.data;
      if (result) {
        window.localStorage.setItem(
          `variable__${variable}`,
          JSON.stringify(result),
        );
      }
      // @ts-ignore
      return result;
    } catch (error) {
      // console.warn(getErrorResult(error).message);
      return null;
    }
  }
}

function alertResponseMessage(data: ApiDataResult<any>): void {
  if (_.startsWith(data.code, `S`)) {
    alertSuccess(data.message);
  } else {
    alertError(data.message);
  }
}

async function makeUrlKey(key: string | number | requestKey) {
  if (typeof key === 'object') {
    let result: string = `${key.key}/`;
    if (key.key2) {
      result = result + `${key.key2}/`;
    }
    if (key.key3) {
      result = result + `${key.key3}/`;
    }
    return result;
  } else {
    return `${key}/`;
  }
}

export async function getExcelApi(url: string): Promise<void> {
  const response = await axios
    .create({
      baseURL: envs.API_HOST || 'http://localhost:8080/',
      responseType: 'blob',
      headers: {
        Authorization: window.localStorage.getItem('accessToken'),
        AuthorizationR: window.localStorage.getItem('refreshToken'),
        'Content-Type':
          'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      },
    })
    .get<any>(`api/${url}`);
  const newUrl = window.URL.createObjectURL(
    new Blob([response.data], { type: response.headers['content-type'] }),
  );
  const tempLink = document.createElement('a');
  tempLink.style.display = 'none';
  tempLink.href = newUrl;
  tempLink.setAttribute(
    'download',
    response.headers['content-disposition']
      .split('=')
      .pop()
      .split(';')
      .join(''),
  );
  document.body.appendChild(tempLink);
  tempLink.click();
  document.body.removeChild(tempLink);
  window.URL.revokeObjectURL(newUrl);
  await store.dispatch('resetTimer');
  return response.data;
}

async function apiRefreshToken(error: AxiosError) {
  if (error.response && error.response.headers.refreshtoken === 'must') {
    try {
      const response = await axios
        .create({
          baseURL: envs.API_HOST,
          headers: {
            contentType: 'application/json',
            Authorization: window.localStorage.getItem('accessToken'),
            AuthorizationR: window.localStorage.getItem('refreshToken'),
          },
        })
        .get('api/auth/refreshToken');
      await refreshToken(response.data.data);
      await store.dispatch('resetTimer');
    } catch (e) {
      if (e.response.status === 401) {
        await needLogin();
      }
    }
    error.config.headers.Authorization = window.localStorage.getItem(
      'accessToken',
    );
    error.config.headers.AuthorizationR = window.localStorage.getItem(
      'refreshToken',
    );
  }
  return error;
}

export function alertAxiosError(e: AxiosError): void {
  e.response && alertError(e.response.data.message || 'System Error');
}
