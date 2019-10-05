package com.github.bestheroz.sample.api.table.samplecodedet;

import org.joda.time.DateTime;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TestTableSampleCodeDetVO implements Serializable {
    private String groupCode;
    private String code;
    private String codeName;
    private Boolean useTf;
    private Integer displayOrder;
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

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getCodeName() {
        return this.codeName;
    }

    public void setCodeName(final String codeName) {
        this.codeName = codeName;
    }

    public Boolean getUseTf() {
        return this.useTf;
    }

    public void setUseTf(final Boolean useTf) {
        this.useTf = useTf;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(final Integer displayOrder) {
        this.displayOrder = displayOrder;
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
