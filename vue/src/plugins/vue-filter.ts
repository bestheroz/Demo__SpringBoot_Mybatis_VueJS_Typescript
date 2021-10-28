import Vue from "vue";
import { DateTime, SelectItem } from "@/definitions/types";
import { getTextOfSelectItem } from "@/utils/codes";
import {
  formatDate,
  formatDatetime,
  getEllipseText,
  getAdminNm,
  getSwitchLabel,
} from "@/utils/formatter";

Vue.filter(
  "formatDatetime",
  function (value: DateTime | undefined | null): string {
    return formatDatetime(value);
  },
);
Vue.filter("formatDate", function (value: DateTime | undefined | null): string {
  return formatDate(value);
});
Vue.filter(
  "formatAdminNm",
  function (value: number | undefined | null): string {
    return getAdminNm(value);
  },
);
Vue.filter(
  "getCodeText",
  function (
    value: string,
    codes: SelectItem<string | number>[] | null,
  ): string {
    return getTextOfSelectItem(codes, value);
  },
);
Vue.filter("getEllipseText", function (text: string, length: number): string {
  return getEllipseText(text, length);
});
Vue.filter("getSwitchLabel", function (yn: boolean, text?: string[]): string {
  return getSwitchLabel(yn, text);
});
