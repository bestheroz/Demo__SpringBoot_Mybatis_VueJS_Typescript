package com.github.bestheroz.demo.entity;

import com.github.bestheroz.demo.api.admin.AdminDTO;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Admin extends AbstractCreatedUpdate implements Serializable {
  private static final long serialVersionUID = 7280716056600887400L;
  private Long id;
  private String adminId;
  private String password;
  private String name;
  private Long roleId;
  private Integer signInFailCnt;
  private Boolean available;
  private String token;
  private Instant expired;

  public void plusSignInFailCnt() {
    this.signInFailCnt = this.signInFailCnt + 1;
  }

  public void resetSignInFailCnt() {
    this.signInFailCnt = 0;
  }

  public void signedSuccess(final String token) {
    this.token = token;
    this.resetSignInFailCnt();
  }

  public void signOut() {
    this.token = null;
  }

  public void setPassword(final String password) {
    this.password = password;
    this.updated = Instant.now();
    this.updatedBy = AuthenticationUtils.getId();
    this.resetSignInFailCnt();
  }

  public void setName(final String name) {
    this.name = name;
    this.updated = Instant.now();
    this.updatedBy = AuthenticationUtils.getId();
  }

  public void change(final AdminDTO dto) {
    this.name = dto.getName();
    this.roleId = dto.getRole().getId();
    this.available = dto.getAvailable();
    this.expired = dto.getExpired();
    this.updated = Instant.now();
    this.updatedBy = AuthenticationUtils.getId();
  }
}
