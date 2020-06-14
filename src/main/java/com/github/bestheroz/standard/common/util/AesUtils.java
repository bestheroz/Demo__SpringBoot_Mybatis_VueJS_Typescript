package com.github.bestheroz.standard.common.util;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
@UtilityClass
public class AesUtils {
    private final String ALGORITHM = "AES";
    private final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private final String DEFAULT_SECRET_KEY = "MY";
    private final String DEFAULT_IV = "MY";
    private final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public String encrypt128(final String text) {
        return encrypt128(text, DEFAULT_SECRET_KEY, DEFAULT_IV);
    }

    public String encrypt128(final String text, final String secretKey) {
        return encrypt128(text, secretKey, DEFAULT_IV);
    }

    public String encrypt128(final String text, final String secretKey, final String iv) {
        try {
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Arrays.copyOf(secretKey.getBytes(DEFAULT_CHARSET), 16), ALGORITHM),
                    new IvParameterSpec(Arrays.copyOf(iv.getBytes(DEFAULT_CHARSET), 16)));
            return Base64.encodeBase64String(cipher.doFinal(text.getBytes(DEFAULT_CHARSET)));
        } catch (final IllegalBlockSizeException | BadPaddingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_ILLEGAL_BLOCK_SIZE_AES);
        } catch (final InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(e);
        }
    }

    public String decrypt128(final String text) {
        return decrypt128(text, DEFAULT_SECRET_KEY, DEFAULT_IV);
    }

    public String decrypt128(final String text, final String secretKey) {
        return decrypt128(text, secretKey, DEFAULT_IV);
    }

    public String decrypt128(final String text, final String secretKey, final String iv) {
        try {
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Arrays.copyOf(secretKey.getBytes(DEFAULT_CHARSET), 16), ALGORITHM),
                    new IvParameterSpec(Arrays.copyOf(iv.getBytes(DEFAULT_CHARSET), 16)));
            return new String(cipher.doFinal(Base64.decodeBase64(text)), DEFAULT_CHARSET);
        } catch (final IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            // throw new CommonResponseException(CommonResultCode.ERROR_ILLEGAL_BLOCK_SIZE_AES);
            return StringUtils.EMPTY;
        } // throw new CommonResponseException(e);

    }

    public String encrypt256(final String text) {
        return encrypt256(text, DEFAULT_SECRET_KEY, DEFAULT_IV);
    }

    public String encrypt256(final String text, final String secretKey) {
        return encrypt256(text, secretKey, DEFAULT_IV);
    }

    public String encrypt256(final String text, final String secretKey, final String iv) {
        try {
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Arrays.copyOf(secretKey.getBytes(DEFAULT_CHARSET), 32), ALGORITHM),
                    new IvParameterSpec(Arrays.copyOf(iv.getBytes(DEFAULT_CHARSET), 16)));
            return Base64.encodeBase64String(cipher.doFinal(text.getBytes(DEFAULT_CHARSET)));
        } catch (final IllegalBlockSizeException | BadPaddingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ExceptionCode.ERROR_ILLEGAL_BLOCK_SIZE_AES);
        } catch (final InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(e);
        }
    }

    public String decrypt256(final String text) {
        return decrypt256(text, DEFAULT_SECRET_KEY, DEFAULT_IV);
    }

    public String decrypt256(final String text, final String secretKey) {
        return decrypt256(text, secretKey, DEFAULT_IV);
    }

    public String decrypt256(final String text, final String secretKey, final String iv) {
        try {
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Arrays.copyOf(secretKey.getBytes(DEFAULT_CHARSET), 32), ALGORITHM),
                    new IvParameterSpec(Arrays.copyOf(iv.getBytes(DEFAULT_CHARSET), 16)));
            return new String(cipher.doFinal(Base64.decodeBase64(text)), DEFAULT_CHARSET);
        } catch (final IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            // throw new CommonResponseException(CommonResultCode.ERROR_ILLEGAL_BLOCK_SIZE_AES);
            return StringUtils.EMPTY;
        } // throw new CommonResponseException(e);

    }

}
