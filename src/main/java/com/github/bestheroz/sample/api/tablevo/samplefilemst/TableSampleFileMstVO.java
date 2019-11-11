package com.github.bestheroz.sample.api.tablevo.samplefilemst;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class TableSampleFileMstVO implements Serializable {
    private static final long serialVersionUID = -5811287711805179661L;
    private Integer fileSeq;
    private String fileName;
    private String fileNameExt;
    private String mimeType;
    private Byte[] fileData; // Arrays.copyOf(value, value.length)
    private String createdBy;
    private LocalDateTime created;
    private String updatedBy;
    private LocalDateTime updated;
}
