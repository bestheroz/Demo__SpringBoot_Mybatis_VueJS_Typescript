package com.github.bestheroz.sample.api.entity.samplemenumst;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "SAMPLE_MENU_MST")
public class TableSampleMenuMstVO implements Serializable {
    private static final long serialVersionUID = 2658557582464222508L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;
    private String menuName;
    private String menuType;
    private Integer parMenuId;
    private Boolean useTf;
    private Integer power;
    private Integer displayOrder;
    private String url;
    private String remark1;
}
