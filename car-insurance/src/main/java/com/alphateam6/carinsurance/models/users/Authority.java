package com.alphateam6.carinsurance.models.users;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authorities")
@Getter
@Setter
public class Authority implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "username")
    private UserAuthentication userAuthentication;

    @Id
    @Column(name = "authority")
    private String authority;

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }
}
