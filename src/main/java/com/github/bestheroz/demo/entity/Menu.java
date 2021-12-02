package com.github.bestheroz.demo.entity;

import com.github.bestheroz.demo.api.menu.MenuSimpleDTO;
import com.github.bestheroz.demo.repository.MenuRepository;
import com.github.bestheroz.demo.type.MenuType;
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
public class Menu implements Serializable {
  @Serial private static final long serialVersionUID = 2658557582464222508L;

  private Long id;

  private Long parentId;

  private String name;

  private MenuType type;

  private Integer displayOrder;
  private String url;
  private String icon;

  protected Long createdBy;
  protected Instant created;
  protected Long updatedBy;
  protected Instant updated;

  public void changeMenu(MenuRepository menuRepository, final MenuSimpleDTO dto) {
    this.name = dto.getName();
    this.type = dto.getType();
    this.url = dto.getUrl();
    this.icon = dto.getIcon();
    menuRepository.updateById(this, this.id);
  }
}
