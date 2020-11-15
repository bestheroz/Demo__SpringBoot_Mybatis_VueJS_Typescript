package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.menu.authority.TableMenuAuthorityEntity;
import com.github.bestheroz.demo.api.entity.menu.authority.TableMenuAuthorityRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/menuAuthority/")
@Slf4j
public class AdminMenuAuthorityController {
  @Resource
  private TableMenuAuthorityRepository tableMenuAuthorityRepository;
  @Resource
  private AdminMenuAuthorityService adminMenuAuthorityService;

  @GetMapping("{authority}")
  ResponseEntity<ApiResult> getItems(
    @PathVariable("authority") final Integer authority
  ) {
    return Result.ok(this.adminMenuAuthorityService.getItems(authority));
  }

  @PutMapping(value = "{authority}")
  public ResponseEntity<ApiResult> put(
    @PathVariable("authority") final Integer authority,
    @RequestBody final Map<String, String> payload
  ) {
    final String menuIdList = Arrays
      .stream(StringUtils.split(payload.get("menuIdList"), ","))
      .map(item -> "^|" + item + ",")
      .collect(Collectors.joining(StringUtils.EMPTY));

    this.tableMenuAuthorityRepository.getItemByKey(
      Map.of("authority", authority)
    )
      .ifPresentOrElse(
        item ->
          this.tableMenuAuthorityRepository.updateMapByKey(
            Map.of("menuIdList", menuIdList),
            Map.of("authority", authority)
          ),
        () ->
          this.tableMenuAuthorityRepository.insert(
            new TableMenuAuthorityEntity(authority, menuIdList)
          )
      );
    return Result.ok();
  }
}
