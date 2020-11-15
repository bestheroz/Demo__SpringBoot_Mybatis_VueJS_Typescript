package com.github.bestheroz.demo.api.menu;

import com.github.bestheroz.demo.api.entity.menu.TableMenuEntity;
import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.standard.common.util.MapperUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
  @Resource
  private TableMenuRepository tableMenuRepository;

  private DrawerVO convertTableMenuEntityToDrawerVO(final TableMenuEntity vo) {
    final DrawerVO drawerVO = new DrawerVO();
    drawerVO.setId(vo.getId());
    drawerVO.setTitle(vo.getName());
    drawerVO.setIcon(vo.getIcon());
    drawerVO.setTo(StringUtils.defaultString(vo.getUrl()));
    drawerVO.setType(vo.getType());
    return drawerVO;
  }

  private MenuVO convertTableMenuEntityToMenuVO(
    final TableMenuEntity vo,
    final Integer level
  ) {
    final MenuVO menuVO = MapperUtils.toObject(vo, MenuVO.class);
    menuVO.setLevel(level);
    return menuVO;
  }

  public List<DrawerVO> getDrawerList(final Integer authority) {
    if (authority.equals(999)) {
      return this.tableMenuRepository.getItemsByLevel2()
        .stream()
        .map(this::convertTableMenuEntityToDrawerVO)
        .peek(
          item ->
            item.setChildren(
              this.tableMenuRepository.getItemsByLevel3AndParentId(item.getId())
                .stream()
                .map(this::convertTableMenuEntityToDrawerVO)
                .collect(Collectors.toList())
            )
        )
        .collect(Collectors.toList());
    } else {
      return this.tableMenuRepository.getItemsByLevel2AndAuthority(authority)
        .stream()
        .map(this::convertTableMenuEntityToDrawerVO)
        .peek(
          item ->
            item.setChildren(
              this.tableMenuRepository.getItemsByLevel3AndAuthorityAndParentId(
                authority,
                item.getId()
              )
                .stream()
                .map(this::convertTableMenuEntityToDrawerVO)
                .collect(Collectors.toList())
            )
        )
        .collect(Collectors.toList());
    }
  }

  public List<MenuVO> getMenuList() {
    final List<MenuVO> result =
      this.tableMenuRepository.getItemsByLevel1()
        .stream()
        .map((TableMenuEntity vo) -> this.convertTableMenuEntityToMenuVO(vo, 1))
        .collect(Collectors.toList());
    this.tableMenuRepository.getItemsByLevel2()
      .stream()
      .map((TableMenuEntity vo) -> this.convertTableMenuEntityToMenuVO(vo, 2))
      .forEach(
        item -> {
          result.add(item);
          result.addAll(
            this.tableMenuRepository.getItemsByLevel3AndParentId(item.getId())
              .stream()
              .map(
                (TableMenuEntity vo) ->
                  this.convertTableMenuEntityToMenuVO(vo, 3)
              )
              .collect(Collectors.toList())
          );
        }
      );
    return result;
  }
}
