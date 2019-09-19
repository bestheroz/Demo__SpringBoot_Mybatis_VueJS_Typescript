// import axios, { AxiosError } from "axios";
import { axiosInstance } from "@/main";
// import qs from "qs";

export interface ApiResult<T> {
  responseCode: string;
  responseMessage: string;
  responseData: T;
}

// function getErrorResult<T>(error: AxiosError): ApiResult<T> {
//   console.error(error.config);
//
//   if (error.response) {
//     // The request was made and the server responded with a status code
//     // that falls out of the range of 2xx
//     console.error(error);
//     return {
//       success: false,
//       status: error.response.status,
//       data: error.response.data,
//       errorMessage: error.response.data.errorMessage
//     };
//   } else if (error.request) {
//     // The request was made but no response was received
//     // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
//     // http.ClientRequest in node.js
//     console.error(error.request);
//     return {
//       success: false,
//       errorMessage: "The request was made but no response was received!"
//     };
//   } else {
//     // Something happened in setting up the request that triggered an Error
//     console.error("Error", error.message);
//     return {
//       success: false,
//       errorMessage: error.message
//     };
//   }
// }

export interface PagingListParams {
  page?: number;
  sortBy?: string[];
  sortDesc?: boolean[];
  itemsPerPage?: number;
  search?: string;
  filters?: Object;
}

export async function getPagingListApi<T>(url: string, pagingListParams : PagingListParams = {
  page:1,
  sortBy : ["id"],
  sortDesc : [true],
  itemsPerPage : 20,
  search : "",
  filters : {}
}): Promise<ApiResult<T>> {
  // const ordering = sortBy
  //   .map((sortBy, index) => `${sortDesc[index] ? "-" : ""}${sortBy}`)
  //   .join();
  //
  // const queryParams = qs.stringify(
  //   {
  //     page: page,
  //     page_size: itemsPerPage,
  //     ordering: ordering,
  //     search: search,
  //     ...filters!
  //   },
  //   { arrayFormat: "repeat" }
  // );

  try {
    // const response = await axiosInstance.get<T>(`${url}?${queryParams}`);
    const response = await axiosInstance.get<ApiResult<T>>(`${url}`);
    return response.data;
  } catch (error) {
    console.error(error);
    throw error;
    // return getErrorResult(error);
  }
}
