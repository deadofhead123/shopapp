package com.example.shopapp.configurations;

import com.example.shopapp.components.other.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) // filter with username, password saved when logging in
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                                    String.format("%s/users/login", apiPrefix),
                                    String.format("%s/users/register", apiPrefix)
                            ).permitAll()
                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    HttpMethod.POST, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.PUT, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.DELETE, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    HttpMethod.POST, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.PUT, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.DELETE, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/orders/**", apiPrefix)).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(
                                    HttpMethod.POST, String.format("%s/orders/**", apiPrefix)).hasRole("USER")
                            .requestMatchers(
                                    HttpMethod.PUT, String.format("%s/orders/**", apiPrefix)).hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.DELETE, String.format("%s/orders/**", apiPrefix)).hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
