package com.github.bestheroz.standard.common.file.excel;

import com.github.bestheroz.demo.api.code.CodeVO;
import com.github.bestheroz.standard.context.abstractview.AbstractExcelXView;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExcelVO {
  private String title;
  private String cellKey;
  private Double charByte; // 셀넓이를 추가로 주기 위함..(한글과 영어 넓이 다름.)
  private AbstractExcelXView.CellType cellType;
  private List<CodeVO<String>> codeList;
}
