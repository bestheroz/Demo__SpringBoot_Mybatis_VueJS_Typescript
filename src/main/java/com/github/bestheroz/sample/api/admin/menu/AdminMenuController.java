package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.entity.samplemenumst.TableSampleMenuMstRepository;
import com.github.bestheroz.sample.api.entity.samplemenumst.TableSampleMenuMstVO;
import com.github.bestheroz.standard.common.response.ResponseVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/sample/admin/menu")
public class AdminMenuController {
    @Resource private AdminMenuDAO adminMenuDAO;
    @Resource private TableSampleMenuMstRepository tableSampleMenuMstRepository;

    @GetMapping
    public ResponseVO getList() {
        return ResponseVO.getSuccessResponseVO(this.tableSampleMenuMstRepository.findAll());
    }

    @GetMapping(value = "{menuId}")
    public ResponseVO getOne(@PathVariable(value = "menuId", required = false) final Integer menuId) {
        return ResponseVO.getSuccessResponseVO(this.tableSampleMenuMstRepository.findById(menuId));
    }

    @PostMapping
    public ResponseVO insert(@RequestBody final TableSampleMenuMstVO tableSampleMenuMstVO) {
        this.tableSampleMenuMstRepository.save(tableSampleMenuMstVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @PatchMapping(value = "{menuId}")
    public ResponseVO update(@PathVariable(value = "menuId") final Integer menuId, @RequestBody final TableSampleMenuMstVO tableSampleMenuMstVO) {
        tableSampleMenuMstVO.setMenuId(menuId);
        this.tableSampleMenuMstRepository.save(tableSampleMenuMstVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @DeleteMapping(value = "{menuId}")
    public ResponseVO delete(@PathVariable(value = "menuId") final Integer menuId) {
        this.tableSampleMenuMstRepository.deleteById(menuId);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @GetMapping(value = "getMenuTypeG")
    public ResponseVO getMenuTypeG() {
        return ResponseVO.getSuccessResponseVO(this.adminMenuDAO.getMenuTypeG());
    }
}
