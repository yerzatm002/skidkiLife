package kz.meirambekuly.skidkilife.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "establishments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Establishment extends AbstractBaseEntity {

    @Column(name = "phone_number", columnDefinition = "TEXT")
    private String phoneNumber;

    @Column(name = "password", columnDefinition = "TEXT")
    private String password;

    @Column(name = "establishment_name")
    private String name;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Calendar createdDate;

    @Column(name = "isActivated")
    private Boolean isActivated = false;

    @Column(name = "isEnabled")
    private Boolean isEnabled = false;

    @Column(name = "address")
    private String address;

    @Column(name = "longitude", columnDefinition = "DECIMAL")
    private Double longitude;

    @Column(name = "latitude", columnDefinition = "DECIMAL")
    private Double latitude;

    @Column(name = "establishment_type")
    private String type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "from_work_schedule")
    private Time fromWorkSchedule;

    @Column(name = "to_work_schedule")
    private Time toWorkSchedule;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "establishment_work_schedules", joinColumns = @JoinColumn(name = "establishment_id"),
            inverseJoinColumns = @JoinColumn(name = "day_id", insertable = false, updatable = false))
    private Set<WorkSchedule> workSchedule;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @OneToMany(mappedBy = "establishment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Sale> sales;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
