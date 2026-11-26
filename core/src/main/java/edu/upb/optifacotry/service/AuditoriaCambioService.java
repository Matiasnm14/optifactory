package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.response.AuditoriaCambioResponseDto;
import edu.upb.optifacotry.repository.entity.AuditoriaCambio;
import edu.upb.optifacotry.repository.entity.BaseEntity;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.AuditoriaCambioRepository;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuditoriaCambioService {
    private AuditoriaCambioRepository auditoriaCambioRepository;

    public void agregarAuditoria(String entidad,
                                 String accion,
                                 JsonNode anterior,
                                 JsonNode nuevo,
                                 String motivo,
                                 Usuario usuario) {
        AuditoriaCambio auditoriaCambio = new AuditoriaCambio();
        auditoriaCambio.setEntidad(entidad);
        auditoriaCambio.setAccion(accion);
        auditoriaCambio.setMotivo(motivo);
        auditoriaCambio.setValoresAnteriores(anterior);
        auditoriaCambio.setValoresNuevos(nuevo);
        auditoriaCambio.setUsuario(usuario);
        auditoriaCambio.setOcurridoEn(Instant.now());
        auditoriaCambioRepository.save(auditoriaCambio);
    }
}
