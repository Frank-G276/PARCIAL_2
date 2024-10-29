package Parcial.pw.Guilombo.Web.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "Home", description = "API para el manejo de navegación principal y autenticación")
@Controller
public class HomeController {

    @Operation(summary = "Redirección inicial",
            description = "Redirige a la página principal de asignaturas cuando se accede a la raíz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirección exitosa a la página de asignaturas"),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder al sistema")
    })
    @GetMapping("/")
    public RedirectView redirectToUser() {
        return new RedirectView("/asignaturas");
    }

    @Operation(summary = "Página de login",
            description = "Muestra la página de inicio de sesión y maneja mensajes de error/logout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de login mostrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los parámetros de la solicitud")
    })
    @GetMapping("/login")
    public String loginPage(
            @Parameter(description = "Indica si hubo un error en el intento de login")
            @RequestParam(required = false) String error,

            @Parameter(description = "Indica si el usuario cerró sesión")
            @RequestParam(required = false) String logout,

            Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }
        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión exitosamente");
        }
        return "login";
    }

    @Operation(summary = "Página de acceso denegado",
            description = "Muestra la página de error 403 cuando se deniega el acceso a un recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de error 403 mostrada exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado al recurso solicitado")
    })
    @GetMapping("403")
    public String accessDenied() {
        return "403";
    }
}