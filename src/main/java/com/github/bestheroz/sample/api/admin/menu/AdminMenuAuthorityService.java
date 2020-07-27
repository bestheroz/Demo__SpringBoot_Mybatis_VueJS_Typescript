package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.entity.menuauthority.TableMenuAuthorityEntity;
import com.github.bestheroz.sample.api.entity.menuauthority.TableMenuAuthorityRepository;
import com.github.bestheroz.sample.api.menu.MenuService;
import com.github.bestheroz.standard.common.util.MapperUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminMenuAuthorityService {
    @Resource private MenuService menuService;
    @Resource private TableMenuAuthorityRepository tableMenuAuthorityRepository;

    public List<AdminMenuAuthorityVO> getList(final Integer authority) {
        final List<AdminMenuAuthorityVO> result = new ArrayList<>();
        final Optional<TableMenuAuthorityEntity> tableMenuAuthorityVO = this.tableMenuAuthorityRepository.findById(authority);
        final boolean present = tableMenuAuthorityVO.isPresent();
        this.menuService.getMenuList().forEach(item -> {
            final AdminMenuAuthorityVO adminMenuAuthorityVO = MapperUtils.toObject(item, AdminMenuAuthorityVO.class);
            if (present) {
                adminMenuAuthorityVO.setChecked(item.getId().equals(1) || StringUtils.contains(tableMenuAuthorityVO.get().getMenuIdList(), "^|" + item.getId() + ","));
            }
            result.add(adminMenuAuthorityVO);
        });
        result.get(0).setChecked(true);
        return result;
    }
}
