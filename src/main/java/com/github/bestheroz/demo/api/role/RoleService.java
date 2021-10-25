package com.github.bestheroz.demo.api.role;

import com.github.bestheroz.demo.entity.Role;
import com.github.bestheroz.demo.repository.RoleRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import com.github.bestheroz.standard.common.util.NullUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {
  private final RoleRepository roleRepository;

  @Transactional(readOnly = true)
  public List<RoleChildrenDTO> getItems() {
    return this.roleRepository
        .getItemsByMapWithOrder(Map.of("parentId", "@NULL"), List.of("displayOrder"))
        .stream()
        .map(r -> new RoleChildrenDTO(r, this.getChildren(r.getId())))
        .collect(Collectors.toList());
  }

  private List<RoleChildrenDTO> getChildren(final Long id) {
    return this.roleRepository.getItemsByMap(Map.of("parentId", id)).stream()
        .map(r -> new RoleChildrenDTO(r, this.getChildren(r.getId())))
        .collect(Collectors.toList());
  }

  public RoleChildrenDTO getItem(final Long id) {
    return new RoleChildrenDTO(
        this.roleRepository
            .getItemById(id)
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS),
        this.getChildren(id));
  }

  public List<RoleChildrenDTO> saveAll(final List<RoleChildrenDTO> payload) {
    if (AuthenticationUtils.isSuperAdmin()) {
      final List<Long> delete_ids = new ArrayList<>();
      this.roleRepository
          .getItemsByMap(
              Map.of(
                  "id:notIn",
                  payload.stream()
                      .map(RoleChildrenDTO::getId)
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
    final List<Role> roles = new ArrayList<>();
    this.getFlatRoleWithRecursiveChildren(roles, payload, null);
    this.roleRepository.insertBatch(
        roles.stream().filter(r -> r.getId() == null).collect(Collectors.toList()));
    roles.stream()
        .filter(r -> r.getId() != null)
        .forEach(r -> this.roleRepository.updateById(r, r.getId()));
    return this.getItems();
  }

  private void getDeleteIds(final List<Long> delete_ids, final Long id) {
    this.roleRepository
        .getItemsByMap(Map.of("parentId", id))
        .forEach(
            r -> {
              delete_ids.add(r.getId());
              this.getDeleteIds(delete_ids, r.getId());
            });
  }

  private void getFlatRoleWithRecursiveChildren(
      final List<Role> result, final List<RoleChildrenDTO> roleChildrenDTOS, final Role parent) {
    int displayOrder = 1;
    for (final RoleChildrenDTO roleChildrenDTO : roleChildrenDTOS) {
      final Role role = roleChildrenDTO.toRole(parent, displayOrder++);
      if (NullUtils.isNotEmpty(roleChildrenDTO.getChildren())) {
        this.getFlatRoleWithRecursiveChildren(result, roleChildrenDTO.getChildren(), role);
      }
      result.add(role);
    }
  }

  public List<Long> getFlatRoleIdsByIdWithRecursiveChildren(final Long id) {
    final List<Long> roleIdList = new ArrayList<>();
    this.roleRepository
        .getItemById(id)
        .ifPresent(
            (role) -> {
              roleIdList.add(role.getId());
              this.getFlatRoleIdsWithRecursiveChildren(roleIdList, role.getId());
            });
    return roleIdList;
  }

  private void getFlatRoleIdsWithRecursiveChildren(final List<Long> result, final Long parentId) {
    for (final Role child : this.roleRepository.getItemsByMap(Map.of("parentId", parentId))) {
      result.add(child.getId());
      this.getFlatRoleIdsWithRecursiveChildren(result, child.getId());
    }
  }

  @Transactional(readOnly = true)
  public List<RoleSimpleDTO> getSelections(final Boolean available) {
    final List<Long> ids;
    if (AuthenticationUtils.isSuperAdmin()) {
      ids = null;
    } else {
      ids = this.getFlatRoleIdsByIdWithRecursiveChildren(AuthenticationUtils.getRoleId());
    }
    final Map<String, Object> map = new HashMap<>();
    if (ids != null) {
      map.put("id:in", ids);
    }
    if (available != null) {
      map.put("available", available);
    }
    return this.roleRepository.getItemsByMap(map).stream()
        .map(RoleSimpleDTO::new)
        .collect(Collectors.toList());
  }
}
