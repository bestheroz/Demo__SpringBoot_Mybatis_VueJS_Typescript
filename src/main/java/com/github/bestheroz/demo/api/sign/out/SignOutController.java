package com.github.bestheroz.demo.api.sign.out;

import com.github.bestheroz.standard.common.authenticate.JwtTokenProvider;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sign-out")
@RequiredArgsConstructor
public class SignOutController {
  private final SignOutService signOutService;

  @DeleteMapping
  public void signOut(
      @RequestHeader(value = "Authorization", required = false) final String accessToken) {
    if (StringUtils.isEmpty(accessToken)) {
      return Result.ok();
    }
    try {
      SecurityContextHolder.getContext()
          .setAuthentication(JwtTokenProvider.getAuthentication(accessToken));
      if (AuthenticationUtils.isNotSigned()) {
        return Result.ok();
      }
    } catch (final UsernameNotFoundException e) {
      return Result.ok();
    }
    this.signOutService.signOut();
    AuthenticationUtils.signOut();
    return Result.ok();
  }
}
