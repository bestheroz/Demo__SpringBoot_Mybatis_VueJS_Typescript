package com.github.bestheroz.sample.api.entity;

import com.github.bestheroz.standard.common.util.AuthenticationUtils;
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
    protected String createdBy;
    @NotNull
    protected LocalDateTime created;
    @NotNull
    protected String updatedBy;
    @NotNull
    protected LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        this.updated = this.created = LocalDateTime.now();
        if (AuthenticationUtils.isLoggedIn()) {
            this.updatedBy = this.createdBy = AuthenticationUtils.getUserName();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = LocalDateTime.now();
        if (AuthenticationUtils.isLoggedIn()) {
            this.updatedBy = AuthenticationUtils.getUserName();
        }
    }
}
