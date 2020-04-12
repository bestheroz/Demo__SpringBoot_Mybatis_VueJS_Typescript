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

    @GetMapping(value = "{id}")
    public ResponseVO getOne(@PathVariable(value = "id", required = false) final Integer id) {
        return ResponseVO.getSuccessResponseVO(this.tableSampleMenuMstRepository.findById(id));
    }

    @PostMapping
    public ResponseVO insert(@RequestBody final TableSampleMenuMstVO tableSampleMenuMstVO) {
        this.tableSampleMenuMstRepository.save(tableSampleMenuMstVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @PatchMapping(value = "{id}")
    public ResponseVO update(@PathVariable(value = "id") final Integer id, @RequestBody final TableSampleMenuMstVO tableSampleMenuMstVO) {
        tableSampleMenuMstVO.setId(id);
        this.tableSampleMenuMstRepository.save(tableSampleMenuMstVO);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @DeleteMapping(value = "{id}")
    public ResponseVO delete(@PathVariable(value = "id") final Integer id) {
        this.tableSampleMenuMstRepository.deleteById(id);
        return ResponseVO.SUCCESS_NORMAL;
    }

    @GetMapping(value = "getMenuTypeG")
    public ResponseVO getMenuTypeG() {
        return ResponseVO.getSuccessResponseVO(this.adminMenuDAO.getMenuTypeG());
    }
}
