package Parcial.pw.Guilombo.Persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_role", schema = "parcial")
@Data
public class UserRoleEntity {
    @EmbeddedId
    private UserRolePK id = new UserRolePK();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("rolename")
    @JoinColumn(name = "rolename")
    private RoleEntity role;
}