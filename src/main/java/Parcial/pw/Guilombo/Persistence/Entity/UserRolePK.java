package Parcial.pw.Guilombo.Persistence.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class UserRolePK implements Serializable {
    @Column(name = "user_id")
    private Integer userId;

    private String rolename;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRolePK that = (UserRolePK) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(rolename, that.rolename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, rolename);
    }
}