// src/main/java/com/bookmyshow/Models/ForgetPassword.java
package com.bookmyshow.Models;

import com.fasterxml.jackson.annotation.JsonIgnore; // Required for @JsonIgnore
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpid;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    // Defines a One-to-One relationship with the User entity.
    // fetch = FetchType.LAZY: The User object will be loaded only when explicitly accessed.
    // @JoinColumn: Specifies the foreign key column in the 'forget_password' table.
    //   - name = "user_id": Sets the column name to 'user_id'.
    //   - unique = true: Ensures that only one ForgetPassword entry can exist per user_id.
    //                    This is crucial for preventing "Duplicate entry" errors.
    //   - nullable = false: Ensures that every ForgetPassword entry must be linked to a User.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    @JsonIgnore // Prevents circular reference during JSON serialization, crucial for API responses.
    private User user;
}
