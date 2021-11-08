package com.github.bestheroz.demo.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Code implements Serializable {
  @Serial private static final long serialVersionUID = -6076508411557466173L;

  private Long id;
  private String type;
  private String value;
  private String text;
  private Boolean available;
  private Integer displayOrder;

  protected Long createdBy;
  protected Instant created;
  protected Long updatedBy;
  protected Instant updated;
}
