package com.github.bestheroz.standard.common.security;

public interface PasswordEncryptor {

    /**
     * Encrypt a raw password
     */
    String encrypt(String rawPassword);
}
