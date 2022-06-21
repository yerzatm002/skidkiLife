package kz.meirambekuly.skidkilife.repositories;

import kz.meirambekuly.skidkilife.entity.SmsVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsVerificationRepository extends JpaRepository<SmsVerification, Long> {
}