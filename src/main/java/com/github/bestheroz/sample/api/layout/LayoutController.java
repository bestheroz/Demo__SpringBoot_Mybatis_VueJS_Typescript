package com.github.bestheroz.sample.api.layout;

import com.github.bestheroz.sample.api.entity.layout.TableLayoutEntity;
import com.github.bestheroz.sample.api.entity.layout.TableLayoutRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import com.github.bestheroz.standard.common.util.MapperUtils;
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

    @GetMapping
    ResponseEntity<ApiResult> getItems() {
        return Result.ok(this.tableLayoutRepository.getItemsByKey(TableLayoutEntity.class, Map.of("memberId", AuthenticationUtils.getUserPk())));
    }

    @PutMapping(value = "{menuId}")
    public ResponseEntity<ApiResult> update(@PathVariable(value = "menuId") final Integer menuId,
                                            @RequestBody final Map<String, String> payload) {
        this.tableLayoutRepository.getItem(TableLayoutEntity.class, Map.of("memberId", AuthenticationUtils.getUserPk(), "menuId", menuId))
                .ifPresentOrElse(item -> this.tableLayoutRepository
                                .updateMap(TableLayoutEntity.class, Map.of("layoutList", MapperUtils.toString(payload.get("layoutList"))),
                                        Map.of("memberId", AuthenticationUtils.getUserPk(), "menuId", menuId))
                        , () -> this.tableLayoutRepository.insert(new TableLayoutEntity(AuthenticationUtils.getUserPk(), menuId, MapperUtils.toString(payload.get("layoutList")))));
        return Result.ok();
    }
}
