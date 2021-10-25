package com.github.bestheroz.standard.common.authenticate;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConfigVO implements Serializable {
  private static final long serialVersionUID = -7056270586002398440L;
  private Long id;
  private String globalTheme;
  private String toolbarTheme;
  private String menuTheme;
  private Boolean toolbarDetached;
  private Boolean contentBoxed;
  private String primaryColor;
}
