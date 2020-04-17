package com.github.bestheroz.sample.api.entity.member;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateVO;
import lombok.*;
import org.joda.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "MEMBER")
public class TableMemberVO extends AbstractCreatedUpdateVO implements Serializable {
    private static final long serialVersionUID = 7280716056600887400L;
    @Id
    private String id;
    private String password;
    private String name;
    private Integer authority;
    private Integer loginFailCnt;
    private LocalDateTime expired;
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
