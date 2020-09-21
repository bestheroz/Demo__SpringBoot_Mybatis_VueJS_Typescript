package com.github.bestheroz.demo.api.admin.codegroup;

import com.github.bestheroz.demo.api.entity.code.TableCodeEntity;
import com.github.bestheroz.demo.api.entity.code.TableCodeRepository;
import com.github.bestheroz.demo.api.entity.codegroup.TableCodeGroupEntity;
import com.github.bestheroz.demo.api.entity.codegroup.TableCodeGroupRepository;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AdminCodeGroupService {
  @Resource
  private TableCodeGroupRepository tableCodeGroupRepository;

  @Resource
  private TableCodeRepository tableCodeRepository;

  @Transactional
  public void delete(final String codeGroup) {
    this.tableCodeRepository.delete(
        TableCodeEntity.class,
        Map.of("codeGroup", codeGroup)
      );
    this.tableCodeGroupRepository.delete(
        TableCodeGroupEntity.class,
        Map.of("codeGroup", codeGroup)
      );
  }
}
