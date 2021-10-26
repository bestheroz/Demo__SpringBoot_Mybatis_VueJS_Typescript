package com.github.bestheroz.demo.api.mine;

import com.github.bestheroz.demo.api.menu.MenuService;
import com.github.bestheroz.demo.api.role.RoleChildrenDTO;
import com.github.bestheroz.demo.api.role.RoleMapsDTO;
import com.github.bestheroz.demo.api.role.RoleService;
import com.github.bestheroz.demo.api.role.menu.RoleMenuChildrenDTO;
import com.github.bestheroz.demo.entity.Menu;
import com.github.bestheroz.demo.entity.Role;
import com.github.bestheroz.demo.entity.RoleMenuMap;
import com.github.bestheroz.demo.repository.AdminRepository;
import com.github.bestheroz.demo.repository.MenuRepository;
import com.github.bestheroz.demo.repository.RoleMenuMapRepository;
import com.github.bestheroz.demo.repository.RoleRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MineService {
  private final MenuService menuService;
  private final RoleService roleService;
  private final RoleRepository roleRepository;
  private final RoleMenuMapRepository roleMenuMapRepository;
  private final AdminRepository adminRepository;
  private final MenuRepository menuRepository;

  @Transactional(readOnly = true)
  public MineDTO getMyInfo() {
    return this.adminRepository
        .getItemById(AuthenticationUtils.getId())
        .map(
            a ->
                new MineDTO(
                    a,
                    this.roleRepository
                        .getItemById(a.getRoleId())
                        .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS)))
        .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
  }

  @Transactional(readOnly = true)
  public RoleMapsDTO getRole() {
    final RoleMapsDTO result =
        this.roleRepository
            .getItemById(AuthenticationUtils.getRoleId())
            .map(r -> new RoleMapsDTO(r, this.getChildren(r, null)))
            .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS));
    if (AuthenticationUtils.isSuperAdmin()) {
      result
          .getMaps()
          .addAll(
              this.menuService.getItems().stream()
                  .map(RoleMenuChildrenDTO::new)
                  .collect(Collectors.toList()));
    }
    return result;
  }

  private List<RoleMenuChildrenDTO> getChildren(final Role role, final Long parentId) {
    final Map<String, Object> params = new HashMap<>();
    params.put("roleId", role.getId());
    params.put("parentId", parentId == null ? "@NULL" : parentId);

    final List<RoleMenuMap> items = this.roleMenuMapRepository.getItemsByMap(params);
    final List<Menu> menus =
        this.menuRepository.getItemsByMap(
            Map.of(
                "id:in",
                items.stream()
                    .filter(Objects::nonNull)
                    .map(RoleMenuMap::getMenuId)
                    .collect(Collectors.toList())));

    return items.stream()
        .map(
            m ->
                new RoleMenuChildrenDTO(
                    m,
                    menus.stream()
                        .filter(menu -> menu.getId().equals(m.getMenuId()))
                        .findFirst()
                        .orElseThrow(
                            () -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS)),
                    this.getChildren(role, m.getId())))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<RoleChildrenDTO> getRoles(final Long id) {
    if (AuthenticationUtils.isSuperAdmin()) {
      return this.roleService.getItems();
    } else {
      return this.roleRepository.getItemById(id).stream()
          .map(r -> new RoleChildrenDTO(r, this.roleService.getChildren(r.getId())))
          .collect(Collectors.toList());
    }
  }
}
