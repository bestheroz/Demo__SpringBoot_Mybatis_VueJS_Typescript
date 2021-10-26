package com.github.bestheroz.demo.api.code;

import com.github.bestheroz.demo.entity.Code;
import com.github.bestheroz.demo.repository.CodeRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "api/codes/")
@RequiredArgsConstructor
public class CodeController {
  private final CodeRepository codeRepository;
  private final CodeService codeService;

  @GetMapping("types/")
  ResponseEntity<ApiResult<List<String>>> getTypes(
      @RequestParam(required = false) final Boolean available) {
    final Map<String, Object> whereConditions = new HashMap<>();
    whereConditions.put("available", available);
    return Result.ok(
        this.codeRepository
            .getTargetItemsByMapWithOrder(Set.of("type"), whereConditions, List.of("type"))
            .stream()
            .map(Code::getType)
            .collect(Collectors.toList()));
  }

  @GetMapping
  ResponseEntity<ApiResult<List<CodeDTO>>> getItems(
      @RequestParam(value = "type") final String type) {
    return Result.ok(
        this.codeRepository
            .getItemsByMapWithOrder(Map.of("type", type), List.of("displayOrder"))
            .stream()
            .map(CodeDTO::new)
            .collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<ApiResult<CodeDTO>> post(@RequestBody @Valid final CodeDTO payload) {
    final Code code = payload.toCode();
    this.codeRepository.insert(code);
    return Result.created(
        new CodeDTO(
            this.codeRepository
                .getItemById(code.getId())
                .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS)));
  }

  @PutMapping(value = "{id}")
  public ResponseEntity<ApiResult<CodeDTO>> put(
      @PathVariable(value = "id") final Long id, @RequestBody @Valid final CodeDTO payload) {
    this.codeRepository.updateById(payload.toCode(), id);
    return Result.ok(
        this.codeRepository
            .getItemById(id)
            .map(
                (code) ->
                    new CodeDTO(
                        this.codeRepository
                            .getItemById(code.getId())
                            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS)))
            .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS)));
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<ApiResult<?>> delete(@PathVariable(value = "id") final Long id) {
    this.codeRepository.deleteById(id);
    return Result.ok();
  }

  @PostMapping(value = "save-all/")
  public ResponseEntity<ApiResult<List<CodeDTO>>> saveAll(
      @RequestBody @Valid final List<CodeDTO> payload) {
    return Result.created(this.codeService.saveAll(payload));
  }
}
