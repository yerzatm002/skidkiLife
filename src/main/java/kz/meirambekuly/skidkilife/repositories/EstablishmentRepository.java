package kz.meirambekuly.skidkilife.repositories;

import kz.meirambekuly.skidkilife.entity.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long>, JpaSpecificationExecutor<Establishment> {

    Establishment findByPhoneNumber(String phoneNumber);

}