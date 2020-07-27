package com.github.bestheroz.sample.api.entity.menu;

import com.github.bestheroz.sample.api.entity.AbstractCreatedUpdateEntity;
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
public class TableMenuEntity extends AbstractCreatedUpdateEntity implements Serializable {
    private static final long serialVersionUID = 2658557582464222508L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    private String name;
    private String type;
    private Integer parentId;
    private Integer displayOrder;
    private String url;
    private String icon;
}
