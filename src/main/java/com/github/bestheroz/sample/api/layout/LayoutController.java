package com.github.bestheroz.sample.api.layout;

import com.github.bestheroz.sample.api.entity.layout.TableLayoutEntity;
import com.github.bestheroz.sample.api.entity.layout.TableLayoutRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "api/layouts")
public class LayoutController {
    @Resource private TableLayoutRepository tableLayoutRepository;

    @GetMapping(value = "{memberId}")
    ResponseEntity<ApiResult> getItems(@PathVariable(value = "memberId") final String memberId) {
        return Result.ok(this.tableLayoutRepository.getItemsByKey(TableLayoutEntity.class, Map.of("memberId", memberId)));
    }

    @PatchMapping(value = "{memberId}/{menuId}")
    public ResponseEntity<ApiResult> update(@PathVariable(value = "memberId") final String memberId, @PathVariable(value = "menuId") final String menuId,
                                            @RequestBody final Map<String, String> payload) {
        this.tableLayoutRepository.updateMap(TableLayoutEntity.class, Map.of("layoutList", payload.get("layoutList")), Map.of("memberId", memberId, "menuId", menuId));
        return Result.ok();
    }
}
