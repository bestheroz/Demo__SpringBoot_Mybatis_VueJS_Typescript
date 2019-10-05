package com.github.bestheroz.sample.api.table.samplemembermst;

import org.joda.time.DateTime;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TestTableSampleMemberMstVO implements Serializable {
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberType;
    private Integer loginFailCnt;
    private Boolean closeTf;
    private String token;
    private DateTime expired;
    private String createdBy;
    private DateTime created;
    private String updatedBy;
    private DateTime updated;

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(final String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPw() {
        return this.memberPw;
    }

    public void setMemberPw(final String memberPw) {
        this.memberPw = memberPw;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(final String memberName) {
        this.memberName = memberName;
    }

    public String getMemberType() {
        return this.memberType;
    }

    public void setMemberType(final String memberType) {
        this.memberType = memberType;
    }

    public Integer getLoginFailCnt() {
        return this.loginFailCnt;
    }

    public void setLoginFailCnt(final Integer loginFailCnt) {
        this.loginFailCnt = loginFailCnt;
    }

    public Boolean getCloseTf() {
        return this.closeTf;
    }

    public void setCloseTf(final Boolean closeTf) {
        this.closeTf = closeTf;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public DateTime getExpired() {
        return this.expired;
    }

    public void setExpired(final DateTime expired) {
        this.expired = expired;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getCreated() {
        return this.created;
    }

    public void setCreated(final DateTime created) {
        this.created = created;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public DateTime getUpdated() {
        return this.updated;
    }

    public void setUpdated(final DateTime updated) {
        this.updated = updated;
    }
}
