package com.github.bestheroz.sample.api.entity.menuauthority;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMenuAuthorityEntity extends AbstractCreatedUpdateEntity implements Serializable {
    private static final long serialVersionUID = 7975686196090342524L;
    private Integer authority;
    private String menuIdList;
}
