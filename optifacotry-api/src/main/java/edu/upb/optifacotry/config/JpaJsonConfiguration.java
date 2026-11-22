package edu.upb.optifacotry.config;

import org.hibernate.type.format.jackson.Jackson3JsonFormatMapper;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

import static org.hibernate.cfg.MappingSettings.JSON_FORMAT_MAPPER;

@Configuration(proxyBeanMethods = false)
public class JpaJsonConfiguration {

    @Bean
    HibernatePropertiesCustomizer jsonFormatMapper(JsonMapper jsonMapper) {
        return properties -> properties.put(JSON_FORMAT_MAPPER, new Jackson3JsonFormatMapper(jsonMapper));
    }
}
