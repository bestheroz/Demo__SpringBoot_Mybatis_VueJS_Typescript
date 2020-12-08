import { SelectItem } from "@/common/types";

export function getCodes(
  codes: SelectItem[],
): {
  text: string;
  value: string;
}[] {
  return Object.values(codes);
}

export function getText(
  codes: SelectItem[] | null,
  value?: string | null,
  defaultText?: string,
): string {
  const filterElement = (codes || []).find(
    (item) => item.value === (value || "").toString(),
  );
  return filterElement?.text || value || defaultText || "";
}
