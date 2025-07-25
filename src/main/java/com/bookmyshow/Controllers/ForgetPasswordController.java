// src/main/java/com/bookmyshow/Controllers/ForgetPasswordController.java
package com.bookmyshow.Controllers;

import com.bookmyshow.Config.PasswordConfig;
import com.bookmyshow.Dtos.RequestDtos.ChangePassword;
import com.bookmyshow.Dtos.ResponseDtos.MailBody;
import com.bookmyshow.Models.ForgetPassword;
import com.bookmyshow.Models.User;
import com.bookmyshow.Repositories.ForgetPasswordRepository;
import com.bookmyshow.Repositories.UserRepository;
import com.bookmyshow.Services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/forgetpassword")
public class ForgetPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgetPasswordRepository forgetPasswordRepository;
    private final PasswordConfig passwordConfig;

    public ForgetPasswordController(UserRepository userRepository, EmailService emailService, ForgetPasswordRepository forgetPasswordRepository, PasswordConfig passwordConfig) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgetPasswordRepository = forgetPasswordRepository;
        this.passwordConfig = passwordConfig;
    }

    @PostMapping("/verifyemail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        // Find the user by email; throw RuntimeException if not found.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Generate a new OTP
        int otp = OTPGenrator();
        // Set expiration time for the OTP (1 minute from now)
        Date expirationTime = new Date(System.currentTimeMillis() + 60 * 1000);

        ForgetPassword fp; // Declare fp variable to hold the ForgetPassword object

        // Step 1: Check if a ForgetPassword entry already exists for this user
        // We use the findByUser method in the repository to get any existing OTP for this user.
        Optional<ForgetPassword> existingFpOptional = forgetPasswordRepository.findByUser(user);

        if (existingFpOptional.isPresent()) {
            // Step 2 (IF EXISTS): If an entry exists, update its OTP and expiration time.
            // This will result in an UPDATE statement when saved.
            fp = existingFpOptional.get(); // Retrieve the existing object
            fp.setOtp(otp); // Update the OTP
            fp.setExpirationTime(expirationTime); // Update the expiration time
            // The 'user' association remains the same, no need to set it again.
        } else {
            // Step 2 (IF NOT EXISTS): If no entry exists, create a new ForgetPassword object.
            // This will result in an INSERT statement when saved.
            fp = ForgetPassword.builder()
                    .otp(otp)
                    .expirationTime(expirationTime)
                    .user(user) // Associate the new OTP with the user.
                    .build();
        }

        // Prepare the email content with the generated OTP.
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is OTP (One Time Password) for your Forget Password Request: " + otp + ". It is valid for 1 minute.")
                .subject("OTP for Forget Password (BOOKMYSHOW)")
                .build();

        // Send the email.
        emailService.sendSimpleMessage(mailBody);

        // Save the ForgetPassword object.
        // If 'fp' was an existing object, this will update it.
        // If 'fp' was a new object, this will persist it.
        forgetPasswordRepository.save(fp);

        return ResponseEntity.ok("Email Sent For Verification");
    }

    @PostMapping("/verifyOTP/{otp}/{email}")
    public ResponseEntity<String> verifyOTP(@PathVariable Integer otp, @PathVariable String email){
        // Find the user by email; throw RuntimeException if not found.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Find the ForgetPassword entry by OTP and user.
        ForgetPassword fp = forgetPasswordRepository.findOTPAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        // Check if the OTP has expired.
        if(fp.getExpirationTime().before(Date.from(Instant.now()))){
            // If expired, delete the OTP record from the database.
            forgetPasswordRepository.deleteById(fp.getFpid());
            return new ResponseEntity<>("OTP has expired", HttpStatus.EXPECTATION_FAILED);
        }

        // OTP is valid and not expired.
        // Optionally: You might want to delete the OTP record immediately after successful verification
        // to prevent reuse, even if the password hasn't been changed yet.
        // forgetPasswordRepository.deleteById(fp.getFpid());

        return ResponseEntity.ok("OTP verified!");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword, @PathVariable String email){
        // Check if password and repeatpassword match.
        if(!Objects.equals(changePassword.password(), changePassword.repeatpassword())){
            return new ResponseEntity<>
                    ("Passwords do not match. Please enter the password again!", HttpStatus.EXPECTATION_FAILED);
        }

        // Encode the new password.
        String encodedPassword = passwordConfig.passwordEncoder().encode(changePassword.password());

        // Update the user's password in the database using the direct DML query.
        // This operation is performed directly on the database and does not affect
        // the JPA session's managed state of the User or ForgetPassword entities.
        forgetPasswordRepository.updatePassword(email, encodedPassword);

        // After successfully changing the password, it's crucial to invalidate/delete the OTP record.
        // To avoid TransientObjectException, we use a direct DELETE DML query by user ID.
        // First, get the user to retrieve their ID.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found during password change cleanup for email: " + email));

        // Use the new direct delete method in the repository.
        // This will delete the ForgetPassword entry linked to the user's ID,
        // bypassing any entity state issues and ensuring cleanup.
        forgetPasswordRepository.deleteByUserId(user.getId());

        return ResponseEntity.ok("Password has been changed successfully!");
    }

    // Helper method to generate a 6-digit OTP.
    private int OTPGenrator(){
        Random random = new Random();
        // Generates a random integer between 100,000 (inclusive) and 999,999 (inclusive).
        return random.nextInt(100_000, 999_999);
    }
}
