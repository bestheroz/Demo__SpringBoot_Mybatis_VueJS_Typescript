export interface Member {
  memberId?: string;
  memberPw?: string;
  memberNm?: string;
  memberTyp?: string;
  loginFailCnt?: number;
  isClosed?: boolean;
  expireDt?: Date;
  regMemberId?: string;
  regDt?: Date;
  updMemberId?: string;
  updDt?: Date;
}
