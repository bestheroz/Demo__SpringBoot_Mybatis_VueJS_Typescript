package com.github.bestheroz.sample.api.entity;

import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import lombok.Data;
import org.joda.time.LocalDateTime;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Data
@MappedSuperclass
public abstract class AbstractCreatedUpdateVO {
    protected String createdBy;
    protected LocalDateTime created;
    protected String updatedBy;
    protected LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        this.updated = this.created = LocalDateTime.now();
        if (AuthenticationUtils.isLoggedIn()) {
            this.updatedBy = this.createdBy = AuthenticationUtils.getUserPk();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = LocalDateTime.now();
        if (AuthenticationUtils.isLoggedIn()) {
            this.updatedBy = AuthenticationUtils.getUserPk();
        }
    }
}
