package kz.meirambekuly.skidkilife.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City extends AbstractBaseEntity{
    @Column(name = "name")
    private String name;
}
