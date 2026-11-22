package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.MensajeRecibidoRequestDto;
import edu.upb.optifacotry.repository.dto.response.MensajeRecibidoResponseDto;
import edu.upb.optifacotry.repository.entity.FuenteIntegracion;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.MensajeRecibido;
import edu.upb.optifacotry.repository.repos.MensajeRecibidoRepository;
import org.springframework.stereotype.Service;

@Service
public class MensajeRecibidoService extends CrudService<MensajeRecibido, MensajeRecibidoRequestDto, MensajeRecibidoResponseDto> {

    public MensajeRecibidoService(MensajeRecibidoRepository repository) {
        super(repository);
    }

    @Override
    protected MensajeRecibido newEntity() {
        return new MensajeRecibido();
    }

    @Override
    protected void applyRequest(MensajeRecibidoRequestDto request, MensajeRecibido entity) {
        entity.setFuente(reference(FuenteIntegracion.class, request.fuenteId()));
        entity.setMaquina(reference(Maquina.class, request.maquinaId()));
        entity.setIdentificadorExterno(request.identificadorExterno());
        entity.setContenido(request.contenido());
        entity.setOrigenEn(request.origenEn());
        entity.setRecibidoEn(request.recibidoEn());
        entity.setValido(request.valido());
        entity.setError(request.error());
    }

    @Override
    protected MensajeRecibidoResponseDto toResponse(MensajeRecibido entity) {
        return new MensajeRecibidoResponseDto(
            entity.getId(),
            referenceId(entity.getFuente()),
            referenceId(entity.getMaquina()),
            entity.getIdentificadorExterno(),
            entity.getContenido(),
            entity.getOrigenEn(),
            entity.getRecibidoEn(),
            entity.getValido(),
            entity.getError()
        );
    }
}
