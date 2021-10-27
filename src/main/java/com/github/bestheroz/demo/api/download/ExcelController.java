package com.github.bestheroz.demo.api.download;

import com.github.bestheroz.demo.api.admin.AdminFilter;
import com.github.bestheroz.demo.api.admin.AdminService;
import com.github.bestheroz.demo.api.code.CodeVO;
import com.github.bestheroz.demo.repository.AdminRepository;
import com.github.bestheroz.standard.common.file.excel.ExcelService;
import com.github.bestheroz.standard.common.file.excel.ExcelVO;
import com.github.bestheroz.standard.context.abstractview.AbstractExcelXView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/excel/")
@CrossOrigin(
    value = {"*"},
    exposedHeaders = {"Content-Disposition"})
@Slf4j
@RequiredArgsConstructor
public class ExcelController {
  private final AdminRepository adminRepository;
  private final AdminService adminService;

  @GetMapping(value = "admins")
  public String excelAdmins(final Model model, final AdminFilter adminFilter) {
    final List<ExcelVO> excelVOList = new ArrayList<>();

    // header
    AbstractExcelXView.addHeader(excelVOList, "사용자 아이디", "adminId", ExcelService.CellType.STRING);
    AbstractExcelXView.addHeader(excelVOList, "사용자 명", "name", ExcelService.CellType.STRING);
    AbstractExcelXView.addHeader(
        excelVOList, "권한", "role.name", ExcelService.CellType.STRING_CENTER);
    AbstractExcelXView.addHeader(excelVOList, "만료일", "expired", ExcelService.CellType.DATE);
    AbstractExcelXView.addHeader(excelVOList, "사용 가능", "available", ExcelService.CellType.STRING);

    // excel maker
    this.excelMaker(
        model, "Admin_List", excelVOList, this.adminService.getAdmins(adminFilter).getContents());

    return ExcelService.VIEW_NAME;
  }

  /**
   * Excel Maker
   *
   * @param model 모델
   * @param fileName 파일명
   * @param excelVOList 엑셀 VO 리스트
   * @param repository 대상 레파지토리
   */
  private void excelMaker(
      final Model model,
      final String fileName,
      final List<ExcelVO> excelVOList,
      final List<?> repository) {

    // 작업일시, 작업자 공통 추가
    AbstractExcelXView.addHeader(excelVOList, "작업 일시", "updated", ExcelService.CellType.DATE);
    AbstractExcelXView.addHeader(
        excelVOList,
        "작업자",
        "updatedBy",
        ExcelService.CellType.STRING,
        this.adminRepository.getCodes().stream()
            .map(c -> new CodeVO<>(c.getValue().toString(), c.getText()))
            .collect(Collectors.toList()));

    model.addAttribute(AbstractExcelXView.FILE_NAME, fileName);
    model.addAttribute(AbstractExcelXView.EXCEL_VOS, excelVOList);
    model.addAttribute(AbstractExcelXView.LIST_DATA, repository);
  }
}
