package com.github.bestheroz.demo.api.entity.member;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TableMemberEntity
  extends AbstractCreatedUpdateEntity
  implements Serializable {

  private static final long serialVersionUID = 7280716056600887400L;
  private String id;
  private String password;
  private String name;
  private Integer authority;
  private Integer loginFailCnt;
  private Instant expired;
  private boolean available;
  private String theme;
  private String token;
}
