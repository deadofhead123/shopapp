package com.example.shopapp.configurations;

import com.example.shopapp.components.filter.JwtTokenFilter;
import com.example.shopapp.entities.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                                    HttpMethod.GET, String.format("%s/roles**", apiPrefix)).permitAll()

                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/categories?**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    HttpMethod.POST, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.PUT, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.DELETE, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/products**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/products/images/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    HttpMethod.POST, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN) // đã bao gồm request upload ảnh
                            .requestMatchers(
                                    HttpMethod.PUT, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.DELETE, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(
                                    HttpMethod.GET, String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.POST, String.format("%s/orders/**", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(
                                    HttpMethod.PUT, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    HttpMethod.DELETE, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable);
        http.cors((Customizer<CorsConfigurer<HttpSecurity>>) httpSecurityCorsConfigurer -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("*"));
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTION"));
            configuration.setAllowedHeaders(List.of("authorization", "x-auth-token", "content-type"));
            configuration.setExposedHeaders(List.of("x-auth-token"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            httpSecurityCorsConfigurer.configurationSource(source);
        });
        return http.build();
    }
}
