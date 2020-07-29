package com.github.bestheroz.standard.common.code;

import com.github.bestheroz.sample.api.entity.code.TableCodeEntity;
import com.github.bestheroz.sample.api.entity.code.TableCodeRepository;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CodeService {
    @Resource private TableCodeRepository tableCodeRepository;

    public List<CodeVO> getCodeVOListByAuthority(final String codeGroup) {
        final Integer authority = AuthenticationUtils.getLoginVO().getAuthority();
        return this.getCodeVOList(codeGroup).stream().filter(item -> item.isAvailable() && item.getAuthority() <= authority)
                .map(item -> new CodeVO(item.getCode(), item.getName())).collect(Collectors.toList());
    }

    @Cacheable(value = "codeCache", key = "#codeGroup")
    public List<TableCodeEntity> getCodeVOList(final String codeGroup) {
        return Lists.newArrayList(this.tableCodeRepository.getItemsByKeyWithOrder(TableCodeEntity.class, Map.of("codeGroup", codeGroup), Set.of("displayOrder")).iterator());
    }
}
