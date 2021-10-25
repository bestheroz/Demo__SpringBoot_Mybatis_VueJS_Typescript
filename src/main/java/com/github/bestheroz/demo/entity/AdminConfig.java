package com.github.bestheroz.demo.entity;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminConfig extends AbstractCreatedUpdate implements Serializable {
  private static final long serialVersionUID = 1426310156205338408L;

  private Long id;
  private Long adminId;
  private String globalTheme;
  private String toolbarTheme;
  private String menuTheme;
  private Boolean toolbarDetached;
  private Boolean contentBoxed;
  private String primaryColor;
}
