package com.github.bestheroz.sample.api.table.samplecodemst;

import org.joda.time.DateTime;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TestTableSampleCodeMstVO implements Serializable {
    private String groupCode;
    private String groupCodeName;
    private String remark1;
    private String createdBy;
    private DateTime created;
    private String updatedBy;
    private DateTime updated;

    public String getGroupCode() {
        return this.groupCode;
    }

    public void setGroupCode(final String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupCodeName() {
        return this.groupCodeName;
    }

    public void setGroupCodeName(final String groupCodeName) {
        this.groupCodeName = groupCodeName;
    }

    public String getRemark1() {
        return this.remark1;
    }

    public void setRemark1(final String remark1) {
        this.remark1 = remark1;
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
