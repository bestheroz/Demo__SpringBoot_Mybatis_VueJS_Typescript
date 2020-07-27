package com.github.bestheroz.sample.api.entity.member;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "MEMBER")
public class TableMemberEntity extends AbstractCreatedUpdateEntity implements Serializable {
    private static final long serialVersionUID = 7280716056600887400L;
    @Id
    private String id;
    private String password;
    private String name;
    private Integer authority;
    private Integer loginFailCnt;
    private Instant expired;
    private boolean available;
    private Integer timeout;
    private String token;

    @Override
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (this.loginFailCnt == null) {
            this.loginFailCnt = 0;
        }
    }
}
