package com.github.bestheroz.sample.api.menu;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
public class MenuVO implements Serializable {
    private static final long serialVersionUID = 5783239806154575625L;
    private Integer level;
    private Integer id;
    private String name;
    private String type;
    private Integer parentId;
    private boolean available;
    private Integer displayOrder;
    private String url;
    private String icon;
    private List<MenuVO> children;
    private String createdBy;
    private Instant created;
    private String updatedBy;
    private Instant updated;
}
