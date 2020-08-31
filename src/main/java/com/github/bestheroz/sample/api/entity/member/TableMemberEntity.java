package com.github.bestheroz.sample.api.entity.member;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMemberEntity extends AbstractCreatedUpdateEntity implements Serializable {
    private static final long serialVersionUID = 7280716056600887400L;
    private String id;
    private String password;
    private String name;
    private Integer authority;
    private Integer loginFailCnt;
    private Instant expired;
    private boolean available;
    private String theme;
    private Integer timeout;
    private String token;
}
