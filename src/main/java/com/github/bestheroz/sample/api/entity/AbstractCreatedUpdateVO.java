package com.github.bestheroz.sample.api.entity;

import com.github.bestheroz.standard.common.util.SessionUtils;
import lombok.Data;
import org.joda.time.LocalDateTime;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

@Data
@MappedSuperclass
public abstract class AbstractCreatedUpdateVO {
    @NotNull
    private String createdBy;
    @NotNull
    private LocalDateTime created;
    @NotNull
    private String updatedBy;
    @NotNull
    private LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        this.updated = this.created = LocalDateTime.now();
        if (SessionUtils.isLoggedIn()) {
            this.updatedBy = this.createdBy = SessionUtils.getId();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = LocalDateTime.now();
        if (SessionUtils.isLoggedIn()) {
            this.updatedBy = SessionUtils.getId();
        }
    }
}
