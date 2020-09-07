package com.github.bestheroz.sample.api.entity.menu;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class TableMenuEntity extends AbstractCreatedUpdateEntity implements Serializable {
    private static final long serialVersionUID = 2658557582464222508L;
    private Integer id;
    private String name;
    private String type;
    private Integer parentId;
    private Integer displayOrder;
    private String url;
    private String icon;
}
