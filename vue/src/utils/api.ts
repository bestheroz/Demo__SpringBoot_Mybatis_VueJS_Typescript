import { AxiosError } from 'axios';
import _ from 'lodash';
import router from '@/router';
import store from '@/store';

export interface ApiDataResult<T> {
  responseCode: string;
  responseData?: T;
  responseMessage: string;
}

export interface ApiResult<T> {
  status?: number;
  errorMessage?: string;
  code?: string;
  data?: T;
  message?: string;
}

function getErrorResult<T>(error: AxiosError): ApiResult<T> {
  // console.error(error);
  // console.error(error.config);

  if (error.response) {
    // The request was made and the server responded with a status code
    // that falls out of the range of 2xx
    // console.error(error);
    return {
      status: error.response.status,
      errorMessage: error.response.data.errorMessage,
    };
  } else if (error.request) {
    // The request was made but no response was received
    // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
    // http.ClientRequest in node.js
    // console.error(error.request);
    return {
      status: 400,
      errorMessage: `The request was made but no response was received!`,
    };
  } else {
    // Something happened in setting up the request that triggered an Error
    // console.error(`Error`, error.message);
    return {
      status: 400,
      errorMessage: error.message,
    };
  }
}

export async function getDataApi<T>(
  url: String,
  id: number = 0,
): Promise<ApiResult<T>> {
  try {
    const response = await store.state.axiosInstance.get<ApiDataResult<T>>(
      `${url}${id}/`,
    );
    await logoutCheck(response.data);
    return {
      status: response.status,
      code: response.data.responseCode,
      message: response.data.responseMessage,
      data: response.data.responseData,
    };
  } catch (error) {
    return getErrorResult(error);
  }
}

export async function postDataApi<T>(
  url: String,
  data: T,
  vueForAutoToast: any = undefined,
): Promise<ApiResult<T>> {
  try {
    const response = await store.state.axiosInstance.post<ApiDataResult<T>>(
      `${url}`,
      data,
    );
    await logoutCheck(response.data);
    // response.status === 201
    vueForAutoToast && toastResponseMessage(vueForAutoToast, response.data);
    return {
      status: response.status,
      code: response.data.responseCode,
      message: response.data.responseMessage,
      data: response.data.responseData,
    };
  } catch (error) {
    return getErrorResult(error);
  }
}

export async function putDataApi<T>(
  url: String,
  data: T,
  key: String | Number,
  vueForAutoToast: any = undefined,
): Promise<ApiResult<T>> {
  try {
    const response = await store.state.axiosInstance.put<ApiDataResult<T>>(
      `${url}${key}/`,
      data,
    );
    await logoutCheck(response.data);
    // response.status === 200
    vueForAutoToast && toastResponseMessage(vueForAutoToast, response.data);
    return {
      status: response.status,
      code: response.data.responseCode,
      message: response.data.responseMessage,
      data: response.data.responseData,
    };
  } catch (error) {
    return getErrorResult(error);
  }
}

export async function patchDataApi<T>(
  url: String,
  data: T,
  key: String | Number,
  vueForAutoToast: any = undefined,
): Promise<ApiResult<T>> {
  try {
    const response = await store.state.axiosInstance.patch<ApiDataResult<T>>(
      `${url}${key}/`,
      data,
    );
    await logoutCheck(response.data);
    // response.status === 200
    vueForAutoToast && toastResponseMessage(vueForAutoToast, response.data);
    return {
      status: response.status,
      code: response.data.responseCode,
      message: response.data.responseMessage,
      data: response.data.responseData,
    };
  } catch (error) {
    return getErrorResult(error);
  }
}

export async function deleteDataApi<T>(
  url: String,
  key: String | Number,
  vueForAutoToast: any = undefined,
): Promise<ApiResult<T>> {
  try {
    const response = await store.state.axiosInstance.delete(`${url}${key}/`);
    await logoutCheck(response.data);
    // response.status === 204
    vueForAutoToast && toastResponseMessage(vueForAutoToast, response.data);
    return {
      status: response.status,
      code: response.data.responseCode,
      message: response.data.responseMessage,
      data: response.data.responseData,
    };
  } catch (error) {
    return getErrorResult(error);
  }
}

export interface PagingListData<T> {
  results: T[];
  count: number;
}

export interface PagingListParams {
  page?: number;
  sortBy?: string[];
  sortDesc?: boolean[];
  itemsPerPage?: number;
  search?: string;
  filters?: Object;
}

export async function getOnlyListDataApi<T>(
  url: String,
): Promise<ApiResult<T>> {
  try {
    const response = await store.state.axiosInstance.get<ApiDataResult<T>>(
      `${url}`,
    );
    await logoutCheck(response.data);
    return {
      status: response.status,
      code: response.data.responseCode,
      message: response.data.responseMessage,
      data: response.data.responseData,
    };
  } catch (error) {
    return getErrorResult(error);
  }
}

export async function getCodeListDataApi<SelectItem>(
  key: String,
): Promise<SelectItem[]> {
  try {
    const response = await store.state.axiosInstance.get<
      ApiDataResult<SelectItem[]>
    >(`sample/admin/codedet/${key}`);
    await logoutCheck(response.data);
    return response.data.responseData || [];
  } catch (error) {
    // console.warn(getErrorResult(error).message);
    return [];
  }
}

function toastResponseMessage(
  vue: any,
  responseData: ApiDataResult<any>,
): void {
  if (_.startsWith(responseData.responseCode, `S`)) {
    vue.$swal({ title: responseData.responseMessage, type: 'success' });
  } else {
    vue.$swal({ title: responseData.responseMessage, type: 'error' });
  }
}

async function logoutCheck(responseData: ApiDataResult<any>) {
  if (responseData.responseCode === 'F004') {
    await router.push(`/login?need=login`);
  }
  return false;
}
