package com.github.bestheroz.standard.common.authenticate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.bestheroz.demo.entity.Admin;
import com.github.bestheroz.demo.entity.Role;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomUserDetails implements UserDetails, Serializable {
  private static final long serialVersionUID = -3806331610004769750L;
  private Long id;
  private String loginId;
  private String name;
  private Long roleId;

  public CustomUserDetails(final Admin admin, final Role role) {
    this.id = admin.getId();
    this.name = admin.getName();
    this.loginId = admin.getLoginId();
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
    return this.getLoginId();
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
