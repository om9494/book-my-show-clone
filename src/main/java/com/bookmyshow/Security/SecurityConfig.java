package com.bookmyshow.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // Import this
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Import this
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Import this
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bookmyshow.Config.JwtAuthFilter;
import com.bookmyshow.Config.PasswordConfig;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // Assuming you have this
    @Autowired
    private PasswordConfig passwordConfig; // Assuming this contains your PasswordEncoder bean
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // Configure CORS here directly or use a specific CORS source if needed
                // For simple cases, this is sufficient:
                .cors(cors -> cors.and()) // This enables CORS configuration globally if a CorsConfigurationSource bean is present, or uses default
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup/register", "/signup/login").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow preflight requests
                        .requestMatchers("/movies/all", "/movies/id/**", "/movies/search").permitAll() // Allow public access to all movies and get by ID
                        .requestMatchers("/signup/profile").authenticated()
                        .requestMatchers("/movies/add", "/movies/*/").hasRole("ADMIN") // Note: /movies/*/ will also match /movies/id/
                        .requestMatchers("/shows/addShow", "/shows/updateShow/**", "/shows/deleteShow/**").hasRole("ADMIN")
                        .requestMatchers("/shows/associateShowSeats").hasRole("ADMIN")
                        .requestMatchers("/theaters/addTheater", "/theaters/updateTheater/**", "/theaters/deleteTheater/**").hasRole("ADMIN")
                        .requestMatchers("/theater-seats/addTheaterSeat", "/theater-seats/updateTheaterSeat/**", "/theater-seats/deleteTheaterSeat/**").hasRole("ADMIN")
                        .requestMatchers("/theaters/getAllTheaters", "/theaters/getTheaterById/**", "/theaters/getTheaterByCity/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/theater-seats/getSeatsByTheater/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/mysql/query").hasAnyRole("USER", "ADMIN")
                        // .requestMatchers("/movies/all", "/movies/id/**", "/movies/totalCollection/**").hasAnyRole("USER", "ADMIN") // Original, changed above
                        .requestMatchers("/movies/totalCollection/**").hasAnyRole("USER", "ADMIN") // Keep this restricted
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

    // You can remove the configure method if you're using a DaoAuthenticationProvider bean
    // and rely on Spring Security's auto-configuration for AuthenticationManager.
    // However, if you explicitly want to define it, use the following:
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