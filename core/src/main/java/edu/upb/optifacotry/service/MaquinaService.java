package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.MaquinaRequestDto;
import edu.upb.optifacotry.repository.dto.request.MaquinaUpdateDto;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.repos.LineaRepository;
import edu.upb.optifacotry.repository.repos.MaquinaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.UUID;

@Service
public class MaquinaService {

    private LineaRepository lineaRepository;
    private MaquinaRepository repository;
    private AuditoriaCambioService auditoriaCambioService;

    private ResponseEntity<Linea> asignarLinea(UUID idMachine, UUID idLine){
        Maquina machine = repository.getReferenceById(idMachine);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode antiguo = mapper.valueToTree(machine);
        Optional<Linea> linea = lineaRepository.findById(idLine);
        if (linea.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        machine.setLinea(linea.get());
        repository.save(machine);
        mapper = new ObjectMapper();
        JsonNode nuevo = mapper.valueToTree(machine);
        auditoriaCambioService.agregarAuditoria("MAQUINAS", "ACTUALIZAR", antiguo, nuevo, null, null);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity crearMaquinaSinLinea(MaquinaRequestDto request) {
        Maquina machine = new Maquina();
        machine.setCodigo(request.codigo());
        machine.setNombre(request.nombre());
        machine.setLinea(null);
        machine.setUbicacion(request.ubicacion());
        machine.setActivo(request.activo());
        machine.setFuente(null);
        machine.setCodigo(request.codigo());
        machine.setIdentificadorExterno(null);
        repository.save(machine);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodo = mapper.valueToTree(machine);
        auditoriaCambioService.agregarAuditoria("MAQUINAS", "CREAR", null, nodo, null, null);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateData(MaquinaUpdateDto request) {
        Maquina maquina = repository.findById(request.id()).orElse(null);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode antiguo = mapper.valueToTree(maquina);
        Linea linea = lineaRepository.findById(request.idLine()).orElse(null);
        if (maquina == null) {
            return ResponseEntity.notFound().build();
        }
        if (linea != null && maquina.getLinea() != linea) {
            maquina.setLinea(linea);
        }
        if (!maquina.getUbicacion().equals(request.ubicacion())) {
            maquina.setUbicacion(request.ubicacion());
        }
        mapper = new ObjectMapper();
        JsonNode nuevo = mapper.valueToTree(maquina);
        repository.save(maquina);
        auditoriaCambioService.agregarAuditoria("MAQUINAS", "ACTUALIZAR", antiguo, nuevo, null, null);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity asignarUbicacion(UUID id, String ubicacion){
        Maquina machine = repository.findById(id).orElse(null);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode antiguo = mapper.valueToTree(machine);
        if (machine == null) {
            return ResponseEntity.notFound().build();
        }
        machine.setUbicacion(ubicacion);
        mapper = new ObjectMapper();
        JsonNode nuevo = mapper.valueToTree(machine);
        repository.save(machine);
        auditoriaCambioService.agregarAuditoria("MAQUINAS", "ACTUALIZAR", antiguo, nuevo, null, null);
        return ResponseEntity.ok().build();
    }
}
