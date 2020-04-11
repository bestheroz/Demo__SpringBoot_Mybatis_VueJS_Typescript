package com.github.bestheroz.sample.api.entity;

import com.github.bestheroz.standard.common.util.SessionUtils;
import lombok.Data;
import org.joda.time.LocalDateTime;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Data
@MappedSuperclass
public abstract class AbstractCreatedUpdateVO {
    private String createdBy;
    private LocalDateTime created;
    private String updatedBy;
    private LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        this.updated = this.created = LocalDateTime.now();
        this.updatedBy = this.createdBy = SessionUtils.getMemberId();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = LocalDateTime.now();
        this.updatedBy = SessionUtils.getMemberId();
    }
}
