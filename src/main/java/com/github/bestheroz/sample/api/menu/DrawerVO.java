package com.github.bestheroz.sample.api.menu;

import lombok.Data;

import java.util.List;

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
