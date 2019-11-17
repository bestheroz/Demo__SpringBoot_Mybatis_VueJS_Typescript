import { AxiosError } from 'axios';
import _ from 'lodash';
import router from '@/router';
import store from '@/store';

export interface ApiDataResult<T> {
  responseCode: string;
  responseData?: T;
  responseMessage: string;
}

function getErrorResult<T>(error: AxiosError): ApiDataResult<T> {
  console.error(error);
  // console.error(error.config);
  return {
    responseCode: 'F000',
    responseMessage: error.message,
  };
}

export async function getDataApi<T>(
  url: String,
  id: number = 0,
): Promise<ApiDataResult<T>> {
  try {
    const response = await store.state.axiosInstance.get<ApiDataResult<T>>(
      `${url}${id}/`,
    );
    await logoutChecker(response.data);
    return response.data;
  } catch (error) {
    return getErrorResult(error);
  }
}

export async function postDataApi<T>(
  url: String,
  data: T,
  vueForAutoToast: any = undefined,
): Promise<ApiDataResult<T>> {
  try {
    const response = await store.state.axiosInstance.post<ApiDataResult<T>>(
      `${url}`,
      data,
    );
    await logoutChecker(response.data);
    // response.status === 201
    vueForAutoToast && toastResponseMessage(vueForAutoToast, response.data);
    return response.data;
  } catch (error) {
    return getErrorResult(error);
  }
}

export async function putDataApi<T>(
  url: String,
  data: T,
  key: String | Number,
  vueForAutoToast: any = undefined,
): Promise<ApiDataResult<T>> {
  try {
    const response = await store.state.axiosInstance.put<ApiDataResult<T>>(
      `${url}${key}/`,
      data,
    );
    await logoutChecker(response.data);
    // response.status === 200
    vueForAutoToast && toastResponseMessage(vueForAutoToast, response.data);
    return response.data;
  } catch (error) {
    return getErrorResult(error);
  }
}

export async function patchDataApi<T>(
  url: String,
  data: T,
  key: String | Number,
  vueForAutoToast: any = undefined,
): Promise<ApiDataResult<T>> {
  try {
    const response = await store.state.axiosInstance.patch<ApiDataResult<T>>(
      `${url}${key}/`,
      data,
    );
    await logoutChecker(response.data);
    // response.status === 200
    vueForAutoToast && toastResponseMessage(vueForAutoToast, response.data);
    return response.data;
  } catch (error) {
    return getErrorResult(error);
  }
}

export async function deleteDataApi<T>(
  url: String,
  key: String | Number,
  vueForAutoToast: any = undefined,
): Promise<ApiDataResult<T>> {
  try {
    const response = await store.state.axiosInstance.delete(`${url}${key}/`);
    await logoutChecker(response.data);
    // response.status === 204
    vueForAutoToast && toastResponseMessage(vueForAutoToast, response.data);
    return response.data;
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
): Promise<ApiDataResult<T>> {
  try {
    const response = await store.state.axiosInstance.get<ApiDataResult<T>>(
      `${url}`,
    );
    await logoutChecker(response.data);
    return response.data;
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
    await logoutChecker(response.data);
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

async function logoutChecker(response: ApiDataResult<any>) {
  if (response.responseCode === 'F004') {
    await router.push(`/login?need=login`);
  }
  return false;
}
