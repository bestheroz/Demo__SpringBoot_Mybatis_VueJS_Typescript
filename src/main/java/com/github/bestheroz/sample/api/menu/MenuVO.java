package com.github.bestheroz.sample.api.menu;

import lombok.Data;

import java.util.List;

@Data
public class MenuVO {
    private String icon;
    private String title;
    private String to;
    private String type;
    private boolean checked;
    private List<MenuVO> children;
}
