package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.menuauthority.TableMenuAuthorityEntity;
import com.github.bestheroz.demo.api.entity.menuauthority.TableMenuAuthorityRepository;
import com.github.bestheroz.demo.api.menu.MenuService;
import com.github.bestheroz.standard.common.util.MapperUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class AdminMenuAuthorityService {
  @Resource
  private MenuService menuService;

  @Resource
  private TableMenuAuthorityRepository tableMenuAuthorityRepository;

  public List<AdminMenuAuthorityVO> getItems(final Integer authority) {
    final String menuIdList =
      this.tableMenuAuthorityRepository.getItem(
          TableMenuAuthorityEntity.class,
          Map.of("authority", authority)
        )
        .orElseGet(TableMenuAuthorityEntity::new)
        .getMenuIdList();
    return this.menuService.getMenuList()
      .stream()
      .map(
        item -> {
          final AdminMenuAuthorityVO adminMenuAuthorityVO = MapperUtils.toObject(
            item,
            AdminMenuAuthorityVO.class
          );
          adminMenuAuthorityVO.setChecked(
            item.getId().equals(1) ||
            StringUtils.contains(menuIdList, "^|" + item.getId() + ",")
          );
          return adminMenuAuthorityVO;
        }
      )
      .collect(Collectors.toList());
  }
}
