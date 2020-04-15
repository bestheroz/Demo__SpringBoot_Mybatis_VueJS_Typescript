package com.github.bestheroz.sample.api.menu;

import com.github.bestheroz.standard.common.response.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("menu")
public class MenuController {
    @Resource private MenuService menuService;

    @GetMapping
    public ResponseVO getList() {
        return ResponseVO.getSuccessResponseVO(this.menuService.getMenuList());
    }
}
