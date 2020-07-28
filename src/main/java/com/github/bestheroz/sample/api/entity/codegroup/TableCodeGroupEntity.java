package com.github.bestheroz.sample.api.entity.codegroup;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CODE_GROUP")
public class TableCodeGroupEntity extends AbstractCreatedUpdateEntity implements Serializable {
    private static final long serialVersionUID = 5295387617727505308L;
    @Id
    private String codeGroup;
    private String name;
}
