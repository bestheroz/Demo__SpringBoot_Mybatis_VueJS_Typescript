package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.menu.TableMenuEntity;
import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.demo.api.entity.menuauthority.TableMenuAuthorityEntity;
import com.github.bestheroz.demo.api.entity.menuauthority.TableMenuAuthorityRepository;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminMenuService {
  @Resource
  private TableMenuRepository tableMenuRepository;

  @Resource
  private TableMenuAuthorityRepository tableMenuAuthorityRepository;

  @Transactional
  public void delete(final Integer id) {
    this.tableMenuRepository.delete(
        TableMenuEntity.class,
        Map.of("parentId", id)
      );
    this.tableMenuRepository.delete(TableMenuEntity.class, Map.of("id", id));
    this.tableMenuAuthorityRepository.getItems(TableMenuAuthorityEntity.class)
      .parallelStream()
      .filter(
        item -> StringUtils.contains(item.getMenuIdList(), "^|" + id + ",")
      )
      .forEach(
        item -> {
          this.tableMenuAuthorityRepository.updateMap(
              TableMenuAuthorityEntity.class,
              Map.of(
                "menuIdList",
                StringUtils.remove(item.getMenuIdList(), "^|" + id + ",")
              ),
              Map.of("authority", item.getAuthority())
            );
        }
      );
  }
}
