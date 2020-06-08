package com.github.bestheroz.standard.common.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptorDelegator implements PasswordEncryptor {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncryptorDelegator(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encrypt(final String rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }
}
