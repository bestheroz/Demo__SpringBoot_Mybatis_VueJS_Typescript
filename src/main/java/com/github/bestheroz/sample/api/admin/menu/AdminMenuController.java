package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.sample.api.entity.menu.TableMenuVO;
import com.github.bestheroz.sample.api.menu.MenuService;
import com.github.bestheroz.standard.common.response.ResponseVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

@RestController
@RequestMapping(value = "/admin/menus")
public class AdminMenuController {
    @Resource private MenuService menuService;
    @Resource private TableMenuRepository tableMenuRepository;

    @GetMapping
    public ResponseVO getList() {
        return ResponseVO.getSuccessResponseVO(this.menuService.getMenuList());
    }

    @GetMapping(value = "{id}")
    public ResponseVO getOne(@PathVariable(value = "id", required = false) final Integer id) {
        return ResponseVO.getSuccessResponseVO(this.tableMenuRepository.findById(id));
    }

    @PostMapping
    @CacheEvict(value = "drawerVO", allEntries = true)
    public ResponseVO insert(@RequestBody final TableMenuVO tableMenuVO) {
        this.tableMenuRepository.save(tableMenuVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @PatchMapping(value = "{id}")
    @CacheEvict(value = "drawerVO", allEntries = true)
    public ResponseVO update(@PathVariable(value = "id") final Integer id, @RequestBody final TableMenuVO tableMenuVO) {
        final Optional<TableMenuVO> byId = this.tableMenuRepository.findById(id);
        if (byId.isPresent()) {
            final TableMenuVO tableMenuVO1 = byId.get();
        }
        tableMenuVO.setId(id);
        this.tableMenuRepository.save(tableMenuVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @DeleteMapping(value = "{id}")
    @CacheEvict(value = "drawerVO", allEntries = true)
    public ResponseVO delete(@PathVariable(value = "id") final Integer id) {
        this.tableMenuRepository.deleteById(id);
        return ResponseVO.SUCCESS_NORMAL;
    }
}
