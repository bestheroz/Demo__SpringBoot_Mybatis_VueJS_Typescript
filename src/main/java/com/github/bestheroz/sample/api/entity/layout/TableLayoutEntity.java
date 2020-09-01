package com.github.bestheroz.sample.api.entity.layout;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableLayoutEntity extends AbstractCreatedUpdateEntity implements Serializable {
    private static final long serialVersionUID = 7975686196090342524L;
    private String memberId;
    private Integer menuId;
    private String layoutList;
}
