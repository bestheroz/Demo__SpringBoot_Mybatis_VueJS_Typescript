package com.github.bestheroz.sample.api.entity.layout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableLayoutEntity implements Serializable {
    private static final long serialVersionUID = 7975686196090342524L;
    private String memberId;
    private Integer menuId;
    private String layoutList;
}
