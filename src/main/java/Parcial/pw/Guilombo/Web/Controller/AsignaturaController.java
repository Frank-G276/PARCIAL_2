package Parcial.pw.Guilombo.Web.Controller;

import Parcial.pw.Guilombo.Persistence.Entity.AsignaturaEntity;
import Parcial.pw.Guilombo.Persistence.Entity.UserEntity;
import Parcial.pw.Guilombo.Persistence.Repository.UserRepository;
import Parcial.pw.Guilombo.Service.AsignaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Tag(name = "Asignaturas", description = "API para la gestión de asignaturas")
@SecurityRequirement(name = "bearerAuth")
@Controller
@RequestMapping("/asignaturas")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Listar todas las asignaturas",
            description = "Muestra una lista de todas las asignaturas registradas en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver las asignaturas")
    })
    @GetMapping
    public String listarAsignaturas(Model model) {
        model.addAttribute("asignaturas", asignaturaService.obtenerTodas());
        return "asignaturas/lista";
    }

    @Operation(summary = "Mostrar formulario de nueva asignatura",
            description = "Muestra el formulario para crear una nueva asignatura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario mostrado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para crear asignaturas")
    })
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("asignatura", new AsignaturaEntity());
        model.addAttribute("docentes", userRepository.findAllDocentes());
        return "asignaturas/formulario";
    }

    @Operation(summary = "Guardar asignatura",
            description = "Guarda una nueva asignatura o actualiza una existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asignatura guardada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de asignatura inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para guardar asignaturas")
    })
    @PostMapping("/guardar")
    public String guardarAsignatura(
            @Parameter(description = "Datos de la asignatura a guardar")
            @ModelAttribute AsignaturaEntity asignatura,
            RedirectAttributes redirectAttrs) {
        try {
            asignaturaService.guardar(asignatura);
            redirectAttrs.addFlashAttribute("mensaje", "Asignatura guardada exitosamente");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/asignaturas";
    }

    @Operation(summary = "Mostrar formulario de edición",
            description = "Muestra el formulario para editar una asignatura existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario de edición mostrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado para editar asignaturas")
    })
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(
            @Parameter(description = "ID de la asignatura a editar")
            @PathVariable Integer id,
            Model model) {
        Optional<AsignaturaEntity> asignatura = asignaturaService.obtenerPorId(id);
        if (asignatura.isPresent()) {
            model.addAttribute("asignatura", asignatura.get());
            model.addAttribute("docentes", userRepository.findAllDocentes());
            return "asignaturas/formulario";
        }
        return "redirect:/asignaturas";
    }

    @Operation(summary = "Eliminar asignatura",
            description = "Elimina una asignatura existente del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asignatura eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado para eliminar asignaturas")
    })
    @PostMapping("/eliminar/{id}")
    public String eliminarAsignatura(
            @Parameter(description = "ID de la asignatura a eliminar")
            @PathVariable Integer id,
            RedirectAttributes redirectAttrs) {
        try {
            asignaturaService.eliminar(id);
            redirectAttrs.addFlashAttribute("mensaje", "Asignatura eliminada exitosamente");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "No se pudo eliminar la asignatura");
        }
        return "redirect:/asignaturas";
    }

    @Operation(summary = "Listar asignaturas del docente",
            description = "Muestra la lista de asignaturas asignadas al docente autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver las asignaturas"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
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

    @Operation(summary = "Actualizar horario de asignatura",
            description = "Actualiza el horario de una asignatura específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Horario actualizado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para actualizar el horario"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
    })
    @PostMapping("/actualizar-horario/{id}")
    public String actualizarHorario(
            @Parameter(description = "ID de la asignatura")
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados de la asignatura")
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