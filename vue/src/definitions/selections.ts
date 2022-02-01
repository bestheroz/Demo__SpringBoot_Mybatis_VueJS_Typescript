import { SelectItem } from "@/definitions/types";

export const ROLE_AUTHORITY_TYPE = {
  WRITE: "WRITE",
  DELETE: "DELETE",
  VIEW: "VIEW",
  EXCEL: "EXCEL",
};
export const MENU_TYPE = {
  GROUP: "GROUP",
  PAGE: "PAGE",
  NEW_TAB: "NEW_TAB",
};
export const MenuTypes: SelectItem[] = [
  { value: MENU_TYPE.GROUP, text: "페이지 그룹" },
  { value: MENU_TYPE.PAGE, text: "페이지" },
  { value: MENU_TYPE.NEW_TAB, text: "새탭" },
];
export const BooleanTypes: SelectItem<boolean>[] = [
  { value: true, text: "예" },
  { value: false, text: "아니요" },
];
export const APP_TYPE = {
  APP_ANDROID: "APP_ANDROID",
  APP_IOS: "APP_IOS",
  WEB_MOBILE: "WEB_MOBILE",
  WEB_PC: "WEB_PC",
};
export const AppTypes: SelectItem[] = [
  { value: APP_TYPE.APP_ANDROID, text: "안드로이드 앱" },
  { value: APP_TYPE.APP_IOS, text: "iOS 앱" },
  { value: APP_TYPE.WEB_MOBILE, text: "모바일 웹" },
  { value: APP_TYPE.WEB_PC, text: "PC 웹" },
];
export const TERMS_TYPE = {
  USE: "USE",
  PERSONAL: "PERSONAL",
  LOCATION: "LOCATION",
  PROMOTION: "PROMOTION",
};
export const TermsTypes: SelectItem[] = [
  { value: TERMS_TYPE.USE, text: "이용약관 동의" },
  { value: TERMS_TYPE.PERSONAL, text: "개인정보 수집 및 이용동의" },
  { value: TERMS_TYPE.LOCATION, text: "위치정보 이용약관 동의" },
  { value: TERMS_TYPE.PROMOTION, text: "프로모션 정보 수신 동의" },
];

export const SERVICE = {
  CUBEWIZ: "CUBEWIZ",
} as const;
