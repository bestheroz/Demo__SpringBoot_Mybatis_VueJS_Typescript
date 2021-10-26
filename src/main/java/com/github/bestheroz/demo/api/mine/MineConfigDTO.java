package com.github.bestheroz.demo.api.mine;

import com.github.bestheroz.demo.entity.Admin;
import com.github.bestheroz.demo.entity.AdminConfig;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MineConfigDTO {
  @NotEmpty private String globalTheme;
  @NotEmpty private String toolbarTheme;
  @NotEmpty private String menuTheme;
  @NotEmpty private Boolean toolbarDetached;
  @NotEmpty private Boolean contentBoxed;
  @NotEmpty private String primaryColor;

  public MineConfigDTO(final AdminConfig adminConfig) {
    this.globalTheme = adminConfig.getGlobalTheme();
    this.toolbarTheme = adminConfig.getToolbarTheme();
    this.menuTheme = adminConfig.getMenuTheme();
    this.toolbarDetached = adminConfig.getToolbarDetached();
    this.contentBoxed = adminConfig.getContentBoxed();
    this.primaryColor = adminConfig.getPrimaryColor();
  }

  public AdminConfig toAdminConfig(final Admin admin) {
    return AdminConfig.builder()
        .adminId(admin.getId())
        .globalTheme(this.globalTheme)
        .toolbarTheme(this.toolbarTheme)
        .menuTheme(this.menuTheme)
        .toolbarDetached(this.toolbarDetached)
        .contentBoxed(this.contentBoxed)
        .primaryColor(this.primaryColor)
        .build();
  }
}
