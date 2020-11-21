package com.github.bestheroz.demo.api.entity;

import java.time.Instant;
import lombok.Data;

@Data
public abstract class AbstractCreatedUpdateEntity {

  protected String createdBy;
  protected Instant created;
  protected String updatedBy;
  protected Instant updated;
}
