package kz.meirambekuly.skidkilife.repositories;

import kz.meirambekuly.skidkilife.entity.Establishment;
import kz.meirambekuly.skidkilife.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findAllByEstablishment (Establishment establishment);
}