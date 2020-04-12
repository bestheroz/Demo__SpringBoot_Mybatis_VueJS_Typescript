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
    private Integer id;
    private String name;
    private String type;
    private Integer parentId;
    private Boolean isUsing;
    private Integer power;
    private Integer displayOrder;
    private String url;
    private String icon;
    private String remark1;
}
