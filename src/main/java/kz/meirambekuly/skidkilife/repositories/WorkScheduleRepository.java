package kz.meirambekuly.skidkilife.repositories;

import kz.meirambekuly.skidkilife.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
}