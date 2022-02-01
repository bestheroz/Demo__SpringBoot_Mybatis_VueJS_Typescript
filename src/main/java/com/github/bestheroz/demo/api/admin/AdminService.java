package com.github.bestheroz.demo.api.admin;

import com.github.bestheroz.demo.api.role.RoleService;
import com.github.bestheroz.demo.entity.Admin;
import com.github.bestheroz.demo.repository.AdminRepository;
import com.github.bestheroz.demo.repository.RoleRepository;
import com.github.bestheroz.demo.type.Page;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
  public Page<AdminDTO> getAdmins(final AdminFilter adminFilter) {
    adminFilter.getFilter().put("roleId:ne", 1);
    if (adminFilter.getRoleId().isEmpty() && AuthenticationUtils.isNotSuperAdmin()) {
      final Set<Long> roleIds =
          this.roleService.getFlatRoleIdsByIdWithRecursiveChildren(AuthenticationUtils.getRoleId());
      if (!roleIds.isEmpty()) {
        adminFilter.getFilter().put("roleId:in", roleIds);
      }
    }
    final int count = this.adminRepository.countForDataTable(adminFilter);
    if (count == 0) {
      return new Page<>(count);
    }

    final List<Admin> admins = this.adminRepository.getItemsForDataTable(adminFilter);
    return new Page<>(
        count,
        admins.stream()
            .map(
                admin ->
                    new AdminDTO(
                        admin,
                        this.roleRepository
                            .getItemsByMap(
                                Map.of(
                                    "id:in",
                                    admins.stream()
                                        .filter(Objects::nonNull)
                                        .map(Admin::getRoleId)
                                        .collect(Collectors.toSet())))
                            .stream()
                            .filter(r -> r.getId().equals(admin.getRoleId()))
                            .findFirst()
                            .orElseThrow(
                                () -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS))))
            .toList());
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
            admin -> {
              admin.change(payload);
              this.adminRepository.updateById(admin, id);
              return this.getAdmin(id);
            })
        .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS));
  }

  public AdminDTO resetPassword(final Long id, final String password) {
    return this.adminRepository
        .getItemById(id)
        .map(
            (admin) -> {
              admin.changePassword(password);
              this.adminRepository.updateById(admin, id);
              return this.getAdmin(id);
            })
        .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS));
  }
}
