package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.sample.api.entity.menu.TableMenuVO;
import com.github.bestheroz.sample.api.menu.MenuService;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/admin/menus")
public class AdminMenuController {
    @Resource private MenuService menuService;
    @Resource private TableMenuRepository tableMenuRepository;

    @GetMapping
    ResponseEntity<ApiResult> getList() {
        return Result.ok(this.menuService.getMenuList());
    }

    @GetMapping(value = "{id}")
    ResponseEntity<ApiResult> getOne(@PathVariable(value = "id", required = false) final Integer id) {
        return Result.ok(this.tableMenuRepository.findById(id));
    }

    @PostMapping
    @CacheEvict(value = "drawerCache", allEntries = true)
    public ResponseEntity<ApiResult> insert(@RequestBody final TableMenuVO tableMenuVO) {
        this.tableMenuRepository.save(tableMenuVO);
        return Result.ok();
    }

    @PatchMapping(value = "{id}")
    @CacheEvict(value = "drawerCache", allEntries = true)
    public ResponseEntity<ApiResult> update(@PathVariable(value = "id") final Integer id, @RequestBody final TableMenuVO tableMenuVO) {
        final Optional<TableMenuVO> byId = this.tableMenuRepository.findById(id);
        if (byId.isPresent()) {
            final TableMenuVO tableMenuVO1 = byId.get();
        }
        tableMenuVO.setId(id);
        this.tableMenuRepository.save(tableMenuVO);
        return Result.ok();
    }

    @DeleteMapping(value = "{id}")
    public @CacheEvict(value = "drawerCache", allEntries = true)
    ResponseEntity<ApiResult> delete(@PathVariable(value = "id") final Integer id) {
        this.tableMenuRepository.deleteById(id);
        return Result.ok();
    }
}
