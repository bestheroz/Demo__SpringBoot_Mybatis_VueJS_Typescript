package com.github.bestheroz.demo.api.sign.out;

import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sign-out")
@RequiredArgsConstructor
public class SignOutController {
  private final SignOutService signOutService;

  @DeleteMapping
  public void signOut() {
    if (AuthenticationUtils.isNotSigned()) {
      return;
    }
    this.signOutService.signOut();
    AuthenticationUtils.signOut();
  }
}
