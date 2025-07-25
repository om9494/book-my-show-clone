package com.bookmyshow.Security;

import com.bookmyshow.Config.JwtAuthFilter;
import com.bookmyshow.Config.PasswordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                .cors(cors -> cors.and()) // Enables CORS based on your CorsConfig.java
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints for login, registration, and browsing movies
                        .requestMatchers("/signup/register", "/signup/login", "/forgetpassword/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow pre-flight requests
                        .requestMatchers("/movies/all", "/movies/id/**", "/movies/search").permitAll()

                        // Authenticated user endpoints
                        .requestMatchers("/signup/profile").authenticated()
                        .requestMatchers("/theaters/getAllTheaters", "/theaters/getTheaterById/**", "/theaters/getTheaterByCity/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/theater-seats/getSeatsByTheater/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/mysql/query").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/movies/totalCollection/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shows/getAllShows", "/shows/getShowById/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shows/showTimingsOnDate", "/shows/theaterAndShowTimingsByMovie", "/shows/movieHavingMostShows").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/seats/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/bookings/**", "/ticket/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/reviews/movie/**").permitAll() // Anyone can view reviews
                        .requestMatchers("/reviews/**").authenticated()

                        // --- NEW RULE FOR PAYMENT ---
                        // This secures the Razorpay payment endpoints.
                        // Only logged-in users (USER or ADMIN) can create or verify a payment.
                        .requestMatchers("/api/payment/**").hasAnyRole("USER", "ADMIN")

                        // Admin-only endpoints
                        .requestMatchers("/movies/add", "/movies/*/").hasRole("ADMIN")
                        .requestMatchers("/shows/addShow", "/shows/updateShow/**", "/shows/deleteShow/**").hasRole("ADMIN")
                        .requestMatchers("/shows/associateShowSeats").hasRole("ADMIN")
                        .requestMatchers("/theaters/addTheater", "/theaters/updateTheater/**", "/theaters/deleteTheater/**").hasRole("ADMIN")
                        .requestMatchers("/theater-seats/addTheaterSeat", "/theater-seats/updateTheaterSeat/**", "/theater-seats/deleteTheaterSeat/**").hasRole("ADMIN")
                        
                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {})
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordConfig.passwordEncoder());
        provider.setUserDetailsService(customUserDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // @Bean
    // public DaoAuthenticationProvider authenticationProvider() {
    //     DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    //     authProvider.setUserDetailsService(customUserDetailsService);
    //     authProvider.setPasswordEncoder(passwordConfig.passwordEncoder());
    //     return authProvider;
    // }
}