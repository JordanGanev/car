package com.alphateam6.carinsurance.models;

import com.alphateam6.carinsurance.models.users.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "verification_token")
@Data
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "token")
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

//    private Date expiryDate;
//
//    private Date calculateExpiryDate(int expiryTimeInMinutes) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Timestamp(cal.getTime().getTime()));
//        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
//        return new Date(cal.getTime().getTime());
//    }
}
