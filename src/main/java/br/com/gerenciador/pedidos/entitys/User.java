package br.com.gerenciador.pedidos.entitys;

import br.com.gerenciador.pedidos.core.converters.RoleConverter;
import br.com.gerenciador.pedidos.core.records.input.UserRegisterInput;
import br.com.gerenciador.pedidos.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "USER_INFO")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Convert(converter = RoleConverter.class)
    @Column(name = "ROLE")
    private Role role;

    public User(UserRegisterInput input) {
        this.password = input.password();
        this.username = input.password();
        this.lastName = input.lastName();
        this.firstName = input.firstName();
        this.role = Role.getById(input.role());
    }

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString();
        final var passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
