export interface Member {
  memberId?: string;
  memberPw?: string;
  memberName?: string;
  memberType?: string;
  loginFailCnt?: number;
  token?: string;
  closeTf?: boolean;
  expired?: Date;
  createdBy?: string;
  created?: Date;
  updatedBy?: string;
  updated?: Date;
}
