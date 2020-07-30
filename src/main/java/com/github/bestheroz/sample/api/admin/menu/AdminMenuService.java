package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.entity.menu.TableMenuEntity;
import com.github.bestheroz.sample.api.entity.menu.TableMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class AdminMenuService {
    @Resource private TableMenuRepository tableMenuRepository;

    @Transactional
    public void delete(final Integer id) {
        this.tableMenuRepository.delete(TableMenuEntity.class, Map.of("parentId", id));
        this.tableMenuRepository.delete(TableMenuEntity.class, Map.of("id", id));
    }
}
