package com.github.bestheroz.standard.common.file.excel;

import com.github.bestheroz.demo.api.code.CodeVO;
import com.github.bestheroz.standard.context.abstractview.AbstractExcelXView;
import java.util.List;

public record ExcelVO(
    String title,
    String cellKey,
    Double charByte, // 셀넓이를 추가로 주기 위함..(한글과 영어 넓이 다름.)
    AbstractExcelXView.CellType cellType,
    List<CodeVO<String>> codeList) {}
