package kz.meirambekuly.skidkilife.specifications;

import kz.meirambekuly.skidkilife.entity.Employee;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class EmployeeSpecifications {
    public static Specification<Employee> findEmployeeByPhoneNumber(String phoneNumber) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("phone_number"), phoneNumber));
    }

    public static Specification<Employee> findEmployeeByPhoneNumberAndPassword(String phoneNumber, String password) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("phone_number"), phoneNumber),
                    criteriaBuilder.equal(root.get("password"), password)));
            return null;
        });
    }
}
