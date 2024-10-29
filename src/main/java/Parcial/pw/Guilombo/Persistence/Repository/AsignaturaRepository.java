package Parcial.pw.Guilombo.Persistence.Repository;

import Parcial.pw.Guilombo.Persistence.Entity.AsignaturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AsignaturaRepository extends JpaRepository<AsignaturaEntity, Integer> {
    List<AsignaturaEntity> findByDocenteUserId(Integer docenteId);

    @Query("SELECT a FROM AsignaturaEntity a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<AsignaturaEntity> buscarPorNombre(@Param("nombre") String nombre);
}