package com.github.bestheroz.standard.common.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CodeService {
    @Resource private CodeDAO codeDAO;

    public List<CodeVO> getCodeVOList(final String groupCode) {
        return this.codeDAO.getCodeVOList(groupCode);
    }
}
