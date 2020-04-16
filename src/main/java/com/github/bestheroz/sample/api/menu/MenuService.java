package com.github.bestheroz.sample.api.menu;

import com.github.bestheroz.standard.common.util.SessionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Resource private MenuDAO menuDAO;

    public List<DrawerVO> getDrawerList() {
        final List<MenuVO> list = this.menuDAO.getList(SessionUtils.getAttributeInteger("authority"));
        final List<DrawerVO> result = list.stream().filter(item -> item.getLevel().equals(2)).map(this::convertMenuVOToDrawerVO).collect(Collectors.toList());
        result.stream().forEach(item -> {
            item.setChildren(list.stream().filter(item2 -> item2.getLevel().equals(3) && item2.getParentId().equals(item.getId())).map(this::convertMenuVOToDrawerVO).collect(Collectors.toList()));
        });
        return result;
    }

    public List<MenuVO> getMenuList() {
        final List<MenuVO> list = this.menuDAO.getList(SessionUtils.getAttributeInteger("authority"));
        final List<MenuVO> result = new ArrayList<>();
        result.add(list.get(0));
        list.stream().filter(item2 -> item2.getLevel().equals(2)).forEach(item2 -> {
            result.add(item2);
            result.addAll(list.stream().filter(item -> item.getLevel().equals(3)).filter(item3 -> item3.getParentId().equals(item2.getId())).collect(Collectors.toList()));
        });
        return result;
    }

    private DrawerVO convertMenuVOToDrawerVO(final MenuVO vo) {
        final DrawerVO drawerVO = new DrawerVO();
        drawerVO.setId(vo.getId());
        drawerVO.setTitle(vo.getName());
        drawerVO.setIcon(vo.getIcon());
        drawerVO.setTo(vo.getUrl());
        drawerVO.setType(vo.getType());
        if (vo.getLevel().equals(2)) {
            drawerVO.setChildren(new ArrayList<>());
        }
        return drawerVO;
    }
}
