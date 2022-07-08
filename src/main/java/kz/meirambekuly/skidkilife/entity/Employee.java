package kz.meirambekuly.skidkilife.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "skidki_employees")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends AbstractBaseEntity{
    @Column(name = "name")
    private String name;

    @Column(name = "employee_created_at")
    private LocalDate createdAt;

    @Column(name = "major")
    private String major;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
