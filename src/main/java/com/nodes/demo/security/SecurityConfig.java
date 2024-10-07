package com.nodes.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final long MAX_AGE_SECS = 3600;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigin;

//    public SecurityConfig(
//            CustomUserDetailsService customUserDetailsService,
//            JwtAuthenticationFilter jwtAuthenticationFilter,
//            JwtAuthenticationEntryPoint unauthorizedHandler  ) {
////        this.authenticationProvider = authenticationProvider;
//        this.customUserDetailsService = customUserDetailsService;
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//        this.unauthorizedHandler = unauthorizedHandler;
//    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
////                        .allowedOrigins("http://localhost:5000")
//                        .allowedOrigins(allowedOrigin)
////                        .allowedOrigins("*")
////                       .allowCredentials(true)
//                        .allowedHeaders("Authorization", "Content-Type")
//                        .allowedMethods("HEAD",  "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//                        .maxAge(MAX_AGE_SECS);
////                registry.addMapping("/**")
////                        .allowedOrigins("http://allowed-origin.com")
////                        .allowedMethods("GET", "POST", "PUT", "DELETE")
//            }
//        };
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Replace with your frontend URL
        configuration.setAllowedOrigins(List.of(allowedOrigin)); // Replace with your frontend URL
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//        configuration.addAllowedMethod("*");
//        configuration.addAllowedHeader("*");
//        configuration.setAllowCredentials(true);
        configuration.setMaxAge(MAX_AGE_SECS);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Use the CorsConfigurationSource bean

                .authorizeHttpRequests(authorizeRequests ->
                                authorizeRequests
//                                .requestMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                                        .requestMatchers("/").permitAll()
                                        .requestMatchers("/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/nodes/add/**").permitAll()
                                        .requestMatchers("/api/nodes/**").permitAll()
                                        .anyRequest().authenticated()
                )
//
////                .exceptionHandling(exceptionHandling ->
////                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
////                )
//
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )


        ;

        return http.build();
    }


}