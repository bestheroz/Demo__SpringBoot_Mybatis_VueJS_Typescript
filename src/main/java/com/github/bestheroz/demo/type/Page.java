package com.github.bestheroz.demo.type;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Page<T> {
  private int totalElements;
  private List<T> contents;

  public Page(final int totalElements) {
    this.totalElements = totalElements;
    this.contents = List.of();
  }
}
