package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.demo.api.entity.menu.authority.TableMenuAuthorityRepository;
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
    this.tableMenuRepository.deleteByKey(Map.of("parentId", id));
    this.tableMenuRepository.deleteByKey(Map.of("id", id));
    this.tableMenuAuthorityRepository.getItems()
      .parallelStream()
      .filter(
        item -> StringUtils.contains(item.getMenuIdList(), "^|" + id + ",")
      )
      .forEach(
        item -> {
          this.tableMenuAuthorityRepository.updateMapByKey(
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
