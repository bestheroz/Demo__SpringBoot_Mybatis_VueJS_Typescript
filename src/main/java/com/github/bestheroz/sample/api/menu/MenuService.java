package com.github.bestheroz.sample.api.menu;

import com.github.bestheroz.sample.api.admin.menu.AdminMenuDAO;
import com.github.bestheroz.standard.common.util.SessionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Resource private MenuDAO menuDAO;
    @Resource private AdminMenuDAO adminMenuDAO;

    public List<MenuVO> getMenuList() {
        final List<MenuVO> list = this.menuDAO.getList(SessionUtils.getAttributeInteger("authority"));
        final List<MenuVO> result = list.stream().filter(item -> item.getLevel().equals(2)).collect(Collectors.toList());
        result.stream().forEach(item -> {
            item.setChildren(
                    list.stream().filter(item2 -> item2.getLevel().equals(3) && item2.getParentId().equals(item.getId())).collect(Collectors.toList()));
        });

//        for (final MenuVO vo : list) {
//            if (vo.getLevel() == 1) {
//                final MenuVO menuVO = new MenuVO();
//                menuVO.setTitle(vo.getMenuNmKor());
//                menuVO.setIcon(vo.getMenuIcon());
//                menuVO.setTo(vo.getUrl());
//                menuVO.setType(vo.getMenuType());
//                menuVO.setChildren(new ArrayList<>());
//                menuVO.setChecked(true);

//
//                for (final MenuVO vo2 : list) {
//                    if (vo.getId().equals(vo2.getParentId())) {
//                        final MenuVO menuVO2 = new MenuVO();
//                        menuVO2.setTitle(vo2.getMenuNmKor());
//                        menuVO2.setIcon(vo2.getMenuIcon());
//                        menuVO2.setTo(vo2.getUrl());
//                        menuVO2.setType(vo2.getMenuType());
//                        menuVO.setChecked(true);
//                        result.get(result.size() - 1).getChildren().add(menuVO2);
//                    }
//                }
//            }
//        }
        return result;
    }
}
