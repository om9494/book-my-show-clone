package com.bookmyshow.Security;

import com.bookmyshow.Config.PasswordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.bookmyshow.Config.PasswordConfig.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordConfig passwordConfig;
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup/register", "/signup/login").permitAll()

                        .requestMatchers("/movies/add", "/movies/*/").hasRole("ADMIN")
                        .requestMatchers("/shows/addShow", "/shows/updateShow/**", "/shows/deleteShow/**").hasRole("ADMIN")
                        .requestMatchers("/shows/associateShowSeats").hasRole("ADMIN")

                        .requestMatchers("/movies/all", "/movies/id/**", "/movies/totalCollection/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shows/getAllShows", "/shows/getShowById/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shows/showTimingsOnDate", "/shows/movieHavingMostShows").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/seats/**").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/bookings/**", "/ticket/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {});

        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordConfig.passwordEncoder());
    }
}