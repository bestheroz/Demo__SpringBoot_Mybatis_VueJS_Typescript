package com.github.bestheroz.demo.api.menu;

import com.github.bestheroz.demo.api.internal.role.menu.RoleMenuChildrenDTO;
import com.github.bestheroz.demo.domain.Menu;
import com.github.bestheroz.demo.repository.MenuRepository;
import com.github.bestheroz.demo.type.MenuType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
  private final MenuRepository menuRepository;

  @Transactional(readOnly = true)
  public List<MenuChildrenDTO> getItems() {
    return this.menuRepository.findAllByParentIdNullOrderByDisplayOrderAsc().stream()
        .map(MenuChildrenDTO::new)
        .collect(Collectors.toList());
  }

  public void deleteById(final Long id) {
    this.menuRepository.deleteById(id);
  }

  public List<MenuChildrenDTO> saveAll(final List<MenuChildrenDTO> payload) {
    return IterableUtils.toList(
            this.menuRepository.saveAll(this.getMenuWithRecursiveChildren(payload, null)))
        .stream()
        .map(MenuChildrenDTO::new)
        .collect(Collectors.toList());
  }

  public List<Menu> getMenuWithRecursiveChildren(
      final List<MenuChildrenDTO> menuChildrenDTOS, final Menu parent) {
    int displayOrder = 1;
    final List<Menu> result = new ArrayList<>();
    for (final MenuChildrenDTO menuChildrenDTO : menuChildrenDTOS) {
      final Menu menu = menuChildrenDTO.toMenu(parent, displayOrder++);
      if (menuChildrenDTO.getType().equals(MenuType.GROUP)) {
        menu.getChildren()
            .addAll(this.getMenuWithRecursiveChildren(menuChildrenDTO.getChildren(), menu));
      }
      result.add(menu);
    }
    return result;
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
