package com.github.bestheroz.demo.api.sign.out;

import com.github.bestheroz.demo.domain.Admin;
import com.github.bestheroz.demo.repository.AdminRepository;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SignOutService {
  private final AdminRepository adminRepository;

  public void signOut() {
    this.adminRepository.getItemById(AuthenticationUtils.getId()).ifPresent(Admin::signOut);
  }
}
