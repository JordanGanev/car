package com.alphateam6.carinsurance.models.users;

import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_details")
@Data
@ToString(exclude = {"policies"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "username")
    private UserAuthentication userAuthentication;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Formula(value = " concat(first_name, ' ', last_name) ")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<QuoteRequest> policies;

    public String getUsername() {
        return userAuthentication.getUsername();
    }

    public boolean isAdmin() {
        return userAuthentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
