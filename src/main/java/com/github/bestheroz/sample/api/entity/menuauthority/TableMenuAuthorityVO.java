package com.github.bestheroz.sample.api.entity.menuauthority;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateVO;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "MENU_AUTHORITY")
public class TableMenuAuthorityVO extends AbstractCreatedUpdateVO implements Serializable {
    private static final long serialVersionUID = 7975686196090342524L;
    @Id
    private Integer authority;
    private String menuIdList;
}
