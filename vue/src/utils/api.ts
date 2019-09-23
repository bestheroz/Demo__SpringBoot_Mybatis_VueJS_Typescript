import axios, { AxiosError } from 'axios';
import { axiosInstance } from '@/main';
import { AlertOptions } from '@/components/alert/common/types';

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
  console.error(error.config);

  if (error.response) {
    // The request was made and the server responded with a status code
    // that falls out of the range of 2xx
    console.error(error);
    return {
      status: error.response.status,
      errorMessage: error.response.data.errorMessage,
    };
  } else if (error.request) {
    // The request was made but no response was received
    // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
    // http.ClientRequest in node.js
    console.error(error.request);
    return {
      status: 400,
      errorMessage: `The request was made but no response was received!`,
    };
  } else {
    // Something happened in setting up the request that triggered an Error
    console.error(`Error`, error.message);
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
    const response = await axiosInstance.get<ApiDataResult<T>>(`${url}${id}/`);
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

export async function createDataApi<T>(
  url: String,
  data: T,
  alertOptions?: AlertOptions,
): Promise<ApiResult<T>> {
  try {
    const response = await axiosInstance.post<ApiDataResult<T>>(`${url}`, data);
    // response.status === 201
    if (alertOptions) {
      alertOptions.result = Object.assign({}, response.data);
    }
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

export async function updateDataApi<T>(
  url: String,
  data: T,
  key: String | Number,
  alertOptions?: AlertOptions,
): Promise<ApiResult<T>> {
  try {
    const response = await axiosInstance.put<ApiDataResult<T>>(
      `${url}${key}/`,
      data,
    );
    // response.status === 200
    if (alertOptions) {
      alertOptions.result = Object.assign({}, response.data);
    }
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
  alertOptions?: AlertOptions,
): Promise<ApiResult<T>> {
  try {
    const response = await axiosInstance.patch<ApiDataResult<T>>(
      `${url}${key}/`,
      data,
    );
    // response.status === 200
    if (alertOptions) {
      alertOptions.result = Object.assign({}, response.data);
    }
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
  data: T,
  key: String | Number,
  alertOptions?: AlertOptions,
): Promise<ApiResult<T>> {
  try {
    const response = await axiosInstance.delete(`${url}${key}/`);
    // response.status === 204
    if (alertOptions) {
      alertOptions.result = Object.assign({}, response.data);
    }
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
    const response = await axiosInstance.get<ApiDataResult<T>>(`${url}`);
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
    const response = await axiosInstance.get<ApiDataResult<SelectItem[]>>(
      `/sample/admin/codedet/${key}`,
    );
    return response.data.responseData || [];
  } catch (error) {
    console.warn(getErrorResult(error).message);
    return [];
  }
}
