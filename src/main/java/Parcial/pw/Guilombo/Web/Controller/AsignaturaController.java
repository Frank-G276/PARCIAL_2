package Parcial.pw.Guilombo.Web.Controller;


import Parcial.pw.Guilombo.Persistence.Entity.AsignaturaEntity;
import Parcial.pw.Guilombo.Persistence.Entity.UserEntity;
import Parcial.pw.Guilombo.Persistence.Repository.UserRepository;
import Parcial.pw.Guilombo.Service.AsignaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/asignaturas")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listarAsignaturas(Model model) {
        model.addAttribute("asignaturas", asignaturaService.obtenerTodas());
        return "asignaturas/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("asignatura", new AsignaturaEntity());
        model.addAttribute("docentes", userRepository.findAllDocentes());
        return "asignaturas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarAsignatura(@ModelAttribute AsignaturaEntity asignatura,
                                    RedirectAttributes redirectAttrs) {
        try {
            asignaturaService.guardar(asignatura);
            redirectAttrs.addFlashAttribute("mensaje", "Asignatura guardada exitosamente");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/asignaturas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        Optional<AsignaturaEntity> asignatura = asignaturaService.obtenerPorId(id);
        if (asignatura.isPresent()) {
            model.addAttribute("asignatura", asignatura.get());
            model.addAttribute("docentes", userRepository.findAllDocentes());
            return "asignaturas/formulario";
        }
        return "redirect:/asignaturas";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAsignatura(@PathVariable Integer id,
                                     RedirectAttributes redirectAttrs) {
        try {
            asignaturaService.eliminar(id);
            redirectAttrs.addFlashAttribute("mensaje", "Asignatura eliminada exitosamente");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "No se pudo eliminar la asignatura");
        }
        return "redirect:/asignaturas";
    }

    @GetMapping("/mis-asignaturas")
    public String listarMisAsignaturas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> docente = userRepository.findByUsername(auth.getName());

        if (docente.isPresent()) {
            model.addAttribute("asignaturas",
                    asignaturaService.obtenerPorDocente(docente.get().getUserId()));
            return "asignaturas/lista-docente";
        }
        return "redirect:/asignaturas";
    }


    @PostMapping("/actualizar-horario/{id}")
    public String actualizarHorario(@PathVariable Integer id,
                                    @ModelAttribute AsignaturaEntity asignaturaActualizada,
                                    RedirectAttributes redirectAttrs) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional<UserEntity> docente = userRepository.findByUsername(auth.getName());
            Optional<AsignaturaEntity> asignatura = asignaturaService.obtenerPorId(id);

            if (docente.isPresent() && asignatura.isPresent() &&
                    asignatura.get().getDocente().getUserId().equals(docente.get().getUserId())) {

                asignaturaService.actualizarHorario(id, asignaturaActualizada);
                redirectAttrs.addFlashAttribute("mensaje", "Horario actualizado exitosamente");
            } else {
                redirectAttrs.addFlashAttribute("error", "No tiene permisos para actualizar este horario");
            }
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al actualizar el horario: " + e.getMessage());
        }
        return "redirect:/asignaturas/mis-asignaturas";
    }
}
