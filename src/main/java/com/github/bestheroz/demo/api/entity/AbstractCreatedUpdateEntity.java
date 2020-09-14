package com.github.bestheroz.demo.api.entity;

import lombok.Data;

import java.time.Instant;

@Data
public abstract class AbstractCreatedUpdateEntity {
    protected String createdBy;
    protected Instant created;
    protected String updatedBy;
    protected Instant updated;
}
