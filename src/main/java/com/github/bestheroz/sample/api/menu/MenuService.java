package com.github.bestheroz.sample.api.menu;

import com.github.bestheroz.sample.api.admin.menu.AdminMenuDAO;
import com.github.bestheroz.sample.api.entity.samplemenumst.TableSampleMenuMstVO;
import com.github.bestheroz.standard.common.util.SessionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    @Resource private MenuDAO menuDAO;
    @Resource private AdminMenuDAO adminMenuDAO;

    public List<MenuVO> getMenuList() {
        final List<TableSampleMenuMstVO> list;
        if ("000".equals(SessionUtils.getAttribute("levelcod"))) {
            list = this.adminMenuDAO.getList();
        } else {
            list = this.menuDAO.getList(SessionUtils.getAttribute("levelcod"));
        }
        final List<MenuVO> result = new ArrayList<>();
        for (final TableSampleMenuMstVO tableMgmtMenumstVO : list) {
            if (tableMgmtMenumstVO.getLvl() == 2) {
                final MenuVO menuVO = new MenuVO();
                menuVO.setTitle(tableMgmtMenumstVO.getMenuNmKor());
                menuVO.setIcon(tableMgmtMenumstVO.getMenuIcon());
                menuVO.setTo(tableMgmtMenumstVO.getUrl());
                menuVO.setType(tableMgmtMenumstVO.getMenuType());
                menuVO.setChildren(new ArrayList<>());
                menuVO.setChecked(true);
                result.add(menuVO);
                for (final TableSampleMenuMstVO tableMgmtMenumstVO2 : list) {
                    if (tableMgmtMenumstVO.getMenuId().equals(tableMgmtMenumstVO2.getPMenuId())) {
                        final MenuVO menuVO2 = new MenuVO();
                        menuVO2.setTitle(tableMgmtMenumstVO2.getMenuNmKor());
                        menuVO2.setIcon(tableMgmtMenumstVO2.getMenuIcon());
                        menuVO2.setTo(tableMgmtMenumstVO2.getUrl());
                        menuVO2.setType(tableMgmtMenumstVO2.getMenuType());
                        menuVO.setChecked(true);
                        result.get(result.size() - 1).getChildren().add(menuVO2);
                    }
                }
            }
        }
        return result;
    }
}
