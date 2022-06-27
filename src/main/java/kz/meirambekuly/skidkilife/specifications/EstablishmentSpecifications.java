package kz.meirambekuly.skidkilife.specifications;

import kz.meirambekuly.skidkilife.entity.Establishment;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

@UtilityClass
public class EstablishmentSpecifications {
    public static Specification<Establishment> findByPhoneNumber(String phoneNumber){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber));
    }

    public static Specification<Establishment> findAllByName(String name){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
    }

    public static Specification<Establishment> findAllByAddress(String address) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + address.toLowerCase() + "%"));
    }

    public static Specification<Establishment> findByType(String type){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type));
    }

}
