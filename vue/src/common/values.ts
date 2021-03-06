import {
  TableCodeEntity,
  TableCodeGroupEntity,
  TableMemberEntity,
  TableMemberMenuEntity,
  TableMenuEntity,
} from "@/common/types";
import dayjs from "dayjs";
export function defaultUser(): {
  id: string | null;
  name: string | null;
  authority: string | null;
  theme: string;
} {
  return { id: null, name: null, authority: null, theme: "light" };
}

export function defaultTableMemberEntity(): TableMemberEntity {
  return {
    name: null,
    loginFailCnt: null,
    expired: dayjs().add(1, "years").endOf("day"),
    available: null,
    theme: null,
    authority: null,
    token: null,
  };
}

export function defaultTableMenuEntity(): TableMenuEntity {
  return {
    name: null,
    type: "G",
    parentId: null,
    authority: null,
    displayOrder: null,
    icon: null,
    url: null,
  };
}
export function defaultTableCodeGroupEntity(): TableCodeGroupEntity {
  return {
    codeGroup: null,
    name: null,
  };
}
export function defaultTableCodeEntity(): TableCodeEntity {
  return {
    codeGroup: null,
    code: null,
    name: null,
    available: null,
    displayOrder: null,
    authority: null,
  };
}
export function defaultTableMemberMenuEntity(): TableMemberMenuEntity {
  return {
    authority: null,
    id: null,
    name: null,
    type: null,
    parentId: null,
    displayOrder: null,
    icon: null,
    url: null,
  };
}
