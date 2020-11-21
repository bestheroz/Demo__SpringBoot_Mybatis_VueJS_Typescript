package com.github.bestheroz.standard.common.code;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeVO implements Serializable {

  private static final long serialVersionUID = 272726757907169621L;
  private String value;
  private String text;
}
