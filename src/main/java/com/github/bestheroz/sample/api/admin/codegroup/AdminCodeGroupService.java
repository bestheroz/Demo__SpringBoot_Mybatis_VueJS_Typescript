package com.github.bestheroz.sample.api.admin.codegroup;

import com.github.bestheroz.sample.api.entity.code.TableCodeRepository;
import com.github.bestheroz.sample.api.entity.codegroup.TableCodeGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class AdminCodeGroupService {
    @Resource private TableCodeGroupRepository tableCodeGroupRepository;
    @Resource private TableCodeRepository tableCodeRepository;

    @Transactional
    public void delete(final String codeGroup) {
        this.tableCodeRepository.deleteByCodeGroup(codeGroup);
        this.tableCodeGroupRepository.deleteById(codeGroup);
    }
}
