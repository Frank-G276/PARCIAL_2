package Parcial.pw.Guilombo.Persistence.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "roles", schema = "parcial")
@Data
public class RoleEntity {
    @Id
    private String rolename;

    @Override
    public String toString() {
        return "Role[rolename=" + rolename + "]";
    }
}
