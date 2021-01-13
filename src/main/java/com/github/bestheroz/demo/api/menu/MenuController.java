package com.github.bestheroz.demo.api.menu;

import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import javax.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/menus")
public class MenuController {
  @Resource private MenuService menuService;

  @GetMapping(value = "drawer")
  ResponseEntity<ApiResult> getItems() {
    return Result.ok(
        this.menuService.getDrawerList(AuthenticationUtils.getLoginVO().getAuthority()));
  }
}
