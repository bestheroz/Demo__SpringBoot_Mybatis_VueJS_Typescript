package com.github.bestheroz.demo.api.code;

import com.github.bestheroz.demo.domain.Code;
import com.github.bestheroz.demo.repository.CodeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeService {
  private final CodeRepository codeRepository;

  public Iterable<Code> saveAll(final List<CodeDTO> payload) {
    return this.codeRepository.saveAll(
        payload.stream().map(CodeDTO::toCode).collect(Collectors.toList()));
  }
}
