package com.github.bestheroz.standard.common.mybatis;

import com.github.bestheroz.standard.common.util.CaseUtils;
import com.github.bestheroz.standard.common.util.NullUtils;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class DataTableFilterDTO {
  private Map<String, Object> filter = new HashMap<>();
  private long page;
  private List<String> sortBy = new ArrayList<>();
  private int itemsPerPage;

  private String search;

  private Set<String> searchColumns = new HashSet<>();

  public long getStartIndex() {
    return (this.page - 1) * this.itemsPerPage;
  }

  public long getEndIndex() {
    return this.page * this.itemsPerPage;
  }

  public String getOrderByLimitString() {
    return MessageFormat.format("{0} {1}", this.getOrderByString(), this.getLimitString());
  }

  public String getLimitString() {
    if (this.itemsPerPage == Short.MAX_VALUE) {
      return StringUtils.EMPTY;
    } else {
      return MessageFormat.format(
          "LIMIT {0}, {1}",
          String.valueOf(this.getStartIndex()), String.valueOf(this.getEndIndex()));
    }
  }

  public String getOrderByString() {
    if (NullUtils.isEmpty(this.sortBy)) {
      return StringUtils.EMPTY;
    } else {
      final List<String> append = new ArrayList<>();
      for (final String s : this.sortBy) {
        append.add(
            MessageFormat.format(
                "{0} {1}",
                CaseUtils.getSnakeCaseToCamelCase(s.replaceFirst("-", "")),
                s.startsWith("-") ? "DESC" : "ASC"));
      }
      return "ORDER BY " + StringUtils.join(append, ",");
    }
  }
}
