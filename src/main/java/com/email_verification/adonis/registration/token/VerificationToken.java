package com.email_verification.adonis.registration.token;

import com.email_verification.adonis.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Calendar;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date tokenExpirationTime;

    private static final int EXPIRATION_TIME = 60;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationToken(String token) {
        this.token = token;
        this.tokenExpirationTime = calculateTokenExpirationTime();
    }

    public VerificationToken(User user, String token) {
        this.token = token;
        this.user = user;
        this.tokenExpirationTime = calculateTokenExpirationTime();
    }

    public Date calculateTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);

        return new Date(calendar.getTime().getTime());
    }
}
