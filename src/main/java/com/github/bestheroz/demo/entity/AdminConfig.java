package com.github.bestheroz.demo.entity;

import com.github.bestheroz.demo.api.mine.MineConfigDTO;
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

  public void change(final MineConfigDTO dto) {
    this.globalTheme = dto.getGlobalTheme();
    this.toolbarTheme = dto.getToolbarTheme();
    this.menuTheme = dto.getMenuTheme();
    this.toolbarDetached = dto.getToolbarDetached();
    this.contentBoxed = dto.getContentBoxed();
    this.primaryColor = dto.getPrimaryColor();
  }
}
