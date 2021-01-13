package com.github.bestheroz.standard.context.abstractview;

import com.github.bestheroz.standard.common.code.CodeVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.file.excel.ExcelVO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractView;

@Slf4j
public abstract class AbstractExcelXView extends AbstractView {
  /** The extension to look for existing templates */
  public static final String EXTENSION = ".xlsx";

  public static final String FILE_NAME = "fileName";
  public static final String PASSWORD = "password";
  public static final String EXCEL_VOS = "excelVOs";
  public static final String SQL = "SQL";
  public static final String LIST_DATA = "listData";
  /** The content type for an Excel response */
  private static final String CONTENT_TYPE =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  private static final String ROW_NUMBER = "ROW_NUMBER";
  private XSSFCellStyle stringRightStyle;
  private XSSFCellStyle stringCenterStyle;
  private XSSFCellStyle numberStyle;
  private XSSFCellStyle doubleStyle;
  private XSSFCellStyle dateStyle;

  /** Default Constructor. * Sets the content type of the view to "application/vnd.ms-excel". */
  public AbstractExcelXView() {
    this.setContentType(CONTENT_TYPE);
  }

  public static void addHeaderOfRowNo(final List<ExcelVO> excelVOList, final String title) {
    addHeader(excelVOList, title, ROW_NUMBER, AbstractExcelXView.CellType.STRING_CENTER, null);
  }

  public static void addHeader(
      final List<ExcelVO> excelVOList,
      final String title,
      final String dbColName,
      final CellType cellType) {
    addHeader(excelVOList, title, dbColName, cellType, null);
  }

  public static void addHeader(
      final List<ExcelVO> excelVOList,
      final String title,
      final String dbColName,
      final CellType cellType,
      final List<CodeVO> codeList) {
    final ExcelVO excelVO = new ExcelVO();
    excelVO.setTitle(title);
    excelVO.setDbColName(dbColName);
    excelVO.setCellType(cellType);
    excelVO.setCodeList(codeList);
    if (cellType.equals(CellType.STRING)) {
      excelVO.setCharByte(1.2D);
    }
    excelVOList.add(excelVO);
  }

  @Override
  protected boolean generatesDownloadContent() {
    return true;
  }

