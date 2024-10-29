package Parcial.pw.Guilombo.Persistence.Repository;

import Parcial.pw.Guilombo.Persistence.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    @Query("SELECT DISTINCT u FROM UserEntity u JOIN u.roles r WHERE r.rolename = 'DOCENTE'")
    List<UserEntity> findAllDocentes();
}