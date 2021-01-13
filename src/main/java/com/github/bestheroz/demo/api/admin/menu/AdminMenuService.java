package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuRepository;
import com.github.bestheroz.demo.api.entity.menu.TableMenuEntity;
import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminMenuService {
  @Resource private TableMenuRepository tableMenuRepository;
  @Resource private TableMemberMenuRepository tableMemberMenuRepository;

  @Transactional
  public void put(final TableMenuEntity tableMenuEntity, final Integer id) {
    this.tableMenuRepository.updateByKey(tableMenuEntity, Map.of("id", id));
    final Map<String, Object> params = new HashMap<>();
    params.put("name", tableMenuEntity.getName());
    params.put("url", tableMenuEntity.getUrl());
    params.put("icon", tableMenuEntity.getIcon());
    this.tableMemberMenuRepository.updateMapByKey(params, Map.of("id", id));
  }

  @Transactional
  public void delete(final Integer id) {
    this.tableMenuRepository.deleteByKey(Map.of("id", id));
    this.tableMemberMenuRepository.deleteByKey(Map.of("id", id));
  }
}
