package com.github.bestheroz.standard.common.exception;

public enum ExceptionCode {
  SUCCESS_NORMAL("S000", "성공"),

  SUCCESS_LOGOUT("S001", "로그아웃 되었습니다."),

  SUCCESS_TRY_NEW_PASSWORD("S002", "비밀번호 초기화"),

  ERROR_SYSTEM("E000", "시스템에러"),

  ERROR_FILE_NOT_FOUND("E001", "파일이 존재하지 않습니다."),

  ERROR_TRANSFORM_DATA("E002", "데이터 변환 실패"),

  ERROR_DIR_PATH_MUST_ENDS_WITH_SLASH("E003", "디렉토리 경로는 항상 '/' 으로 끝나야만 합니다."),

  ERROR_ILLEGAL_BLOCK_SIZE_AES("E004", "암/복호화 오류 발생"),

  FAIL_INVALID_REQUEST("F000", "올바르지 않은 요청"),

  FAIL_INVALID_PARAMETER("F001", "올바르지 않은 파라미터"),

  FAIL_NOT_ALLOWED_MEMBER("F002", "허용되지 않는 사용자"),

  FAIL_LOGIN_CLOSED("F003", "계정 차단"),

  FAIL_LOGIN_FAIL_CNT("F004", "로그인 실패 회수 초과"),

  FAIL_TRY_LOGIN_FIRST("F005", "처리를 위해 로그인이 필요합니다."),

  FAIL_FILE_MIMETYPE("F006", "올바르지 않은 파일 내용입니다."),

  FAIL_FILE_EXT("F007", "올바르지 않은 파일 확장자 입니다."),

  FAIL_IMAGE_MIMETYPE("F008", "올바르지 않은 이미지 형식입니다."),

  FAIL_IMAGE_EXT("F009", "올바르지 않은 이미지 확장자 입니다."),

  FAIL_EXCEL_MIMETYPE("F010", "올바르지 않은 엑셀 형식입니다."),

  FAIL_EXCEL_EXT("F011", "올바르지 않은 엑셀 확장자 입니다."),

  FAIL_WORD_MIMETYPE("F012", "올바르지 않은 워드문서 형식입니다."),

  FAIL_WORD_EXT("F013", "올바르지 않은 워드문서 확장자 입니다."),

  FAIL_PDF_MIMETYPE("F014", "올바르지 않은 PDF 문서 형식입니다."),

  FAIL_PDF_EXT("F015", "올바르지 않은 PDF 문서 확장자 입니다."),

  FAIL_FILE_SIZE("F016", "최대 2MB 이하의 파일 첨부만 가능합니다."),

  FAIL_NO_DATA_SUCCESS("F017", "처리건 없음."),

  FAIL_UNIQUE_CONSTRAINT_VIOLATED("F018", "이미 존재하는 데이터입니다."),

  FAIL_CANNOT_INSERT_NULL("F019", "필수 값을 채워주세요."),

  FAIL_VALUE_TOO_LARGE_FOR_COLUMN("F020", "입력하신 값이 너무 큽니다."),

  FAIL_MATCH_OLD_PASSWORD("F021", "이전 비밀번호가 올바르지 않습니다."),

  FAIL_MATCH_PASSWORD("F022", "비밀번호가 올바르지 않습니다.");

  private final String code;
  private final String message;

  ExceptionCode(final String code, final String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return this.code;
  }

  public String getMessage() {
    return this.message;
  }

  @Override
  public String toString() {
    return this.name() + "(\"" + this.code + "\", \"" + this.message + "\");";
  }
}
