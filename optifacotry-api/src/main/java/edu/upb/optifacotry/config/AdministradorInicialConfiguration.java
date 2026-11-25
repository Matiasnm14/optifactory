package edu.upb.optifacotry.config;

import edu.upb.optifacotry.service.UsuarioRolService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AdministradorInicialConfiguration {

    @Bean
    @ConditionalOnProperty("optifactory.security.bootstrap-admin")
    ApplicationRunner aprovisionarAdministrador(UsuarioRolService roles,
            @Value("${optifactory.security.bootstrap-admin}") String principal) {
        if (principal.isBlank()) {
            throw new IllegalArgumentException("El principal del administrador inicial no puede estar vacío.");
        }
        return args -> roles.aprovisionarAdministrador(principal);
    }
}