  /** Renders the Excel view, given the specified model. */
  @Override
  protected final void renderMergedOutputModel(
      final Map<String, Object> model,
      final HttpServletRequest request,
      final HttpServletResponse response) {
    // java.lang.OutOfMemoryError: Java heap space 발생시...
    // -XX:PermSize=64M -XX:MaxPermSize=1000M
    try (final ByteArrayOutputStream baos = this.createTemporaryOutputStream();
        final SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
      workbook.setCompressTempFiles(true);

      this.stringRightStyle = (XSSFCellStyle) workbook.createCellStyle();
      this.stringRightStyle.setAlignment(HorizontalAlignment.RIGHT);

      this.stringCenterStyle = (XSSFCellStyle) workbook.createCellStyle();
      this.stringCenterStyle.setAlignment(HorizontalAlignment.CENTER);

      this.numberStyle = (XSSFCellStyle) workbook.createCellStyle();
      this.numberStyle.setDataFormat(
          workbook.getCreationHelper().createDataFormat().getFormat("#,##0"));

      this.doubleStyle = (XSSFCellStyle) workbook.createCellStyle();
      this.doubleStyle.setDataFormat(
          workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00"));

      this.dateStyle = (XSSFCellStyle) workbook.createCellStyle();
      this.dateStyle.setAlignment(HorizontalAlignment.CENTER);

      log.debug("Created Excel Workbook from scratch");

      this.buildExcelDocument(model, workbook, request, response);

      log.debug("Excel Password : {}", model.get(PASSWORD));
      if (StringUtils.isNotEmpty((String) model.get(PASSWORD))) {
        final POIFSFileSystem fs = new POIFSFileSystem();
        final EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
        final Encryptor enc = info.getEncryptor();
        enc.confirmPassword(String.valueOf(model.get(PASSWORD)));
        final OutputStream os = enc.getDataStream(fs);
        workbook.write(os);
        final ServletOutputStream out = response.getOutputStream();
        fs.writeFilesystem(out);
        response.setContentType(this.getContentType());
        // response.setContentLength(baos.size());
        out.flush();
      } else {
        workbook.write(baos);
        this.writeToResponse(response, baos);
      }
    } catch (final Throwable e) {
      log.warn(ExceptionUtils.getStackTrace(e));
      response.setContentType("text/html;charset=utf-8");
      try (final PrintWriter pw = response.getWriter()) {
        pw.println("<script>");
        pw.println("alert('파일이 없습니다.');");
        pw.println("history.back();");
        pw.println("</script>");
      } catch (final IOException e1) {
        log.warn(ExceptionUtils.getStackTrace(e1));
        throw new BusinessException(e1);
      }
    }
  }

  protected SXSSFWorkbook getTemplateSource(final String url, final HttpServletRequest request)
      throws Exception {
    final ApplicationContext applicationContext = this.getApplicationContext();
    Optional.ofNullable(applicationContext)
        .orElseThrow(
            () -> {
              log.warn("applicationContext is null");
              return BusinessException.ERROR_SYSTEM;
            });
    final LocalizedResourceHelper helper = new LocalizedResourceHelper(applicationContext);
    final Locale userLocale = RequestContextUtils.getLocale(request);
    final Resource inputFile = helper.findLocalizedResource(url, EXTENSION, userLocale);

    // Create the Excel document from the source.
    if (log.isDebugEnabled()) {
      log.debug("Loading Excel workbook from {}.", inputFile);
    }
    // POIFSFileSystem fs = new POIFSFileSystem(inputFile.getInputStream());
    return new SXSSFWorkbook(new XSSFWorkbook(inputFile.getInputStream()));
  }

  protected abstract void buildExcelDocument(
      Map<String, Object> model,
      SXSSFWorkbook workbook,
      HttpServletRequest request,
      HttpServletResponse response);

  protected SXSSFCell getCell(final SXSSFSheet sheet, final int row, final int col) {
    final SXSSFRow sheetRow =
        Objects.requireNonNullElseGet(sheet.getRow(row), () -> sheet.createRow(row));
    return Objects.requireNonNullElseGet(sheetRow.getCell(col), () -> sheetRow.createCell(col));
  }

  protected void autoSizeColumn(final SXSSFSheet sheet, final List<ExcelVO> excelVOs) {
    sheet.trackAllColumnsForAutoSizing();
    for (int j = 0; j < excelVOs.size(); j++) {
      sheet.autoSizeColumn(j);
      // java.lang.IllegalArgumentException: The maximum column width for an individual cell is 255
      // characters. max 59999
      int columWidth = (int) (sheet.getColumnWidth(j) * excelVOs.get(j).getCharByte() + 300);
      columWidth = Math.max(columWidth, 3000);
      columWidth = Math.min(columWidth, 59999);
      sheet.setColumnWidth(j, columWidth);
    }
    sheet.untrackAllColumnsForAutoSizing();
  }

  protected void writeColumnData(
      final List<ExcelVO> excelVOs,
      final Integer columnIdx,
      final SXSSFCell cell,
      final String data) {
    if (excelVOs.get(columnIdx).getCellType().equals(CellType.INTEGER)) {
      this.setInteger(cell, data);
    } else if (excelVOs.get(columnIdx).getCellType().equals(CellType.DOUBLE)) {
      this.setDouble(cell, data);
    } else if (excelVOs.get(columnIdx).getCellType().equals(CellType.DATE)
        || excelVOs.get(columnIdx).getCellType().equals(CellType.DATE_YYYYMMDDHHMMSS)) {
      this.setDate(cell, Instant.parse(data).toString());
    } else if (excelVOs.get(columnIdx).getCellType().equals(CellType.DATE_YYYYMMDD)) {
      this.setDate(cell, Instant.parse(data).toString());
    } else {
      try {
        Optional.ofNullable(excelVOs.get(columnIdx).getCodeList())
            .flatMap(
                items -> items.stream().filter(item -> item.getValue().equals(data)).findFirst())
            .ifPresentOrElse(
                item -> {
                  final String value = item.getText();
                  if (excelVOs.get(columnIdx).getCellType().equals(CellType.STRING_CENTER)) {
                    this.setStringCenter(cell, value);
                  } else if (excelVOs.get(columnIdx).getCellType().equals(CellType.STRING_RIGHT)) {
                    this.setStringRight(cell, value);
                  } else {
                    this.setString(cell, value);
                  }
                },
                () -> {
                  if (excelVOs.get(columnIdx).getCellType().equals(CellType.STRING_CENTER)) {
                    this.setStringCenter(cell, data);
                  } else if (excelVOs.get(columnIdx).getCellType().equals(CellType.STRING_RIGHT)) {
                    this.setStringRight(cell, data);
                  } else {
                    this.setString(cell, data);
                  }
                });
      } catch (final Throwable e) {
        log.warn(ExceptionUtils.getStackTrace(e));
        this.setString(cell, data);
      }
    }
  }

  private void setString(final SXSSFCell cell, final String text) {
    cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
    cell.setCellValue(this.getSecureCellText(text));
  }

  private void setStringCenter(final SXSSFCell cell, final String text) {
    cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
    cell.setCellStyle(this.stringCenterStyle);
    cell.setCellValue(this.getSecureCellText(text));
  }

  private void setStringRight(final SXSSFCell cell, final String text) {
    cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
    cell.setCellStyle(this.stringRightStyle);
    cell.setCellValue(this.getSecureCellText(text));
  }

  private void setInteger(final SXSSFCell cell, final String text) {
    cell.setCellType(org.apache.poi.ss.usermodel.CellType.NUMERIC);
    cell.setCellStyle(this.numberStyle);
    try {
      cell.setCellValue((long) Double.parseDouble(this.getSecureCellText(text)));
    } catch (final Throwable e) {
      log.warn("Excel setInteger() error\n{}.", ExceptionUtils.getStackTrace(e));
      cell.setCellValue(this.getSecureCellText(text));
    }
  }

  private void setDouble(final SXSSFCell cell, final String text) {
    cell.setCellType(org.apache.poi.ss.usermodel.CellType.NUMERIC);
    cell.setCellStyle(this.doubleStyle);
    try {
      cell.setCellValue(Double.parseDouble(this.getSecureCellText(text)));
    } catch (final Throwable e) {
      log.warn("Excel setDouble() error\n{}.", ExceptionUtils.getStackTrace(e));
      cell.setCellValue(this.getSecureCellText(text));
    }
  }

  private void setDate(final SXSSFCell cell, final String text) {
    cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
    cell.setCellStyle(this.dateStyle);
    try {
      cell.setCellValue(this.getSecureCellText(text));
    } catch (final Throwable e) {
      log.warn("Excel setDate() error\n{}.", ExceptionUtils.getStackTrace(e));
    }
  }

  private String getSecureCellText(final String text) {
    if (StringUtils.isEmpty(text) || StringUtils.equals(text, "null")) {
      return StringUtils.EMPTY;
    } else {
      return text;
    }
  }

  public enum CellType {
    STRING,
    STRING_CENTER,
    STRING_RIGHT,
    INTEGER,
    DOUBLE,
    DATE,
    DATE_YYYYMMDD,
    DATE_YYYYMMDDHHMMSS
  }
}
