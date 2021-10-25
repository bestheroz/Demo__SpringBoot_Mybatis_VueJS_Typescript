package com.github.bestheroz.demo.api.menu;

import com.github.bestheroz.demo.entity.Menu;
import com.github.bestheroz.demo.type.MenuType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuSimpleDTO {
  private Long id;

  @NotEmpty private String name;

  @Enumerated(EnumType.STRING)
  private MenuType type;

  private String url;
  private String icon;

  public MenuSimpleDTO(final Menu menu) {
    this.id = menu.getId();
    this.name = menu.getName();
    this.type = menu.getType();
    this.url = menu.getUrl();
    this.icon = menu.getIcon();
  }

  public MenuSimpleDTO(final MenuChildrenDTO dto) {
    this.id = dto.getId();
    this.name = dto.getName();
    this.type = dto.getType();
    this.url = dto.getUrl();
    this.icon = dto.getIcon();
  }

  public Menu toMenu() {
    return Menu.builder()
        .id(this.id)
        .name(this.name)
        .type(this.type)
        .url(this.url)
        .icon(this.icon)
        .build();
  }

  public MenuChildrenDTO toMenuChildrenDTO() {
    final MenuChildrenDTO menuChildrenDTO = new MenuChildrenDTO();
    menuChildrenDTO.setId(this.id);
    menuChildrenDTO.setName(this.name);
    menuChildrenDTO.setType(this.type);
    menuChildrenDTO.setUrl(this.url);
    menuChildrenDTO.setIcon(this.icon);
    return menuChildrenDTO;
  }
}
