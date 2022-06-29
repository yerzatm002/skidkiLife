package kz.meirambekuly.skidkilife.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "sales")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sale extends AbstractBaseEntity{

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establishment_id", referencedColumnName = "id")
    private Establishment establishment;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Calendar createdDate;
}
