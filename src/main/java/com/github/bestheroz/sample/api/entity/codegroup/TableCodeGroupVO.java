package com.github.bestheroz.sample.api.entity.codegroup;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateVO;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "CODE_GROUP")
public class TableCodeGroupVO extends AbstractCreatedUpdateVO implements Serializable {
    private static final long serialVersionUID = 5295387617727505308L;
    @Id
    private String codeGroup;
    private String name;
    private String remark1;
}
