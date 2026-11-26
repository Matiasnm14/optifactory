package edu.upb.optifacotry.config;

import edu.upb.optifacotry.service.PermisosService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain seguridadHttp(HttpSecurity http, PermisosService permisos) throws Exception {
        return http.authorizeHttpRequests(autorizacion -> autorizacion.anyRequest().authenticated())
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .csrf(withDefaults())
                .addFilterAfter(new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                                    FilterChain cadena) throws ServletException, IOException {
                        permisos.iniciarPeticion();
                        try {
                            cadena.doFilter(request, response);
                        } finally {
                            permisos.terminarPeticion();
                        }
                    }
                }, AuthorizationFilter.class)
                .build();
    }
}
