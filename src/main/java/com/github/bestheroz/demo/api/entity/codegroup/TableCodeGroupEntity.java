package com.github.bestheroz.demo.api.entity.codegroup;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class TableCodeGroupEntity extends AbstractCreatedUpdateEntity implements Serializable {
    private static final long serialVersionUID = 5295387617727505308L;
    private String codeGroup;
    private String name;
}
