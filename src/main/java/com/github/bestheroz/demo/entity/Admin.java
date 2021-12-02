package com.github.bestheroz.demo.entity;

import com.github.bestheroz.demo.api.admin.AdminDTO;
import com.github.bestheroz.demo.repository.AdminRepository;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Admin implements Serializable {
  @Serial private static final long serialVersionUID = 7280716056600887400L;
  private Long id;
  private String adminId;
  private String password;
  private String name;
  private Long roleId;
  private Integer signInFailCnt;
  private Boolean available;
  private String token;
  private Instant expired;

  protected Long createdBy;
  protected Instant created;
  protected Long updatedBy;
  protected Instant updated;

  public void plusSignInFailCnt(AdminRepository adminRepository) {
    this.signInFailCnt = this.signInFailCnt + 1;
    adminRepository.plusSignInFailCnt(this.id);
  }

  public void signedSuccess(AdminRepository adminRepository, final String token) {
    this.token = token;
    this.signInFailCnt = 0;
    adminRepository.updateTokenAndSignInFailCnt(this.id, this.token);
  }

  public void signOut(AdminRepository adminRepository) {
    adminRepository.updateTokenNullById(this.id);
  }

  public void changePassword(AdminRepository adminRepository, final String password) {
    this.password = password;
    this.signInFailCnt = 0;
    adminRepository.updateById(this, this.id);
  }

  public void changeName(AdminRepository adminRepository, final String name) {
    this.name = name;
    adminRepository.updateById(this, this.id);
  }

  public void change(AdminRepository adminRepository, final AdminDTO dto) {
    this.name = dto.getName();
    this.roleId = dto.getRole().getId();
    this.available = dto.getAvailable();
    this.expired = dto.getExpired();
    adminRepository.updateById(this, this.id);
  }
}
