package com.github.bestheroz.standard.common.util;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tika.Tika;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@UtilityClass
@Slf4j
public class FileUtils {
  private final String FILE_ROOT_PATH = System.getProperty("java.io.tmpdir");
  private final String STR_DOT = ".";
  private final String STR_INFO_MESSAGE = "Target for uploading file : {}";
  private final String STR_UNDERLINE = "_";
  private final Tika TIKA_INSTANCE = new Tika();

  public void deleteDirectory(final File file) {
    forceDelete(file);
    log.info("Target for deleting dir : {}", file.getAbsolutePath());
  }

  public void deleteDirectory(final String filePath) {
    deleteFile(getFile(filePath));
  }

  public void deleteFile(final File file) {
    forceDelete(file);
    log.info("Target for deleting file : {}", file.getAbsolutePath());
  }

  public void deleteFile(final String filePath) {
    deleteFile(getFile(filePath));
  }

  private void forceDelete(final File file) {
    try {
      org.apache.commons.io.FileUtils.forceDelete(file);
    } catch (final IOException e) {
      log.warn(ExceptionUtils.getStackTrace(e));
      // throw new CommonResponseException(e);
    }
  }

  public String getEncodedFileName(final HttpServletRequest request, final String fileName) {
    try {
      final String header = request.getHeader("User-Agent");

      final String encodedFilename;
      if (StringUtils.contains(header, "MSIE")) {
        encodedFilename =
            URLEncoder.encode(fileName, StandardCharsets.UTF_8.displayName())
                .replaceAll("\\+", "%20");
      } else if (StringUtils.contains(header, "Trident")) {
        encodedFilename =
            URLEncoder.encode(fileName, StandardCharsets.UTF_8.displayName())
                .replaceAll("\\+", "%20");
      } else if (StringUtils.contains(header, "Chrome")) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fileName.length(); i++) {
          final char c = fileName.charAt(i);
          if (c > '~') {
            sb.append(
                URLEncoder.encode(StringUtils.EMPTY + c, StandardCharsets.UTF_8.displayName()));
          } else {
            sb.append(c);
          }
        }
        encodedFilename = sb.toString();
      } else {
        encodedFilename =
            URLDecoder.decode(
                "\""
                    + new String(
                        fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)
                    + "\"",
                StandardCharsets.UTF_8.displayName());
      }
      return encodedFilename;
    } catch (final UnsupportedEncodingException e) {
      log.warn(ExceptionUtils.getStackTrace(e));
      throw new BusinessException(e);
    }
  }

  public File getFile(final String filePath) {
    final String path =
        RegExUtils.replaceAll(getFileRoot() + "/" + filePath, "\\\\", "/").replaceAll("//", "/");
    final File file = org.apache.commons.io.FileUtils.getFile(path);
    if (file.isDirectory() && !StringUtils.endsWith(path, "/")) {
      log.warn("{} : {}", path, ExceptionCode.ERROR_DIR_PATH_MUST_ENDS_WITH_SLASH.toString());
      throw new BusinessException(ExceptionCode.ERROR_DIR_PATH_MUST_ENDS_WITH_SLASH);
    }
    return file;
  }

  public void checkExistingDirectory(final String dirPath) {
    final String path =
        RegExUtils.replaceAll(getFileRoot() + dirPath, "\\\\", "/").replaceAll("//", "/");
    if (!StringUtils.endsWith(path, "/")) {
      log.warn("{} : {}", path, ExceptionCode.ERROR_DIR_PATH_MUST_ENDS_WITH_SLASH.toString());
      throw new BusinessException(ExceptionCode.ERROR_DIR_PATH_MUST_ENDS_WITH_SLASH);
    }
    log.debug(path);
    final File file = new File(path);
    if (!NullUtils.exists(file)) {
      try {
        org.apache.commons.io.FileUtils.forceMkdir(file);
      } catch (final IOException e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        // ignored
      }
    }
  }

  private String getFileRoot() {
    return FILE_ROOT_PATH;
  }

  public boolean isExistsFile(final String filePath) {
    return NullUtils.exists(getFile(filePath));
  }

  public void uploadAllFiles(
      final MultipartHttpServletRequest mRequest, final String targetDirPath) {
    final Map<String, MultipartFile> fileMap = mRequest.getFileMap();
    if (NullUtils.size(fileMap) < 1) {
      return;
    }
    checkExistingDirectory(targetDirPath);
    final Iterator<String> fileNames = mRequest.getFileNames();
    while (NullUtils.hasNext(fileNames)) {
      final MultipartFile multipartFile = fileMap.get(fileNames.next());
      validateFile(multipartFile);
      final File file = uploadMultipartFile(targetDirPath, multipartFile);
      log.info(STR_INFO_MESSAGE, file.getAbsolutePath());
    }
  }

  private File uploadMultipartFile(final String targetDirPath, final MultipartFile multipartFile) {
    final StringBuilder fileName = new StringBuilder(80);
    fileName
        .append(DateUtils.toStringNow("yyyyMMddHHmmss"))
        .append(STR_UNDERLINE)
        .append(
            DigestUtils.md5DigestAsHex(
                Objects.requireNonNull(multipartFile.getOriginalFilename()).getBytes()));
    if (StringUtils.isNotEmpty(getExtension(multipartFile))) {
      fileName.append(STR_DOT).append(getExtension(multipartFile));
    }
    final File file = getFile(targetDirPath + "/" + fileName);
    try {
      FileCopyUtils.copy(multipartFile.getBytes(), file);
    } catch (final IOException e) {
      log.warn(ExceptionUtils.getStackTrace(e));
      throw new BusinessException(e);
    }
    return file;
  }

  public String uploadFile(final MultipartFile multipartFile, final String targetDirPath) {
    return Optional.ofNullable(multipartFile)
        .map(
            item -> {
              validateFile(item);
              checkExistingDirectory(targetDirPath);
              final File file = uploadMultipartFile(targetDirPath, item);
              log.info(STR_INFO_MESSAGE, file.getAbsolutePath());
              return file.getName();
            })
        .orElse("error");
  }

  // 업로드 하려는 파일의 검증(MultipartFile 이용)
  public void validateFile(final MultipartFile multipartFile) {
    if (FileType.ILLEGAL.extList.contains(getExtension(multipartFile))) {
      log.warn(
          "{}{}", ExceptionCode.FAIL_FILE_SIZE.toString(), multipartFile.getOriginalFilename());
      throw new BusinessException(ExceptionCode.FAIL_FILE_SIZE);
    }
    if (FileType.ILLEGAL.mimeTypeList.contains(getMimeType(multipartFile))) {
      log.warn(
          "{}{}", ExceptionCode.FAIL_FILE_MIMETYPE.toString(), multipartFile.getOriginalFilename());
      throw new BusinessException(ExceptionCode.FAIL_FILE_MIMETYPE);
    }
  }

  // 업로드된 파일의 검증(File 이용)
  public void validateFile(final File file) {
    if (FileType.ILLEGAL.extList.contains(getExtension(file))) {
      log.warn("{}{}", ExceptionCode.FAIL_FILE_SIZE.toString(), file.getAbsolutePath());
      throw new BusinessException(ExceptionCode.FAIL_FILE_SIZE);
    }
    if (FileType.ILLEGAL.mimeTypeList.contains(getMimeType(file))) {
      log.warn("{}{}", ExceptionCode.FAIL_FILE_MIMETYPE.toString(), file.getAbsolutePath());
      throw new BusinessException(ExceptionCode.FAIL_FILE_MIMETYPE);
    }
  }

  // 업로드 하려는 파일의 검증(MultipartFile 이용)
  public void validateFile(final MultipartFile multipartFile, final FileType fileType) {
    try {
      if (!fileType.extList.contains(getExtension(multipartFile))) {
        log.warn(
            "{}{}", ExceptionCode.FAIL_FILE_SIZE.toString(), multipartFile.getOriginalFilename());
        throw new BusinessException(ExceptionCode.FAIL_FILE_SIZE);
      }
      if (!fileType.mimeTypeList.contains(getMimeType(multipartFile))) {
        log.warn(
            "{}{}",
            ExceptionCode.FAIL_FILE_MIMETYPE.toString(),
            multipartFile.getOriginalFilename());
        throw new BusinessException(ExceptionCode.FAIL_FILE_MIMETYPE);
      }

      validateFile(multipartFile);
    } catch (final BusinessException e) {
      throw setResultCodeByFileType(e, fileType);
    }
  }

  // 업로드된 파일의 검증(File 이용)
  public void validateFile(final File file, final FileType fileType) {
    try {
      if (!fileType.extList.contains(getExtension(file))) {
        log.warn("{}{}", ExceptionCode.FAIL_FILE_SIZE.toString(), file.getAbsolutePath());
        throw new BusinessException(ExceptionCode.FAIL_FILE_SIZE);
      }
      if (!fileType.mimeTypeList.contains(getMimeType(file))) {
        log.warn("{}{}", ExceptionCode.FAIL_FILE_MIMETYPE.toString(), file.getAbsolutePath());
        throw new BusinessException(ExceptionCode.FAIL_FILE_MIMETYPE);
      }

      validateFile(file);
    } catch (final BusinessException e) {
      throw setResultCodeByFileType(e, fileType);
    }
  }

  private BusinessException setResultCodeByFileType(
      final BusinessException e, final FileType fileType) {
    if (FileType.IMAGE.equals(fileType)) {
      if (e.isEquals(ExceptionCode.FAIL_FILE_SIZE)) {
        return new BusinessException(ExceptionCode.FAIL_IMAGE_EXT);
      } else if (e.isEquals(ExceptionCode.FAIL_FILE_MIMETYPE)) {
        return new BusinessException(ExceptionCode.FAIL_IMAGE_MIMETYPE);
      }
    } else if (FileType.EXCEL.equals(fileType)) {
      if (e.isEquals(ExceptionCode.FAIL_FILE_SIZE)) {
        return new BusinessException(ExceptionCode.FAIL_EXCEL_EXT);
      } else if (e.isEquals(ExceptionCode.FAIL_FILE_MIMETYPE)) {
        return new BusinessException(ExceptionCode.FAIL_EXCEL_MIMETYPE);
      }
    } else if (FileType.WORD.equals(fileType)) {
      if (e.isEquals(ExceptionCode.FAIL_FILE_SIZE)) {
        throw new BusinessException(ExceptionCode.FAIL_WORD_EXT);
      } else if (e.isEquals(ExceptionCode.FAIL_FILE_MIMETYPE)) {
        throw new BusinessException(ExceptionCode.FAIL_WORD_MIMETYPE);
      }
    } else if (FileType.PDF.equals(fileType)) {
      if (e.isEquals(ExceptionCode.FAIL_FILE_SIZE)) {
        throw new BusinessException(ExceptionCode.FAIL_PDF_EXT);
      } else if (e.isEquals(ExceptionCode.FAIL_FILE_MIMETYPE)) {
        throw new BusinessException(ExceptionCode.FAIL_PDF_MIMETYPE);
      }
    }
    return e;
  }

  public boolean isFileType(final MultipartFile multipartFile, final FileType fileType) {
    return (fileType.extList.contains(getExtension(multipartFile))
        && fileType.mimeTypeList.contains(getMimeType(multipartFile)));
  }

  public boolean isFileType(final File file, final FileType fileType) {
    return (fileType.extList.contains(getExtension(file))
        && fileType.mimeTypeList.contains(getMimeType(file)));
  }

  public String getMimeType(final MultipartFile multipartFile) {
    try {
      if (multipartFile == null) {
        return null;
      }
      return TIKA_INSTANCE.detect(multipartFile.getBytes()).toLowerCase();
    } catch (final IOException e) {
      log.warn(ExceptionUtils.getStackTrace(e));
      throw new BusinessException(e);
    }
  }

  public String getMimeType(final File file) {
    try {
      if (file == null) {
        return null;
      }
      return TIKA_INSTANCE.detect(file).toLowerCase();
    } catch (final IOException e) {
      log.warn(ExceptionUtils.getStackTrace(e));
      throw new BusinessException(e);
    }
  }

  public String getFullName(final MultipartFile multipartFile) {
    if (multipartFile == null) {
      return null;
    }
    return getFullName(multipartFile.getOriginalFilename());
  }

  public String getFullName(final File file) {
    if (file == null) {
      return null;
    }
    return getFullName(file.getName());
  }

  public String getFullName(final String fileName) {
    return FilenameUtils.getName(fileName);
  }

  public String getBaseName(final MultipartFile multipartFile) {
    if (multipartFile == null) {
      return null;
    }
    return getBaseName(multipartFile.getOriginalFilename());
  }

  public String getBaseName(final File file) {
    if (file == null) {
      return null;
    }
    return getBaseName(file.getName());
  }

  public String getBaseName(final String fileName) {
    return FilenameUtils.getBaseName(fileName);
  }

  public String getExtension(final MultipartFile multipartFile) {
    if (multipartFile == null) {
      return null;
    }
    return getExtension(multipartFile.getOriginalFilename());
  }

  public String getExtension(final File file) {
    if (file == null) {
      return null;
    }
    return getExtension(file.getName());
  }

  public String getExtension(final String fileName) {
    return FilenameUtils.getExtension(fileName).toLowerCase(Locale.getDefault());
  }

  public enum FileType {
    IMAGE(
        Set.of("gif", "jpg", "jpeg", "tif", "tiff", "png", "bmp"),
        Set.of(
            "image/gif",
            "image/jpeg",
            "image/pjpeg",
            "image/tiff",
            "image/x-tiff",
            "image/png",
            "image/bmp")),
    EXCEL(
        Set.of("xlsx", "xls"),
        Set.of(
            "application/excel",
            "application/vnd.ms-excel",
            "application/x-excel",
            "application/x-msexcel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
    WORD(
        Set.of("docx", "doc", "dotx"),
        Set.of(
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.template")),
    PDF(Set.of("pdf"), Set.of("application/pdf", "application/x-pdf")),
    ILLEGAL(
        Set.of("exe", "sh", "csh", "ai"),
        Set.of(
            "application/octet-stream",
            "application/x-sh",
            "application/x-shar",
            "text/x-script.sh",
            "application/x-csh",
            "text/x-script.csh",
            "application/postscript"));
    private final Set<String> extList;
    private final Set<String> mimeTypeList;

    FileType(final Set<String> extList, final Set<String> mimeTypeList) {
      this.extList = extList;
      this.mimeTypeList = mimeTypeList;
    }
  }
}
