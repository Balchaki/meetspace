package br.balchaki.meetspace.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Table(name = "en_user")
@Entity(name = "User")
@EqualsAndHashCode(of = "userId")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("userId") @Column(name = "seq_user")
    private Long userId;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private Boolean isAdmin = false;

    public boolean isAdmin() {
        return isAdmin;
    }
}
