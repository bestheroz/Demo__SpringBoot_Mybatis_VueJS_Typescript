package com.github.bestheroz.sample.api.admin.codegroup;

import com.github.bestheroz.sample.api.entity.code.TableCodeEntity;
import com.github.bestheroz.sample.api.entity.code.TableCodeRepository;
import com.github.bestheroz.sample.api.entity.codegroup.TableCodeGroupEntity;
import com.github.bestheroz.sample.api.entity.codegroup.TableCodeGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Service
public class AdminCodeGroupService {
    @Resource private TableCodeGroupRepository tableCodeGroupRepository;
    @Resource private TableCodeRepository tableCodeRepository;

    @Transactional
    public void delete(final String codeGroup) {
        this.tableCodeRepository.delete(TableCodeEntity.class, Map.of("codeGroup", codeGroup));
        this.tableCodeGroupRepository.delete(TableCodeGroupEntity.class, Map.of("codeGroup", codeGroup));
    }
}
