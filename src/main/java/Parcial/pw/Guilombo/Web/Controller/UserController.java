package Parcial.pw.Guilombo.Web.Controller;

import Parcial.pw.Guilombo.Persistence.Entity.RoleEntity;
import Parcial.pw.Guilombo.Persistence.Entity.UserEntity;
import Parcial.pw.Guilombo.Persistence.Entity.UserRoleEntity;
import Parcial.pw.Guilombo.Service.RoleService;
import Parcial.pw.Guilombo.Service.UserRoleService;
import Parcial.pw.Guilombo.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Usuarios", description = "API para la gestión de usuarios y sus roles")
@SecurityRequirement(name = "bearerAuth")
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService,
                          UserRoleService userRoleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Listar todos los usuarios",
            description = "Obtiene una lista de todos los usuarios registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver la lista de usuarios")
    })
    @GetMapping
    public String getAll(Model model) {
        List<UserEntity> users = userService.getAll();
        model.addAttribute("users", users);
        return "userList";
    }

    @Operation(summary = "Mostrar formulario de nuevo usuario",
            description = "Muestra el formulario para crear un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario mostrado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para crear usuarios")
    })
    @GetMapping("/newUser")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "newUser";
    }

    @Operation(summary = "Crear nuevo usuario",
            description = "Crea un nuevo usuario en el sistema con los roles especificados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para crear usuarios")
    })
    @PostMapping("/newUser")
    public String add(
            @Parameter(description = "ID del usuario") @RequestParam Integer user_id,
            @Parameter(description = "Nombre de usuario") @RequestParam String username,
            @Parameter(description = "Contraseña del usuario") @RequestParam String password,
            @Parameter(description = "Lista de roles asignados") @RequestParam(required = false) List<String> roles) {

        String encryptedPassword = passwordEncoder.encode(password);

        UserEntity newUser = new UserEntity();
        newUser.setUserId(user_id);
        newUser.setUsername(username);
        newUser.setPassword(encryptedPassword);
        newUser.setEnabled(true);

        userService.save(newUser);

        if (roles != null && !roles.isEmpty()) {
            for (String roleName : roles) {
                UserRoleEntity userRole = new UserRoleEntity();
                RoleEntity role = roleService.findByName(roleName);

                if (role != null) {
                    userRole.setUser(newUser);
                    userRole.setRole(role);
                    userRoleService.save(userRole);
                }
            }
        }
        return "redirect:/user";
    }

    @Operation(summary = "Mostrar formulario de edición",
            description = "Muestra el formulario para editar un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Formulario de edición mostrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "No autorizado para editar usuarios")
    })
    @GetMapping("/editar/{id}")
    public String showEditForm(
            @Parameter(description = "ID del usuario a editar") @PathVariable Integer id,
            Model model) {
        UserEntity user = userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        model.addAttribute("user", user);
        return "editUser";
    }

    @Operation(summary = "Actualizar usuario",
            description = "Actualiza la información de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para actualizar usuarios")
    })
    @PostMapping("/editar/{id}")
    public String updateUser(
            @Parameter(description = "ID del usuario") @PathVariable Integer id,
            @Parameter(description = "Nuevo nombre de usuario") @RequestParam String username,
            @Parameter(description = "Nueva contraseña (opcional)") @RequestParam(required = false) String password,
            @Parameter(description = "Estado de la cuenta") @RequestParam(required = false) Boolean enabled,
            Model model) {
        try {
            UserEntity user = userService.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            user.setUsername(username);
            user.setEnabled(enabled != null ? enabled : true);

            if (password != null && !password.trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }

            userService.save(user);
            model.addAttribute("successMessage", "User updated successfully");
            return "redirect:/user";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating user: " + e.getMessage());
            return "editUser";
        }
    }

    @Operation(summary = "Eliminar usuario",
            description = "Elimina un usuario existente del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "No autorizado para eliminar usuarios")
    })
    @PostMapping("/delete/{id}")
    public String deleteUser(
            @Parameter(description = "ID del usuario a eliminar") @PathVariable Integer id,
            Model model) {
        try {
            userService.deleteById(id);
            model.addAttribute("successMessage", "User deleted successfully");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/user";
    }
}