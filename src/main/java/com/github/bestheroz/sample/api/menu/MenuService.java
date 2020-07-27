package com.github.bestheroz.sample.api.menu;

import com.github.bestheroz.sample.api.entity.menu.TableMenuEntity;
import com.github.bestheroz.sample.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.standard.common.util.MapperUtils;
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
            result = this.tableMenuRepository.getMenuListLevel2().stream().map(this::convertTableMenuVOToDrawerVO).collect(Collectors.toList());
            result.forEach(item -> item.setChildren(this.tableMenuRepository.getMenuListLevel3(item.getId()).stream().map(this::convertTableMenuVOToDrawerVO).collect(Collectors.toList())));
        } else {
            result = this.tableMenuRepository.getMenuListLevel2(authority).stream().map(this::convertTableMenuVOToDrawerVO).collect(Collectors.toList());
            result.forEach(item -> item.setChildren(this.tableMenuRepository.getMenuListLevel3(authority, item.getId()).stream().map(this::convertTableMenuVOToDrawerVO).collect(Collectors.toList())));
        }
        return result;
    }

    public List<MenuVO> getMenuList() {
        final List<MenuVO> result = this.tableMenuRepository.getMenuListLevel1().stream().map((TableMenuEntity vo) -> this.convertTableMenuVOToMenuVO(vo, 1)).collect(Collectors.toList());
        final List<MenuVO> list = this.tableMenuRepository.getMenuListLevel2().stream().map((TableMenuEntity vo) -> this.convertTableMenuVOToMenuVO(vo, 2)).collect(Collectors.toList());
        list.forEach(item2 -> {
            result.add(item2);
            result.addAll(this.tableMenuRepository.getMenuListLevel3(item2.getId()).stream().map((TableMenuEntity vo) -> this.convertTableMenuVOToMenuVO(vo, 3)).collect(Collectors.toList()));
        });
        return result;
    }

    private DrawerVO convertTableMenuVOToDrawerVO(final TableMenuEntity vo) {
        final DrawerVO drawerVO = new DrawerVO();
        drawerVO.setId(vo.getId());
        drawerVO.setTitle(vo.getName());
        drawerVO.setIcon(vo.getIcon());
        drawerVO.setTo(vo.getUrl());
        drawerVO.setType(vo.getType());
        return drawerVO;
    }

    private MenuVO convertTableMenuVOToMenuVO(final TableMenuEntity vo, final Integer level) {
        final MenuVO menuVO = MapperUtils.toObject(vo, MenuVO.class);
        menuVO.setLevel(level);
        return menuVO;
    }
}
