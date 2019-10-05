export interface Member {
  memberId?: string;
  memberPw?: string;
  memberName?: string;
  memberType?: string;
  loginFailCnt?: number;
  closeTf?: boolean;
  expired?: Date;
  createdBy?: string;
  created?: Date;
  updatedBy?: string;
  updated?: Date;
}
