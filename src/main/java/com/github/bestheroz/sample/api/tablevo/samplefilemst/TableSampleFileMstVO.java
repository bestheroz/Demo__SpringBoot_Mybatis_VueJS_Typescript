package com.github.bestheroz.sample.api.tablevo.samplefilemst;

import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class TableSampleFileMstVO implements Serializable {
    private Integer fileSeq;
    private String fileNm;
    private String fileNmExt;
    private String mimeTyp;
    private Byte[] fileData; // Arrays.copyOf(value, value.length)
    private String createdBy;
    private LocalDateTime created;
    private String updatedBy;
    private LocalDateTime updated;

    public Integer getFileSeq() {
        return this.fileSeq;
    }

    public void setFileSeq(final Integer fileSeq) {
        this.fileSeq = fileSeq;
    }

    public String getFileNm() {
        return this.fileNm;
    }

    public void setFileNm(final String fileNm) {
        this.fileNm = fileNm;
    }

    public String getFileNmExt() {
        return this.fileNmExt;
    }

    public void setFileNmExt(final String fileNmExt) {
        this.fileNmExt = fileNmExt;
    }

    public String getMimeTyp() {
        return this.mimeTyp;
    }

    public void setMimeTyp(final String mimeTyp) {
        this.mimeTyp = mimeTyp;
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

    public LocalDateTime getCreated() {
        return this.created;
    }

    public void setCreated(final LocalDateTime created) {
        this.created = created;
    }

    @SuppressWarnings("unused")
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdated() {
        return this.updated;
    }

    @SuppressWarnings("unused")
    public void setUpdated(final LocalDateTime updated) {
        this.updated = updated;
    }

}
