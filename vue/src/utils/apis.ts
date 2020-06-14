import axios, { AxiosError } from 'axios';
import _ from 'lodash';
import router from '@/router';
import store from '@/store';
import { alertError, alertSuccess } from '@/utils/alerts';
import envs from '@/constants/envs';
import Vue from 'vue';

export const axiosInstance = axios.create({
  baseURL: envs.API_HOST,
  headers: {
    contentType: 'application/json',
    Authorization: Vue.$storage.get('accessToken'),
  },
});

axiosInstance.interceptors.request.use(
  function (config) {
    config.headers.Authorization = Vue.$storage.get('accessToken');
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  },
);
axiosInstance.interceptors.response.use(
  function (response) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    store.commit('timer');
    return response;
  },
  async function (error: AxiosError) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    console.log('에러일 경우', error.config);
    const errorAPI = error.config;
    if (error.response && [401, 403].includes(error.response.data.status)) {
      // if (['F004', 'F011'].includes(response.code)) {
      await router.push(`/login?login=need`);
      // }
      return axios(errorAPI);
    }
    if (envs.ENV !== 'production') {
      console.error('에러난다. 빨리 고치자');
      await router.push('/error/500');
    }
    if (error.response && error.response.data && error.response.data.message) {
      return {
        code: error.response.data.code,
        message: error.response.data.message,
      };
    } else {
      return {
        code: 'F000',
        message: error.message,
      };
    }
    // return Promise.reject(error);
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

export async function getDataApi<T>(
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
  if (Vue.$storage.has(`code__${codeGroup}`)) {
    return Vue.$storage.get(`code__${codeGroup}`);
  } else {
    try {
      const response = await axiosInstance.get<ApiDataResult<SelectItem[]>>(
        `api/codes/${codeGroup}`,
      );
      const result = response.data.data || [];
      if (result.length > 0) {
        Vue.$storage.set(`code__${codeGroup}`, result);
      }
      // @ts-ignore
      return result;
    } catch (error) {
      // console.warn(getErrorResult(error).message);
      return [];
    }
  }
}

export async function getVariableApi<String>(
  variable: string,
): Promise<string | null> {
  if (Vue.$storage.has(`variable__${variable}`)) {
    return Vue.$storage.get(`variable__${variable}`);
  } else {
    try {
      const response = await axiosInstance.get<ApiDataResult<string>>(
        `api/variables/${variable}`,
      );
      const result = response.data.data;
      if (result) {
        Vue.$storage.set(`variable__${variable}`, result);
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
        Authorization: Vue.$storage.get('accessToken'),
        'Content-Type':
          'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      },
    })
    .get<any>(url);
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
  store.commit('timer');
  return response.data;
}
