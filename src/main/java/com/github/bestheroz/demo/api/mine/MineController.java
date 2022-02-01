package com.github.bestheroz.demo.api.mine;

import com.github.bestheroz.demo.api.menu.MenuChildrenDTO;
import com.github.bestheroz.demo.api.menu.MenuService;
import com.github.bestheroz.demo.api.role.RoleChildrenDTO;
import com.github.bestheroz.demo.api.role.RoleMapsDTO;
import com.github.bestheroz.demo.api.role.RoleSimpleDTO;
import com.github.bestheroz.demo.api.role.menu.RoleMenuService;
import com.github.bestheroz.demo.entity.Admin;
import com.github.bestheroz.demo.entity.AdminConfig;
import com.github.bestheroz.demo.repository.AdminConfigRepository;
import com.github.bestheroz.demo.repository.AdminRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import com.github.bestheroz.standard.common.util.NullUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/mine")
@Slf4j
@RequiredArgsConstructor
public class MineController {
  private final AdminRepository adminRepository;
  private final AdminConfigRepository adminConfigRepository;
  private final MineService mineService;
  private final MenuService menuService;
  private final RoleMenuService roleMenuService;

  @GetMapping()
  public ResponseEntity<ApiResult<MineDTO>> getMyInfo() {
    return Result.ok(this.mineService.getMyInfo());
  }

  @PatchMapping()
  public ResponseEntity<ApiResult<EditMeDTO>> editMe(@RequestBody @Valid final EditMeDTO payload) {
    return this.adminRepository
        .getItemById(AuthenticationUtils.getId())
        .map(
            admin -> {
              this.verifyPassword(admin.getPassword(), payload.getPassword());
              admin.setName(payload.getName());
              this.adminRepository.updateById(admin, admin.getId());
              return Result.ok(
                  new EditMeDTO(
                      this.adminRepository
                          .getItemById(admin.getId())
                          .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS)));
            })
        .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_ADMIN));
  }

  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static class EditMeDTO {
    private Long id;
    private String name;
    private String password;

    public EditMeDTO(final Admin admin) {
      this.id = admin.getId();
      this.name = admin.getName();
    }
  }

  @PatchMapping(value = "password")
  public ResponseEntity<ApiResult<?>> changePassword(
      @RequestBody @Valid final ChangePasswordDTO payload) {
    return this.adminRepository
        .getItemById(AuthenticationUtils.getId())
        .map(
            admin -> {
              this.verifyPassword(admin.getPassword(), payload.getOldPassword());
              admin.changePassword(payload.getNewPassword());
              this.adminRepository.updateById(admin, admin.getId());
              return Result.ok();
            })
        .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
  }

  @Data
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static class ChangePasswordDTO {
    @NotEmpty private String oldPassword;
    @NotEmpty private String newPassword;
  }

  @PostMapping(value = "config")
  public ResponseEntity<ApiResult<MineConfigDTO>> changeConfig(
      @RequestBody @Valid final MineConfigDTO payload) {
    return Result.ok(
        new MineConfigDTO(
            this.adminConfigRepository
                .getItemByMap(Map.of("adminId", AuthenticationUtils.getId()))
                .map(
                    adminConfig -> {
                      adminConfig.change(payload);
                      this.adminConfigRepository.updateById(adminConfig, adminConfig.getId());
                      return this.adminConfigRepository.getItemById(adminConfig.getId());
                    })
                .orElseGet(
                    () -> {
                      final AdminConfig adminConfig =
                          payload.toAdminConfig(AuthenticationUtils.getAdmin());
                      this.adminConfigRepository.insert(adminConfig);
                      return this.adminConfigRepository.getItemById(adminConfig.getId());
                    })
                .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS)));
  }

  @GetMapping(value = "config")
  public ResponseEntity<ApiResult<MineConfigDTO>> getConfig() {
    return Result.ok(
        this.adminConfigRepository
            .getItemByMap(Map.of("adminId", AuthenticationUtils.getId()))
            .map(MineConfigDTO::new)
            .orElseGet(MineConfigDTO::new));
  }

  @GetMapping(value = "role")
  public ResponseEntity<ApiResult<RoleMapsDTO>> getRole() {
    return Result.ok(this.mineService.getRole());
  }

  @GetMapping("roles/")
  ResponseEntity<ApiResult<List<RoleChildrenDTO>>> getRoles() {
    return Result.ok(this.mineService.getRoles(AuthenticationUtils.getRoleId()));
  }

  @GetMapping(value = "roles/selections/")
  public ResponseEntity<ApiResult<List<RoleSimpleDTO>>> getAllFlat() {
    final List<RoleChildrenDTO> roleChildrenDTOS =
        this.mineService.getRoles(AuthenticationUtils.getRoleId());
    final List<RoleSimpleDTO> result = new ArrayList<>();
    this.getRoleSimpleDTOWithRecursiveChildren(result, roleChildrenDTOS);
    return Result.ok(result);
  }

  private void getRoleSimpleDTOWithRecursiveChildren(
      final List<RoleSimpleDTO> result, final List<RoleChildrenDTO> roleChildrenDTOS) {
    for (final RoleChildrenDTO roleChildrenDTO : roleChildrenDTOS) {
      result.add(new RoleSimpleDTO(roleChildrenDTO));
      if (NullUtils.isNotEmpty(roleChildrenDTO.getChildren())) {
        this.getRoleSimpleDTOWithRecursiveChildren(result, roleChildrenDTO.getChildren());
      }
    }
  }

  @PostMapping(value = "verify-password")
  public ResponseEntity<ApiResult<?>> getVerifyPassword(@RequestBody @Valid final String password) {
    return this.adminRepository
        .getItemById(AuthenticationUtils.getId())
        .map(
            admin -> {
              this.verifyPassword(admin.getPassword(), password);
              return Result.ok();
            })
        .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_ADMIN));
  }

  // 패스워드 검증 공통 함수
  private void verifyPassword(final String adminPassword, final String payload) {
    final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();

    // 패스워드가 틀리면
    if (!pbkdf2PasswordEncoder.matches(adminPassword, pbkdf2PasswordEncoder.encode(payload))) {
      throw new BusinessException(ExceptionCode.FAIL_MATCH_PASSWORD);
    }
  }

  @GetMapping("menus/")
  public ResponseEntity<ApiResult<List<MenuChildrenDTO>>> getMenus() {
    if (AuthenticationUtils.isSuperAdmin()) {
      return Result.ok(this.menuService.getItems());
    } else {
      return Result.ok(
          MenuService.getMenuChildrenDTOWithRecursiveChildren(
              this.roleMenuService.getItems(AuthenticationUtils.getRoleId())));
    }
  }
}
