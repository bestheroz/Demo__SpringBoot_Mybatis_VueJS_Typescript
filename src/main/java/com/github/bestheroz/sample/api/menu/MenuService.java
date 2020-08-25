package com.github.bestheroz.sample.api.menu;

import com.github.bestheroz.sample.api.entity.menu.TableMenuEntity;
import com.github.bestheroz.sample.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.standard.common.util.MapperUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Resource private TableMenuRepository tableMenuRepository;

    @Cacheable(value = "drawerCache", key = "#authority")
    public List<DrawerVO> getDrawerList(final Integer authority) {
        final List<DrawerVO> result;
        if (authority.equals(999)) {
            result = this.tableMenuRepository.getItemsByLevel2().stream().map(MenuService::convertTableMenuEntityToDrawerVO).collect(Collectors.toList());
            result.forEach(
                    item -> item
                            .setChildren(this.tableMenuRepository.getItemsByLevel3AndParentId(item.getId()).stream().map(MenuService::convertTableMenuEntityToDrawerVO).collect(Collectors.toList())));
        } else {
            result = this.tableMenuRepository.getItemsByLevel2AndAuthority(authority).stream().map(MenuService::convertTableMenuEntityToDrawerVO).collect(Collectors.toList());
            result.forEach(item -> item.setChildren(
                    this.tableMenuRepository.getItemsByLevel3AndAuthorityAndParentId(authority, item.getId()).stream().map(MenuService::convertTableMenuEntityToDrawerVO)
                            .collect(Collectors.toList())));
        }
        return result;
    }

    public List<MenuVO> getMenuList() {
        final List<MenuVO> result = this.tableMenuRepository.getItemsByLevel1().stream().map((TableMenuEntity vo) -> MenuService.convertTableMenuEntityToMenuVO(vo, 1)).collect(Collectors.toList());
        final List<MenuVO> list = this.tableMenuRepository.getItemsByLevel2().stream().map((TableMenuEntity vo) -> MenuService.convertTableMenuEntityToMenuVO(vo, 2)).collect(Collectors.toList());
        list.forEach(item2 -> {
            result.add(item2);
            result.addAll(
                    this.tableMenuRepository.getItemsByLevel3AndParentId(item2.getId()).stream().map((TableMenuEntity vo) -> MenuService.convertTableMenuEntityToMenuVO(vo, 3))
                            .collect(Collectors.toList()));
        });
        return result;
    }

    private static DrawerVO convertTableMenuEntityToDrawerVO(final TableMenuEntity vo) {
        final DrawerVO drawerVO = new DrawerVO();
        drawerVO.setId(vo.getId());
        drawerVO.setTitle(vo.getName());
        drawerVO.setIcon(vo.getIcon());
        drawerVO.setTo(StringUtils.defaultString(vo.getUrl()));
        drawerVO.setType(vo.getType());
        return drawerVO;
    }

    private static MenuVO convertTableMenuEntityToMenuVO(final TableMenuEntity vo, final Integer level) {
        final MenuVO menuVO = MapperUtils.toObject(vo, MenuVO.class);
        menuVO.setLevel(level);
        return menuVO;
    }
}
