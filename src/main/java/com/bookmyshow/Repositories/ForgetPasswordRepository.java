// src/main/java/com/bookmyshow/Repositories/ForgetPasswordRepository.java
package com.bookmyshow.Repositories;

import com.bookmyshow.Models.ForgetPassword;
import com.bookmyshow.Models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, Integer> {
    // Custom query to find an OTP for a specific user.
    // Used in verifyOTP to validate the OTP against the user.
    @Query("Select fp from ForgetPassword fp where fp.otp = ?1 and fp.user = ?2")
    Optional<ForgetPassword> findOTPAndUser(Integer otp, User user);

    // Custom query to update the password for a user by email.
    // This is a direct DML operation, bypassing entity loading.
    @Modifying
    @Transactional
    @Query("update User u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);

    // Finds a ForgetPassword entry associated with a given User object.
    // Used in verifyEmail to check for and update an existing OTP.
    Optional<ForgetPassword> findByUser(User user);

    // --- CRITICAL FIX FOR TransientObjectException ---
    // This method performs a direct DELETE DML operation based on the user's ID.
    // It is explicitly marked @Modifying and @Transactional to ensure it runs as a DML.
    // This bypasses Hibernate's entity state management for this specific deletion,
    // making it robust against the TransientObjectException when mixed with other direct DML.
    @Modifying
    @Transactional
    @Query("DELETE FROM ForgetPassword fp WHERE fp.user.id = ?1")
    void deleteByUserId(Integer userId);
}
