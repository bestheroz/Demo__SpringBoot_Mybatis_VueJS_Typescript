import store from "@/store";
import { DateTime, SelectItem } from "@/definitions/types";
import dayjs from "dayjs";
import { truncate } from "lodash-es";
import envs from "@/constants/envs";

export function getAdminNm(value: number | undefined | null): string {
  const find: SelectItem<number> = store.getters.adminCodes.find(
    (value1: SelectItem<number>) => value1.value === value,
  );
  return find?.text ?? value ?? "-";
}

export function formatDatetime(value: DateTime | undefined | null): string {
  if (value === undefined || value === null || value === "") {
    return "-";
  }
  return dayjs(value).format("YYYY-MM-DD HH:mm:ss");
}

export function formatDate(value: DateTime | undefined | null): string {
  if (value === undefined || value === null || value === "") {
    return "-";
  }
  return dayjs(value).format("YYYY-MM-DD");
}

export function getEllipseText(
  value: string | null | undefined,
  length: number,
): string {
  if (value === undefined || value === null || value === "") {
    return "-";
  }
  return truncate(value, {
    length: length,
  });
}

export function getSwitchLabel(
  yn: boolean,
  text = ["사용", "사용안함"],
): string {
  return yn ? text[0] : text[1];
}
export function getImageUrl(path: string): string {
  return `${envs.FILE_API_HOST.substring(
    0,
    envs.FILE_API_HOST.length - 1,
  )}${path}`;
}
