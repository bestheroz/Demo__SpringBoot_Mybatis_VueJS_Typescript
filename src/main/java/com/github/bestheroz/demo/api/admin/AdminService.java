package com.github.bestheroz.demo.api.admin;

import com.github.bestheroz.demo.api.role.RoleService;
import com.github.bestheroz.demo.entity.Admin;
import com.github.bestheroz.demo.entity.Role;
import com.github.bestheroz.demo.repository.AdminRepository;
import com.github.bestheroz.demo.repository.RoleRepository;
import com.github.bestheroz.demo.type.Page;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.mybatis.DataTableFilterDTO;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
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
public class AdminService {
  private final AdminRepository adminRepository;
  private final RoleRepository roleRepository;
  private final RoleService roleService;

  @Transactional(readOnly = true)
  public Page<AdminDTO> getAdmins(
      final String search, final DataTableFilterDTO dataTableFilterDTO) {
    if (dataTableFilterDTO.getFilter().get("roleId:in") == null
        && AuthenticationUtils.isNotSuperAdmin()) {
      dataTableFilterDTO
          .getFilter()
          .put(
              "roleId:in",
              this.roleService.getFlatRoleIdsByIdWithRecursiveChildren(
                  AuthenticationUtils.getRoleId()));
    }
    final int count =
        this.adminRepository.countForSearchAndDataTable(
            search, List.of("adminId", "name"), dataTableFilterDTO);
    if (count == 0) {
      return new Page<>(count);
    }

    final List<Admin> items =
        this.adminRepository.getItemsForSearchAndDataTable(
            search, List.of("adminId", "name"), dataTableFilterDTO);
    final List<Role> roles =
        this.roleRepository.getItemsByMap(
            Map.of(
                "id:in",
                items.stream()
                    .filter(Objects::nonNull)
                    .map(Admin::getRoleId)
                    .collect(Collectors.toList())));
    return new Page<>(
        count,
        items.stream()
            .map(
                data ->
                    new AdminDTO(
                        data,
                        roles.stream()
                            .filter(r -> r.getId().equals(data.getRoleId()))
                            .findFirst()
                            .orElseThrow(
                                () -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS))))
            .collect(Collectors.toList()));
  }

  @Transactional(readOnly = true)
  public AdminDTO getAdmin(final Long id) {
    final Admin admin =
        this.adminRepository
            .getItemById(id)
            .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS));
    return new AdminDTO(
        admin,
        this.roleRepository
            .getItemById(admin.getRoleId())
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }

  public AdminDTO change(final Long id, final AdminDTO payload) {
    return this.adminRepository
        .getItemById(id)
        .map(
            item -> {
              item.change(payload);
              this.adminRepository.updateById(item, item.getId());
              return this.getAdmin(id);
            })
        .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS));
  }

  public AdminDTO resetPassword(final Long id, final String password) {
    return this.adminRepository
        .getItemById(id)
        .map(
            (item) -> {
              item.setPassword(password);
              this.adminRepository.updateById(item, item.getId());
              return this.getAdmin(id);
            })
        .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS));
  }
}
