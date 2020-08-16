package com.github.bestheroz.sample.api.entity.codegroup;

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
public class TableCodeGroupEntity extends AbstractCreatedUpdateEntity implements Serializable {
    private static final long serialVersionUID = 5295387617727505308L;
    private String codeGroup;
    private String name;
}
