package com.github.bestheroz.demo.api.code;

import com.github.bestheroz.demo.repository.CodeRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeService {
  private final CodeRepository codeRepository;

  public List<CodeDTO> saveAll(final List<CodeDTO> payload) {
    payload.stream()
        .map(CodeDTO::toCode)
        .forEach(c -> this.codeRepository.updateById(c, c.getId()));
    if (!payload.isEmpty()) {
      return this.codeRepository
          .getItemsByMapWithOrder(Map.of("type", payload.get(0).getType()), List.of("displayOrder"))
          .stream()
          .map(CodeDTO::new)
          .toList();
    } else {
      return List.of();
    }
  }
}
