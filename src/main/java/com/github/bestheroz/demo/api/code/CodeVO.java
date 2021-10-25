package com.github.bestheroz.demo.api.code;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeVO<T> implements Serializable {
  private static final long serialVersionUID = 272726757907169621L;
  private T value;
  private String text;
}
