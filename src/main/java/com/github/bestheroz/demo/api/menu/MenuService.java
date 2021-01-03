package com.github.bestheroz.demo.api.menu;

import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuEntity;
import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuRepository;
import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.standard.common.util.MapperUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
  @Resource
  private TableMenuRepository tableMenuRepository;
  @Resource
  private TableMemberMenuRepository tableMemberMenuRepository;

  public List<MenuVO> getDrawerList(final Integer authority) {
    final List<MenuVO> parents;
    final List<TableMemberMenuEntity> items;
    if (authority.equals(999)) {
      items =
        MapperUtils.toArrayList(
          this.tableMenuRepository.getItemsWithOrder(
            List.of("displayOrder", "name")
          ),
          TableMemberMenuEntity.class
        );
      parents =
        MapperUtils.toArrayList(
          items
            .stream()
            .filter(menu -> menu.getParentId().equals(0))
            .collect(Collectors.toList()),
          MenuVO.class
        );
    } else {
      items =
        this.tableMemberMenuRepository.getItemsByKeyWithOrder(
          Map.of("authority", authority),
          List.of("displayOrder", "name")
        );
      parents =
        MapperUtils.toArrayList(
          items
            .stream()
            .filter(menu -> menu.getParentId().equals(0))
            .collect(Collectors.toList()),
          MenuVO.class
        );
    }

    parents.forEach(
      parent ->
        parent.setChildren(
          MapperUtils.toArrayList(
            items
              .stream()
              .filter(menu -> menu.getParentId().equals(parent.getId()))
              .collect(Collectors.toList()),
            MenuVO.class
          )
        )
    );
    return parents;
  }
}
