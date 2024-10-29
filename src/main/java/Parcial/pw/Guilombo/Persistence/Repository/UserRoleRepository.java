package Parcial.pw.Guilombo.Persistence.Repository;


import Parcial.pw.Guilombo.Persistence.Entity.UserRoleEntity;
import Parcial.pw.Guilombo.Persistence.Entity.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRolePK> {

}
