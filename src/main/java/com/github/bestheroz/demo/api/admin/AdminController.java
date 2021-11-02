package com.github.bestheroz.demo.api.admin;

import com.github.bestheroz.demo.api.code.CodeVO;
import com.github.bestheroz.demo.entity.Admin;
import com.github.bestheroz.demo.repository.AdminRepository;
import com.github.bestheroz.demo.type.Page;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "api/admins/")
@RequiredArgsConstructor
public class AdminController {
  private final AdminRepository adminRepository;
  private final AdminService adminService;

  @GetMapping(value = "codes/")
  public ResponseEntity<ApiResult<List<CodeVO<Long>>>> getCodes() {
    return Result.ok(this.adminRepository.getCodes());
  }

  @GetMapping
  public ResponseEntity<ApiResult<Page<AdminDTO>>> getItems(final AdminFilter adminFilter) {
    return Result.ok(this.adminService.getAdmins(adminFilter));
  }

  @PostMapping
  public ResponseEntity<ApiResult<AdminDTO>> post(
      @RequestBody @Valid final AdminPasswordDTO payload) {
    final Admin admin = payload.toAdmin();
    this.adminRepository.insert(admin);
    return Result.created(this.adminService.getAdmin(admin.getId()));
  }

  @PatchMapping(value = "{id}")
  public ResponseEntity<ApiResult<AdminDTO>> patch(
      @PathVariable(value = "id") final Long id, @RequestBody @Valid final AdminDTO payload) {
    return Result.ok(this.adminService.change(id, payload));
  }

  @PatchMapping(value = "{id}/reset-password")
  public ResponseEntity<ApiResult<AdminDTO>> resetPassword(
      @PathVariable(value = "id") final Long id, @RequestBody @Valid final String password) {
    return Result.ok(this.adminService.resetPassword(id, password));
  }
}
