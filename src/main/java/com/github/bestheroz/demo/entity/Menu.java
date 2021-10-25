package com.github.bestheroz.demo.entity;

import com.github.bestheroz.demo.type.MenuType;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Menu extends AbstractCreatedUpdate implements Serializable {
  private static final long serialVersionUID = 2658557582464222508L;

  private Long id;

  private Long parentId;

  private String name;

  private MenuType type;

  private Integer displayOrder;
  private String url;
  private String icon;
}
