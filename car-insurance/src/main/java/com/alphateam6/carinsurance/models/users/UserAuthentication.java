package com.alphateam6.carinsurance.models.users;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserAuthentication {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(mappedBy = "userAuthentication", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Authority> authorities;

    public UserAuthentication() {
        super();
        this.enabled = false;
    }

    public UserAuthentication(String username, String password) {
        super();
        this.username = username;
        this.password = password;
        this.enabled = false;
    }
}
