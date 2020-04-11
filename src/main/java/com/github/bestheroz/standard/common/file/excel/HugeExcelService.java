package com.github.bestheroz.standard.common.file.excel;

import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.util.DateUtils;
import com.github.bestheroz.standard.common.util.FileUtils;
import com.github.bestheroz.standard.context.abstractview.AbstractExcelXView;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.session.SqlSession;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Slf4j
public class HugeExcelService extends AbstractExcelXView {
    public static final String VIEW_NAME = "hugeExcelView";

    @Resource private SqlSession sqlSession;

    @SuppressWarnings("DuplicatedCode")
    @Override
    protected void buildExcelDocument(final Map<String, Object> model, final SXSSFWorkbook workbook, final HttpServletRequest request, final HttpServletResponse response) {
        @SuppressWarnings("unchecked") final List<ExcelVO> excelVOs = (List<ExcelVO>) model.get(AbstractExcelXView.EXCEL_VOS);
        final String sql = (String) model.get(AbstractExcelXView.SQL);
        final String fileName = FileUtils.getEncodedFileName(request, (String) model.get(AbstractExcelXView.FILE_NAME));

        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "_" + DateUtils.getStringNow(DateUtils.YYYYMMDDHHMMSS) + AbstractExcelXView.EXTENSION + ";");

        final SXSSFSheet sheet = workbook.createSheet("report");
        sheet.setRandomAccessWindowSize(100);

        this.createColumnLabel(sheet, excelVOs);

        this.addRowData(sheet, excelVOs, sql);
        this.autoSizeColumn(sheet, excelVOs);
    }

    @SuppressWarnings("DuplicatedCode")
    private void createColumnLabel(final SXSSFSheet sheet, final List<ExcelVO> excelVOs) {
        final SXSSFRow row0 = sheet.createRow(0);
        final CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 0, excelVOs.size() - 1);
        sheet.addMergedRegion(mergedRegion);
        final SXSSFCell cell10 = row0.createCell(0);
        cell10.setCellValue("This report was generated at " + DateUtils.getStringNow(DateUtils.YYYY_MM_DD_HH_MM_SS));

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
            cell.setCellValue(excelVOs.get(i).getTitle());
            cell.setCellStyle(cellStyle);
        }
    }

    private void addRowData(final SXSSFSheet sheet, final List<ExcelVO> excelVOs, final String sql) {
        try (final Connection connection = this.sqlSession.getConnection();
             final Statement createStatement = connection.createStatement();
             final ResultSet executeQuery = createStatement.executeQuery(sql)) {

            for (int i = 0; executeQuery.next(); i++) {
                if (i != 0 && i % 200 == 0) {
                    log.debug("[Excel]{} write {} rows", sheet.getSheetName(), i + 1);
                }

                final SXSSFRow row = sheet.createRow(3 + i);
                for (int j = 0; j < excelVOs.size(); j++) {
                    final Object object = executeQuery.getObject(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, excelVOs.get(j).getDbColName()));
                    final String strData;
                    if (object != null) {
                        strData = String.valueOf(object);
                    } else {
                        strData = "";
                    }
                    this.writeColumnData(excelVOs, j, row.createCell(j), strData);
                }
            }
        } catch (final Throwable e) {
            log.warn(ExceptionUtils.getStackTrace(e));
            throw new BusinessException(e);
        }
    }
}
