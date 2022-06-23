package app.web.pavelk.read2.schema;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;


@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", schema = "client")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(name = "username")
    private String username;

    @NotBlank(message = "Password is required")
    @Column(name = "password")
    private String password;

    @NotEmpty(message = "Email is required")
    @Column(name = "email")
    private String email;

    @Column(name = "created")
    private Instant created;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "roles")
    @Type(type = "app.web.pavelk.read2.schema.type.CustomUserRoleListType")
    private List<User.Role> roles;

    public enum Role implements GrantedAuthority {
        USER, ADMIN;

        @Override
        public String getAuthority() {
            return this.name();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
