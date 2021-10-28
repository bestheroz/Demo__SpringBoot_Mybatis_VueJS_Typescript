package com.github.bestheroz.demo.entity;

import java.time.Instant;
import lombok.Data;

@Data
public abstract class AbstractCreatedUpdate {
  protected Long createdBy;
  protected Instant created;
  protected Long updatedBy;
  protected Instant updated;
}
