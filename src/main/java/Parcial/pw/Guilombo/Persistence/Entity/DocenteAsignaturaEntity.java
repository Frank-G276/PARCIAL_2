package Parcial.pw.Guilombo.Persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "docente_asignatura", schema = "parcial")
@Data
public class DocenteAsignaturaEntity {
    @Id
    @Column(name = "docente_asignatura_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer docenteAsignaturaId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity docente;

    @ManyToOne
    @JoinColumn(name = "asignatura_id")
    private AsignaturaEntity asignatura;
}