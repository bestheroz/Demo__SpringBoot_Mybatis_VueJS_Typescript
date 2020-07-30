package com.github.bestheroz.sample.api.entity;

import lombok.Data;

import java.time.Instant;

@Data
public abstract class AbstractCreatedUpdateEntity {
    protected String createdBy;
    protected Instant created;
    protected String updatedBy;
    protected Instant updated;
}
