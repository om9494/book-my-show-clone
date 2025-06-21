package com.example.Super30_Project.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //Tells Spring that this class contains **bean definitions**
@EnableMethodSecurity // allows @PreAuthorize
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //CSRF = Cross Site Request Forgery protection
                //It's useful for web apps with sessions/forms — but we're building a REST API
                //So, we disable it to avoid issues with tools like Postman.
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        //Don’t care what the path is — if someone makes a request,
                        //they must be logged in.
                        //if not used, then everything is left open
                        .anyRequest().authenticated()
                )
                //Below line enables **Basic Authentication** — where Postman asks for a
                //username/password and sends them with every request.
                //This is perfect for development and testing.
                .httpBasic(Customizer.withDefaults()); // for Postman/testing
        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        //users defined for testing so no DB required
        UserDetails admin = User
                .withUsername("admin")
                .password("{noop}admin123") // {noop} disables password encoding
                //{noop} needed or we'll get errors about password storage format
                .roles("ADMIN")
                .build();

        UserDetails user = User
                .withUsername("user")
                .password("{noop}user123")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
