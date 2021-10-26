package com.github.bestheroz.demo.api.role;

import com.github.bestheroz.demo.entity.Role;
import com.github.bestheroz.demo.repository.RoleRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/roles/")
@RequiredArgsConstructor
public class RoleController {
  private final RoleService roleService;
  private final RoleRepository roleRepository;

  @GetMapping(value = "selections/")
  public ResponseEntity<ApiResult<List<RoleSimpleDTO>>> getSelections(
      @RequestParam(required = false) final Boolean available) {
    return Result.ok(this.roleService.getSelections(available));
  }

  @GetMapping
  ResponseEntity<ApiResult<List<RoleChildrenDTO>>> getItems() {
    return Result.ok(this.roleService.getItems());
  }

  @PostMapping()
  public ResponseEntity<ApiResult<RoleChildrenDTO>> post(
      @RequestParam(required = false) final Long parentId,
      @RequestBody @Valid final RoleSimpleDTO payload) {
    final Role role = payload.toRole();
    role.setParentId(parentId);
    this.roleRepository.insert(role);
    return Result.created(this.roleService.getItem(role.getId()));
  }

  @PutMapping(value = "{id}")
  public ResponseEntity<ApiResult<RoleSimpleDTO>> put(
      @PathVariable(value = "id") final Long id, @RequestBody @Valid final RoleSimpleDTO payload) {
    return Result.ok(
        this.roleRepository
            .getItemById(id)
            .map(
                (item) -> {
                  item.change(payload);
                  this.roleRepository.updateById(item, id);
                  return new RoleSimpleDTO(
                      this.roleRepository
                          .getItemById(id)
                          .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
                })
            .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NO_DATA_SUCCESS)));
  }

  @PostMapping(value = "save-all/")
  public ResponseEntity<ApiResult<List<RoleChildrenDTO>>> saveAll(
      @RequestBody @Valid final List<RoleChildrenDTO> payload) {
    return Result.created(this.roleService.saveAll(payload));
  }
}
