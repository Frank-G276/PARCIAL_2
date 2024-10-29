package Parcial.pw.Guilombo.Web.Controller;


import Parcial.pw.Guilombo.Persistence.Entity.RoleEntity;
import Parcial.pw.Guilombo.Persistence.Entity.UserEntity;
import Parcial.pw.Guilombo.Persistence.Entity.UserRoleEntity;
import Parcial.pw.Guilombo.Service.RoleService;
import Parcial.pw.Guilombo.Service.UserRoleService;
import Parcial.pw.Guilombo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @GetMapping
    public String getAll(Model model) {
        List<UserEntity> users = userService.getAll();
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("/newUser")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "newUser";
    }

    @PostMapping("/newUser")
    public String add(@RequestParam Integer user_id,
                      @RequestParam String username,
                      @RequestParam String password,
                      @RequestParam(required = false) List<String> roles) {

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

    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        UserEntity user = userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/editar/{id}")
    public String updateUser(@PathVariable Integer id,
                             @RequestParam String username,
                             @RequestParam(required = false) String password,
                             @RequestParam(required = false) Boolean enabled,
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

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id, Model model) {
        try {
            userService.deleteById(id);
            model.addAttribute("successMessage", "User deleted successfully");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/user";
    }
}
