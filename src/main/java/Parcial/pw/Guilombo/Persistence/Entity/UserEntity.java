package Parcial.pw.Guilombo.Persistence.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "parcial")
@Data
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    private String username;
    private String password;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            schema = "exersice",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rolename")
    )
    private Set<RoleEntity> roles = new HashSet<>();
}
