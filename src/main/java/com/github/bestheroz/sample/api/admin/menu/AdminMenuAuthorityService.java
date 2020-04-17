package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.entity.menuauthority.TableMenuAuthorityRepository;
import com.github.bestheroz.sample.api.entity.menuauthority.TableMenuAuthorityVO;
import com.github.bestheroz.sample.api.menu.MenuService;
import com.github.bestheroz.sample.api.menu.MenuVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
        final Optional<TableMenuAuthorityVO> tableMenuAuthorityVO = this.tableMenuAuthorityRepository.findById(authority);
        final List<MenuVO> menuList = this.menuService.getMenuList();
        final boolean present = tableMenuAuthorityVO.isPresent();
        menuList.forEach(item -> {
            final AdminMenuAuthorityVO adminMenuAuthorityVO = new AdminMenuAuthorityVO();
            BeanUtils.copyProperties(item, adminMenuAuthorityVO);
            if (present) {
                adminMenuAuthorityVO.setChecked(item.getId().equals(1) || StringUtils.contains(tableMenuAuthorityVO.get().getMenuIdList(), "^|" + item.getId() + ","));
            }
            result.add(adminMenuAuthorityVO);
        });
        result.get(0).setChecked(true);
        return result;
    }
}
