package kz.meirambekuly.skidkilife.repositories;

import kz.meirambekuly.skidkilife.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}