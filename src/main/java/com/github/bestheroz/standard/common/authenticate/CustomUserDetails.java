package com.github.bestheroz.standard.common.authenticate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.bestheroz.demo.entity.Admin;
import com.github.bestheroz.demo.entity.Role;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomUserDetails implements UserDetails, Serializable {
  private static final long serialVersionUID = -3806331610004769750L;
  private Long id;
  private String adminId;
  private String name;
  private Long roleId;

  public CustomUserDetails(final Admin admin, final Role role) {
    this.id = admin.getId();
    this.name = admin.getName();
    this.adminId = admin.getAdminId();
    this.roleId = role.getId();
  }

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return this.getAdminId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
