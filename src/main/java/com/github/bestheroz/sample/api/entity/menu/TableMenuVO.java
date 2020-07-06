package com.github.bestheroz.sample.api.entity.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MENU")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableMenuVO extends AbstractCreatedUpdateVO implements Serializable {
    private static final long serialVersionUID = 2658557582464222508L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    private String name;
    private String type;
    private Integer parentId;
    private Integer displayOrder;
    private String url;
    private String icon;
    private String remark1;
}
