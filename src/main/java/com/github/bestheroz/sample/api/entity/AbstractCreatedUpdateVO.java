package com.github.bestheroz.sample.api.entity;

import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.OffsetDateTime;

@Data
@MappedSuperclass
public abstract class AbstractCreatedUpdateVO {
    protected String createdBy;
    protected OffsetDateTime created;
    protected String updatedBy;
    protected OffsetDateTime updated;

    @PrePersist
    protected void onCreate() {
        this.updated = this.created = OffsetDateTime.now();
        if (AuthenticationUtils.isLoggedIn()) {
            this.updatedBy = this.createdBy = AuthenticationUtils.getUserPk();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = OffsetDateTime.now();
        if (AuthenticationUtils.isLoggedIn()) {
            this.updatedBy = AuthenticationUtils.getUserPk();
        }
    }
}
