package Parcial.pw.Guilombo.Persistence.Repository;

import Parcial.pw.Guilombo.Persistence.Entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {

    RoleEntity findByRolename(String rolename);
}