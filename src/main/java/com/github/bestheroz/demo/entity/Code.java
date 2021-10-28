package com.github.bestheroz.demo.entity;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Code extends AbstractCreatedUpdate implements Serializable {
  private static final long serialVersionUID = -6076508411557466173L;

  private Long id;
  private String type;
  private String value;
  private String text;
  private Boolean available;
  private Integer displayOrder;
}
