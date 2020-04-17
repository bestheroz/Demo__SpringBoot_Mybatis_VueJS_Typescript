package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.entity.menuauthority.TableMenuAuthorityRepository;
import com.github.bestheroz.sample.api.entity.menuauthority.TableMenuAuthorityVO;
import com.github.bestheroz.standard.common.response.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/menuAuthority/")
@Slf4j
public class AdminMenuAuthorityController {
    @Resource private TableMenuAuthorityRepository tableMenuAuthorityRepository;
    @Resource private AdminMenuAuthorityService adminMenuAuthorityService;

    @GetMapping("{authority}")
    public ResponseVO getList(@PathVariable("authority") final Integer authority) {
        return ResponseVO.getSuccessResponseVO(this.adminMenuAuthorityService.getList(authority));
    }

    @PutMapping(value = "{authority}")
    public ResponseVO save(@PathVariable("authority") final Integer authority, @RequestBody final Map<String, String> payload) {
        final TableMenuAuthorityVO TableMenuAuthorityVO = new TableMenuAuthorityVO();
        TableMenuAuthorityVO.setAuthority(authority);
        TableMenuAuthorityVO.setMenuIdList(Arrays.stream(StringUtils.split(payload.get("menuIdList"), ",")).map(item -> "^|" + item + ",").collect(Collectors.joining("")));
        this.tableMenuAuthorityRepository.save(TableMenuAuthorityVO);
        return ResponseVO.SUCCESS_NORMAL;
    }
}
