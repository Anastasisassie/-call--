package com.app.model;

import com.app.model.enums.ApplicationStatus;
import com.app.model.enums.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
public class User implements Serializable, UserDetails {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String fio = "ФИО";
    private String tel = "Номер телефона";

    @Column(length = 1000)
    private String img = "/img/avatar.png";

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Application> applicationsClient = new ArrayList<>();
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Application> applicationsEmployee = new ArrayList<>();
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Application> applicationsManager = new ArrayList<>();
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<ApplicationComment> applicationComments = new ArrayList<>();

    public User(String username, String password, Role role) {
        this.role = role;
        this.username = username;
        this.password = passwordEncoder().encode(password);
    }

    public List<Application> getApplicationsClient() {
        applicationsClient.sort(Comparator.comparing(Application::getId));
        Collections.reverse(applicationsClient);
        return applicationsClient;
    }

    public int getApplicationsEmployeeSolvedCount() {
        return applicationsEmployee.stream().reduce(0, (i, app) -> {
            if (app.getStatus() == ApplicationStatus.SOLVED && app.getManager() == null) return i + 1;
            return i;
        }, Integer::sum);
    }

    public int getApplicationsManagerSolvedCount() {
        return applicationsManager.stream().reduce(0, (i, app) -> {
            if (app.getStatus() == ApplicationStatus.SOLVED) return i + 1;
            return i;
        }, Integer::sum);
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(getRole());
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
