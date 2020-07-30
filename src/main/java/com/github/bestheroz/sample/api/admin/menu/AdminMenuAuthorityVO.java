package com.github.bestheroz.sample.api.admin.menu;

import com.github.bestheroz.sample.api.entity.menu.TableMenuEntity;
import lombok.Data;


@Data
public class AdminMenuAuthorityVO extends TableMenuEntity {
    private static final long serialVersionUID = -76896741552627585L;
    private Integer level;
    private boolean checked;
}
