package com.bookmyshow.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bookmyshow.Config.JwtAuthFilter;
import com.bookmyshow.Config.PasswordConfig;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private PasswordConfig passwordConfig;
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.and())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup/register", "/signup/login").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movies/all", "/movies/id/**", "/movies/search").permitAll()
                        .requestMatchers("/signup/profile").authenticated()

                        // Corrected Theatre endpoint rules
                        .requestMatchers(HttpMethod.POST, "/theaters").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/theaters/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/theaters/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/theaters", "/theaters/city/**").hasAnyRole("USER", "ADMIN") // Corrected for /theaters and /theaters/city/{city}

                        // Corrected Theater Seat endpoint rules
                        .requestMatchers(HttpMethod.POST, "/theater-seats").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/theater-seats/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/theater-seats/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/theater-seats/theater/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/theater-seats/bulk").hasRole("ADMIN") // New rule


                        .requestMatchers("/movies/add", "/movies/*/").hasRole("ADMIN")
                        .requestMatchers("/shows/addShow", "/shows/updateShow/**", "/shows/deleteShow/**").hasRole("ADMIN")
                        .requestMatchers("/shows/associateShowSeats").hasRole("ADMIN")

                        .requestMatchers("/movies/totalCollection/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shows/getAllShows", "/shows/getShowById/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shows/showTimingsOnDate", "/shows/theaterAndShowTimingsByMovie", "/shows/movieHavingMostShows").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/seats/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/bookings/**", "/ticket/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {})
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordConfig.passwordEncoder());
        return authProvider;
    }
}
