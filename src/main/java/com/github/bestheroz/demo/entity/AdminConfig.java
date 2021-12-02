package com.github.bestheroz.demo.entity;

import com.github.bestheroz.demo.api.mine.MineConfigDTO;
import com.github.bestheroz.demo.repository.AdminConfigRepository;
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
public class AdminConfig implements Serializable {
  @Serial private static final long serialVersionUID = 1426310156205338408L;

  private Long id;
  private Long adminId;
  private String globalTheme;
  private String toolbarTheme;
  private String menuTheme;
  private Boolean toolbarDetached;
  private Boolean contentBoxed;
  private String primaryColor;

  protected Long createdBy;
  protected Instant created;
  protected Long updatedBy;
  protected Instant updated;

  public void change(AdminConfigRepository adminConfigRepository, final MineConfigDTO dto) {
    this.globalTheme = dto.getGlobalTheme();
    this.toolbarTheme = dto.getToolbarTheme();
    this.menuTheme = dto.getMenuTheme();
    this.toolbarDetached = dto.getToolbarDetached();
    this.contentBoxed = dto.getContentBoxed();
    this.primaryColor = dto.getPrimaryColor();
    adminConfigRepository.updateById(this, this.id);
  }
}
