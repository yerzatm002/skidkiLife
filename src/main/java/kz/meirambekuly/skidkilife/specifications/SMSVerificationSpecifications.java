package kz.meirambekuly.skidkilife.specifications;

import kz.meirambekuly.skidkilife.entity.SmsVerification;
import org.springframework.data.jpa.domain.Specification;

public class SMSVerificationSpecifications {
    public static Specification<SmsVerification> findVerificationByPhoneNumber(String phoneNumber) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber));
    }

    public static Specification<SmsVerification> findVerificationByPhoneNumberAndCode(String phoneNumber, String code) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber),
                    criteriaBuilder.equal(root.get("code"), code)));
            return null;
        });
    }
}
