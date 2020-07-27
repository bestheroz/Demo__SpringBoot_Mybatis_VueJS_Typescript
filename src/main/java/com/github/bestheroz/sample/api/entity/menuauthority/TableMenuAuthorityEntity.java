package com.github.bestheroz.sample.api.entity.menuauthority;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "MENU_AUTHORITY")
public class TableMenuAuthorityEntity extends AbstractCreatedUpdateEntity implements Serializable {
    private static final long serialVersionUID = 7975686196090342524L;
    @Id private Integer authority;
    private String menuIdList;
}
