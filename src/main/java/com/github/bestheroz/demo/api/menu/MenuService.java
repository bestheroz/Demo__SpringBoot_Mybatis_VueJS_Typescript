package com.github.bestheroz.demo.api.menu;

import com.github.bestheroz.demo.api.role.menu.RoleMenuChildrenDTO;
import com.github.bestheroz.demo.entity.Menu;
import com.github.bestheroz.demo.repository.MenuRepository;
import com.github.bestheroz.demo.type.MenuType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
  private final MenuRepository menuRepository;

  @Transactional(readOnly = true)
  public List<MenuChildrenDTO> getItems() {
    return this.menuRepository
        .getItemsByMapWithOrder(Map.of("parentId", "@NULL"), List.of("displayOrder"))
        .stream()
        .map(r -> new MenuChildrenDTO(r, this.getChildren(r.getId())))
        .toList();
  }

  public List<MenuChildrenDTO> getChildren(final Long parentId) {
    return this.menuRepository.getItemsByMap(Map.of("parentId", parentId)).stream()
        .map(r -> new MenuChildrenDTO(r, this.getChildren(r.getId())))
        .toList();
  }

  public void deleteById(final Long id) {
    this.menuRepository.deleteById(id);
  }

  public List<MenuChildrenDTO> saveAll(final List<MenuChildrenDTO> payload) {
    final List<Menu> menus = new ArrayList<>();
    this.getFatMenuWithRecursiveChildren(menus, payload, null);
    menus.forEach(m -> this.menuRepository.updateById(m, m.getId()));
    return this.getItems();
  }

  public void getFatMenuWithRecursiveChildren(
      final List<Menu> result, final List<MenuChildrenDTO> menuChildrenDTOS, final Menu parent) {
    int displayOrder = 1;
    for (final MenuChildrenDTO menuChildrenDTO : menuChildrenDTOS) {
      final Menu menu = menuChildrenDTO.toMenu(parent, displayOrder++);
      if (menuChildrenDTO.getType().equals(MenuType.GROUP)) {
        this.getFatMenuWithRecursiveChildren(result, menuChildrenDTO.getChildren(), menu);
      }
      result.add(menu);
    }
  }

  public static List<MenuChildrenDTO> getMenuChildrenDTOWithRecursiveChildren(
      final List<RoleMenuChildrenDTO> roleMenuChildrenDTOs) {
    final List<MenuChildrenDTO> result = new ArrayList<>();
    for (final RoleMenuChildrenDTO roleMenuChildrenDTO : roleMenuChildrenDTOs) {
      final MenuChildrenDTO menuChildrenDTO = roleMenuChildrenDTO.getMenu().toMenuChildrenDTO();
      if (menuChildrenDTO.getType().equals(MenuType.GROUP)) {
        menuChildrenDTO
            .getChildren()
            .addAll(
                MenuService.getMenuChildrenDTOWithRecursiveChildren(
                    roleMenuChildrenDTO.getChildren()));
      }
      result.add(menuChildrenDTO);
    }
    return result;
  }
}
