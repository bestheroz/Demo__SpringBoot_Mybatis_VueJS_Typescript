package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.sample.api.entity.menu.TableMenuVO;
import com.github.bestheroz.sample.api.menu.MenuDAO;
import com.github.bestheroz.standard.common.response.ResponseVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/admin/menus")
public class AdminMenuController {
    @Resource private AdminMenuDAO adminMenuDAO;
    @Resource private MenuDAO menuDAO;
    @Resource private TableMenuRepository tableMenuRepository;

    @GetMapping
    public ResponseVO getList() {
        return ResponseVO.getSuccessResponseVO(this.menuDAO.getList(999));
    }

    @GetMapping(value = "{id}")
    public ResponseVO getOne(@PathVariable(value = "id", required = false) final Integer id) {
        return ResponseVO.getSuccessResponseVO(this.tableMenuRepository.findById(id));
    }

    @PostMapping
    public ResponseVO insert(@RequestBody final TableMenuVO tableMenuVO) {
        this.tableMenuRepository.save(tableMenuVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @PatchMapping(value = "{id}")
    public ResponseVO update(@PathVariable(value = "id") final Integer id, @RequestBody final TableMenuVO tableMenuVO) {
        tableMenuVO.setId(id);
        this.tableMenuRepository.save(tableMenuVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @DeleteMapping(value = "{id}")
    public ResponseVO delete(@PathVariable(value = "id") final Integer id) {
        this.tableMenuRepository.deleteById(id);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @GetMapping(value = "getMenuTypeG")
    public ResponseVO getMenuTypeG() {
        return ResponseVO.getSuccessResponseVO(this.adminMenuDAO.getMenuTypeG());
    }
}
