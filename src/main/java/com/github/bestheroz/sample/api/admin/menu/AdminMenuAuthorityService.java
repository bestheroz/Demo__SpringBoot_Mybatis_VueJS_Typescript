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
import java.util.Map;
import java.util.Optional;

@Service
public class AdminMenuAuthorityService {
    @Resource private MenuService menuService;
    @Resource private TableMenuAuthorityRepository tableMenuAuthorityRepository;

    public List<AdminMenuAuthorityVO> getItems(final Integer authority) {
        final List<AdminMenuAuthorityVO> result = new ArrayList<>();
        final Optional<TableMenuAuthorityEntity> oTableMenuAuthorityEntity = this.tableMenuAuthorityRepository.getItem(TableMenuAuthorityEntity.class, Map.of("authority", authority));
        final String menuIdList = oTableMenuAuthorityEntity.orElseGet(TableMenuAuthorityEntity::new).getMenuIdList();
        this.menuService.getMenuList().forEach(item -> {
            final AdminMenuAuthorityVO adminMenuAuthorityVO = MapperUtils.toObject(item, AdminMenuAuthorityVO.class);
            adminMenuAuthorityVO.setChecked(item.getId().equals(1) || StringUtils.contains(menuIdList, "^|" + item.getId() + ","));
            result.add(adminMenuAuthorityVO);
        });
        return result;
    }
}
