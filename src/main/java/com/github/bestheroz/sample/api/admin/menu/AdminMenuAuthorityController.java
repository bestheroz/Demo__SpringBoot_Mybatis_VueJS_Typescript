package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.entity.menuauthority.TableMenuAuthorityRepository;
import com.github.bestheroz.sample.api.entity.menuauthority.TableMenuAuthorityVO;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin/menuAuthority/")
@Slf4j
public class AdminMenuAuthorityController {
    @Resource private TableMenuAuthorityRepository tableMenuAuthorityRepository;
    @Resource private AdminMenuAuthorityService adminMenuAuthorityService;

    @GetMapping("{authority}")
    ResponseEntity<ApiResult> getList(@PathVariable("authority") final Integer authority) {
        return Result.ok(this.adminMenuAuthorityService.getList(authority));
    }

    @PutMapping(value = "{authority}")
    @CacheEvict(value = "drawerCache", key = "#authority")
    public ResponseEntity<ApiResult> save(@PathVariable("authority") final Integer authority, @RequestBody final Map<String, String> payload) {
        final TableMenuAuthorityVO tableMenuAuthorityVO = new TableMenuAuthorityVO();
        tableMenuAuthorityVO.setAuthority(authority);
        tableMenuAuthorityVO.setMenuIdList(Arrays.stream(StringUtils.split(payload.get("menuIdList"), ",")).map(item -> "^|" + item + ",").collect(Collectors.joining(StringUtils.EMPTY)));
        this.tableMenuAuthorityRepository.save(tableMenuAuthorityVO);
        return Result.ok();
    }
}
