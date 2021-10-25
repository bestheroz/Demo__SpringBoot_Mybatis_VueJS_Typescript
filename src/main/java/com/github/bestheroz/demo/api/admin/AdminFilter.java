package com.github.bestheroz.demo.api.admin;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminFilter {
  private List<Boolean> availableList = new ArrayList<>();
  private List<Long> roleIdList = new ArrayList<>();
}
