package Parcial.pw.Guilombo.Persistence.Repository;

import Parcial.pw.Guilombo.Persistence.Entity.DocenteAsignaturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocenteAsignaturaRepository extends JpaRepository<DocenteAsignaturaEntity, Integer> {
    List<DocenteAsignaturaEntity> findByDocenteUserId(Integer docenteId);
}
