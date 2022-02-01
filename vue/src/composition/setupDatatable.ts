import {
  computed,
  reactive,
  toRefs,
  UnwrapRefSimple,
} from "@vue/composition-api";
import { FilterOutput, PageResult, Pagination } from "@/definitions/types";
import qs from "qs";
import { debounce, DebouncedFunc } from "lodash-es";
import { getApi } from "@/utils/apis";
import setupList from "@/composition/setupList";

export default function <T>(url: string) {
  const listPage = setupList<T>(url);

  const state = reactive({
    pagination: {
      page: 1,
      sortBy: ["updated"],
      sortDesc: [true],
      itemsPerPage: 10,
    } as Pagination,
    search: "",
    filterOutput: {} as FilterOutput,
  });
  const computes = {
    queryString: computed((): string =>
      qs.stringify({
        search: state.search,
        ...state.filterOutput,
        ...state.pagination,
      }),
    ),
    fetchList: computed(
      (): DebouncedFunc<() => Promise<void>> =>
        debounce(async () => {
          listPage.items.value = [];
          listPage.totalItems.value = 0;
          listPage.loading.value = true;
          const response = await getApi<PageResult<UnwrapRefSimple<T>>>(
            `${listPage.url.value}?${computes.queryString.value}`,
          );
          listPage.loading.value = false;
          listPage.items.value =
            response.data.content || ([] as UnwrapRefSimple<T>[]);
          listPage.totalItems.value = response.data.totalElements;
        }, 300),
    ),
  };

  const methods = {
    onCreated: (item: T): void => {
      listPage.items.value = [item, ...listPage.items.value].slice(
        0,
        state.pagination.itemsPerPage,
      ) as UnwrapRefSimple<T>[];
      listPage.totalItems.value++;
    },

    onUpdated: (item: T): void => {
      listPage.items.value.splice(
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        listPage.items.value.findIndex((_item) => _item.id === item.id),
        1,
        item as UnwrapRefSimple<T>,
      );
    },
  };

  return { ...listPage, ...toRefs(state), ...computes, ...methods };
}
