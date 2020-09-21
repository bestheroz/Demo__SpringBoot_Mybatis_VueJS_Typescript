package com.github.bestheroz.demo.api.menu;

import java.util.List;
import lombok.Data;

@Data
public class DrawerVO {
  private Integer id;
  private String title;
  private String icon;
  private String to;
  private String type;
  private String group;
  private boolean checked;
  private List<DrawerVO> children;
}
