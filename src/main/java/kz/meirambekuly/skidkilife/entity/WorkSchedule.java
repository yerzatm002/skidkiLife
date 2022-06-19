package kz.meirambekuly.skidkilife.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "work_schedules")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkSchedule extends AbstractBaseEntity {

    @Column(name = "day")
    private String day;

}
