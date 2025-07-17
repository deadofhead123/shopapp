package com.example.shopapp.components.other;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Check if this request needs a token
        if(isBypassToken(request)){
            filterChain.doFilter(request, response); // Continue to execute request. If you don't have this method, the server returns "200 OK" immediately (no executing request)
        }

        // Get token to authenticate and authorize
        String header = request.getHeader("Authorization");
        if(header != null && header.contains("Bearer")){
            String token = header.substring(7);
            if(header != null && SecurityContextHolder.getContext().getAuthentication() != null){ // User logged in, Spring Security saved information
                token = header.substring(7);
            }
        }

    }

    private boolean isBypassToken(HttpServletRequest request){
        List<Pair<String, String>> bypassRequests = List.of(
                Pair.of(String.format("/%s/users/login", apiPrefix), "POST"),
                Pair.of(String.format("/%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("/%s/products", apiPrefix), "GET"),
                Pair.of(String.format("/%s/categories", apiPrefix), "GET")
        );
        for(Pair<String, String> item : bypassRequests){
            if(item.getKey().equals(request.getServletPath()) && item.getValue().equals(request.getMethod())){
                return true;
            }
        }
        return false;
    }
}
