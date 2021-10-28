package com.github.bestheroz.demo.api.code;

import com.github.bestheroz.demo.entity.Code;
import java.time.Instant;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeDTO {
  private Long id;
  @NotEmpty private String type;
  @NotEmpty private String value;
  @NotEmpty private String text;
  @NotNull private Boolean available;
  @NotNull private Integer displayOrder;

  private Long createdBy;
  private Instant created;

  private Long updatedBy;
  private Instant updated;

  public CodeDTO(final Code code) {
    this.id = code.getId();
    this.type = code.getType();
    this.value = code.getValue();
    this.text = code.getText();
    this.available = code.getAvailable();
    this.displayOrder = code.getDisplayOrder();
    this.created = code.getCreated();
    this.createdBy = code.getCreatedBy();
    this.updated = code.getUpdated();
    this.updatedBy = code.getUpdatedBy();
  }

  public Code toCode() {
    return Code.builder()
        .id(this.id)
        .type(this.type)
        .value(this.value)
        .text(this.text)
        .available(this.available)
        .displayOrder(this.displayOrder)
        .build();
  }
}
