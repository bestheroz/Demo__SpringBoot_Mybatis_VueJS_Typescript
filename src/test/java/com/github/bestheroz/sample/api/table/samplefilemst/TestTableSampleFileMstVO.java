package com.github.bestheroz.sample.api.table.samplefilemst;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class TestTableSampleFileMstVO implements Serializable {
    private Integer fileSeq;
    private String fileName;
    private String fileNameExt;
    private String mimeType;
    private Byte[] fileData; // Arrays.copyOf(value, value.length)
    private String createdBy;
    private DateTime created;
    private String updatedBy;
    private DateTime updated;

    public Integer getFileSeq() {
        return this.fileSeq;
    }

    public void setFileSeq(final Integer fileSeq) {
        this.fileSeq = fileSeq;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameExt() {
        return this.fileNameExt;
    }

    public void setFileNameExt(final String fileNameExt) {
        this.fileNameExt = fileNameExt;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    public Byte[] getFileData() {
        return Arrays.copyOf(this.fileData, this.fileData.length);
    }

    public void setFileData(final Byte[] fileData) {
        this.fileData = Arrays.copyOf(fileData, fileData.length);
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
