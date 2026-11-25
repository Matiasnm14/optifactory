package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.MaquinaRequestDto;
import edu.upb.optifacotry.repository.dto.response.MaquinaResponseDto;
import edu.upb.optifacotry.repository.entity.FuenteIntegracion;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.repos.MaquinaRepository;
import org.springframework.stereotype.Service;

@Service
public class MaquinaService extends CrudService<Maquina, MaquinaRequestDto, MaquinaResponseDto> {

    public MaquinaService(MaquinaRepository repository) {
        super(repository);
    }

    @Override
    protected Maquina newEntity() {
        return new Maquina();
    }

    @Override
    protected void applyRequest(MaquinaRequestDto request, Maquina entity) {
        entity.setCodigo(request.codigo());
        entity.setLinea(reference(Linea.class, request.lineaId()));
        entity.setFuente(reference(FuenteIntegracion.class, request.fuenteId()));
        entity.setNombre(request.nombre());
        entity.setUbicacion(request.ubicacion());
        entity.setIdentificadorExterno(request.identificadorExterno());
        entity.setActivo(request.activo());
    }

    @Override
    protected MaquinaResponseDto toResponse(Maquina entity) {
        return new MaquinaResponseDto(
            entity.getId(),
            entity.getCodigo(),
            referenceId(entity.getLinea()),
            referenceId(entity.getFuente()),
            entity.getNombre(),
            entity.getUbicacion(),
            entity.getIdentificadorExterno(),
            entity.getActivo()
        );
    }
}
