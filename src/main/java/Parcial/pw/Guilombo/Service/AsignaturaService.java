package Parcial.pw.Guilombo.Service;

import Parcial.pw.Guilombo.Persistence.Entity.AsignaturaEntity;
import Parcial.pw.Guilombo.Persistence.Repository.AsignaturaRepository;
import Parcial.pw.Guilombo.Persistence.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private UserRepository userRepository;

    public List<AsignaturaEntity> obtenerTodas() {
        return asignaturaRepository.findAll();
    }

    public Optional<AsignaturaEntity> obtenerPorId(Integer id) {
        return asignaturaRepository.findById(id);
    }

    public List<AsignaturaEntity> obtenerPorDocente(Integer docenteId) {
        return asignaturaRepository.findByDocenteUserId(docenteId);
    }

    public List<AsignaturaEntity> buscarPorNombre(String nombre) {
        return asignaturaRepository.buscarPorNombre(nombre);
    }

    public AsignaturaEntity guardar(AsignaturaEntity asignatura) {
        if (asignatura.getNombre().length() > 30) {
            throw new IllegalArgumentException("El nombre no puede superar los 30 caracteres");
        }
        if (asignatura.getDescripcion() != null && asignatura.getDescripcion().length() > 100) {
            throw new IllegalArgumentException("La descripción no puede superar los 100 caracteres");
        }
        return asignaturaRepository.save(asignatura);
    }

    public void eliminar(Integer id) {
        asignaturaRepository.deleteById(id);
    }

    public AsignaturaEntity actualizarHorario(Integer id, AsignaturaEntity asignaturaActualizada) {
        return asignaturaRepository.findById(id)
                .map(asignatura -> {
                    asignatura.setHoraInicio(asignaturaActualizada.getHoraInicio());
                    asignatura.setHoraFin(asignaturaActualizada.getHoraFin());
                    return asignaturaRepository.save(asignatura);
                })
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));
    }
}
