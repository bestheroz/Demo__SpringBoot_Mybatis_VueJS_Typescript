package com.github.bestheroz.demo.api.role.menu;

import com.github.bestheroz.demo.api.menu.MenuSimpleDTO;
import com.github.bestheroz.demo.entity.Menu;
import com.github.bestheroz.demo.entity.RoleMenuMap;
import com.github.bestheroz.demo.repository.MenuRepository;
import com.github.bestheroz.demo.repository.RoleMenuMapRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.util.NullUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoleMenuService {
  private final RoleMenuMapRepository roleMenuMapRepository;
  private final MenuRepository menuRepository;

  @Transactional(readOnly = true)
  public List<RoleMenuChildrenDTO> getItems(final Long roleId) {
    final List<RoleMenuMap> roleMenuMaps =
        this.roleMenuMapRepository.getItemsByMapWithOrder(
            Map.of("roleId", roleId, "parentId", "@NULL"), List.of("displayOrder"));
    final List<Menu> menus =
        this.menuRepository.getItemsByMap(
            Map.of(
                "id:in",
                roleMenuMaps.stream()
                    .filter(Objects::nonNull)
                    .map(RoleMenuMap::getMenuId)
                    .collect(Collectors.toList())));
    return roleMenuMaps.stream()
        .map(
            r ->
                new RoleMenuChildrenDTO(
                    r,
                    new MenuSimpleDTO(
                        menus.stream()
                            .filter(m -> m.getId().equals(r.getMenuId()))
                            .findFirst()
                            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS)),
                    this.getChildren(r.getId())))
        .collect(Collectors.toList());
  }

  private List<RoleMenuChildrenDTO> getChildren(final Long id) {
    final List<RoleMenuMap> roleMenuMaps =
        this.roleMenuMapRepository.getItemsByMap(Map.of("parentId", id));
    final List<Menu> menus =
        this.menuRepository.getItemsByMap(
            Map.of(
                "id:in",
                roleMenuMaps.stream()
                    .filter(Objects::nonNull)
                    .map(RoleMenuMap::getMenuId)
                    .collect(Collectors.toList())));
    return roleMenuMaps.stream()
        .map(
            r ->
                new RoleMenuChildrenDTO(
                    r,
                    new MenuSimpleDTO(
                        menus.stream()
                            .filter(m -> m.getId().equals(r.getMenuId()))
                            .findFirst()
                            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS)),
                    this.getChildren(r.getId())))
        .collect(Collectors.toList());
  }

  public List<RoleMenuChildrenDTO> saveAll(
      final Long roleId, final List<RoleMenuChildrenDTO> payload) {
    if (NullUtils.isEmpty(payload)) {
      this.roleMenuMapRepository.deleteByMap(Map.of("roleId", roleId));
    } else {
      final List<Long> delete_ids = new ArrayList<>();
      this.roleMenuMapRepository
          .getItemsByMap(
              Map.of(
                  "roleId",
                  roleId,
                  "id:notIn",
                  payload.stream()
                      .map(RoleMenuChildrenDTO::getId)
                      .filter(Objects::nonNull)
                      .collect(Collectors.toList()),
                  "parentId",
                  "@NULL"))
          .forEach(
              r -> {
                delete_ids.add(r.getId());
                this.getDeleteIds(delete_ids, r.getId());
              });
    }
    final List<RoleMenuMap> roleMenuMaps = new ArrayList<>();
    this.getFlatRoleMenuMapWithRecursiveChildren(roleMenuMaps, roleId, payload, null);
    this.roleMenuMapRepository.insertBatch(
        roleMenuMaps.stream().filter(r -> r.getId() == null).collect(Collectors.toList()));
    roleMenuMaps.stream()
        .filter(r -> r.getId() != null)
        .forEach(r -> this.roleMenuMapRepository.updateById(r, r.getId()));
    return this.getItems(roleId);
  }

  private void getDeleteIds(final List<Long> delete_ids, final Long id) {
    this.roleMenuMapRepository
        .getItemsByMap(Map.of("parentId", id))
        .forEach(
            r -> {
              delete_ids.add(r.getId());
              this.getDeleteIds(delete_ids, r.getId());
            });
  }

  public void getFlatRoleMenuMapWithRecursiveChildren(
      final List<RoleMenuMap> result,
      final Long roleId,
      final List<RoleMenuChildrenDTO> roleMenuChildrenDTOS,
      final Long parentId) {
    int displayOrder = 1;
    for (final RoleMenuChildrenDTO roleMenuChildrenDTO : roleMenuChildrenDTOS) {
      final RoleMenuMap roleMenuMap =
          roleMenuChildrenDTO.toRoleMenuMap(parentId, roleId, displayOrder++);
      this.getFlatRoleMenuMapWithRecursiveChildren(
          result, roleId, roleMenuChildrenDTO.getChildren(), roleMenuChildrenDTO.getId());
      result.add(roleMenuMap);
    }
  }
}
