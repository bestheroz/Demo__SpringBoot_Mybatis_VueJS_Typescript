package com.github.bestheroz.standard.common.file.excel;

import com.github.bestheroz.standard.common.util.FileUtils;
import com.github.bestheroz.standard.common.util.MapperUtils;
import com.github.bestheroz.standard.context.abstractview.AbstractExcelXView;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

@Slf4j
public class ExcelService extends AbstractExcelXView {
  public static final String VIEW_NAME = "excelView";

  @Override
  protected void buildExcelDocument(
      final Map<String, Object> model,
      final SXSSFWorkbook workbook,
      final HttpServletRequest request,
      final HttpServletResponse response) {
    @SuppressWarnings("unchecked")
    final List<ExcelVO> excelVOs = (List<ExcelVO>) model.get(AbstractExcelXView.EXCEL_VOS);
    final List<?> listData = (List<?>) model.get(AbstractExcelXView.LIST_DATA);
    final String fileName =
        FileUtils.getEncodedFileName(request, (String) model.get(AbstractExcelXView.FILE_NAME));

    final String now = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
    response.setHeader(
        "Content-Disposition",
        "attachment;filename=" + fileName + "_" + now + AbstractExcelXView.EXTENSION + ";");

    final SXSSFSheet sheet = workbook.createSheet("report");
    sheet.setRandomAccessWindowSize(100);

    this.createColumnLabel(sheet, excelVOs, now);

    this.addRowData(sheet, excelVOs, listData);
    this.autoSizeColumn(sheet, excelVOs);
  }

  private void createColumnLabel(
      final SXSSFSheet sheet, final List<ExcelVO> excelVOs, final String now) {
    final SXSSFRow row0 = sheet.createRow(0);
    final CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, excelVOs.size() - 1);
    sheet.addMergedRegion(mergedRegion);
    final SXSSFCell cell10 = row0.createCell(0);
    cell10.setCellValue("This report was generated at " + now);

    final XSSFCellStyle cellStyle = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    final XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
    font.setBold(true);
    cellStyle.setFont(font);

    final SXSSFRow row2 = sheet.createRow(2);
    for (int i = 0; i < excelVOs.size(); i++) {
      final SXSSFCell cell = row2.createCell(i);
      cell.setCellValue(excelVOs.get(i).title());
      cell.setCellStyle(cellStyle);
    }
  }

  private void addRowData(
      final SXSSFSheet sheet, final List<ExcelVO> excelVOs, final List<?> listData) {
    for (int i = 0; i < listData.size(); i++) {
      if (i != 0 && i % 100 == 0) {
        log.debug("[Excel]{} write {} rows", sheet.getSheetName(), i + 1);
      }
      final SXSSFRow row = sheet.createRow(3 + i);
      final Map<String, Object> data = MapperUtils.toMap(listData.get(i));
      for (int j = 0; j < excelVOs.size(); j++) {
        Object value = data;
        for (final String key : StringUtils.split(excelVOs.get(j).cellKey(), ".")) {
          if (Objects.nonNull(value)) {
            if (value instanceof HashMap map) {
              value = (map).get(key);
            } else if (!value.getClass().isPrimitive() && !(value instanceof List)) {
              value = (MapperUtils.toMap(value)).get(key);
            }
          }
        }
        if (Objects.nonNull(value)) {
          this.writeColumnData(excelVOs, j, row.createCell(j), String.valueOf(value));
        }
      }
    }
  }
}
