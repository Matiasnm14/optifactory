package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.RecepcionRequestDto;
import edu.upb.optifacotry.repository.dto.response.RecepcionResponseDto;
import edu.upb.optifacotry.repository.entity.Proveedor;
import edu.upb.optifacotry.repository.entity.Recepcion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.RecepcionRepository;
import org.springframework.stereotype.Service;

@Service
public class RecepcionService extends CrudService<Recepcion, RecepcionRequestDto, RecepcionResponseDto> {

    public RecepcionService(RecepcionRepository repository) {
        super(repository);
    }

    @Override
    protected Recepcion newEntity() {
        return new Recepcion();
    }

    @Override
    protected void applyRequest(RecepcionRequestDto request, Recepcion entity) {
        entity.setProveedor(reference(Proveedor.class, request.proveedorId()));
        entity.setReferenciaCompra(request.referenciaCompra());
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setRecibidaEn(request.recibidaEn());
    }

    @Override
    protected RecepcionResponseDto toResponse(Recepcion entity) {
        return new RecepcionResponseDto(
            entity.getId(),
            referenceId(entity.getProveedor()),
            entity.getReferenciaCompra(),
            referenceId(entity.getResponsable()),
            entity.getRecibidaEn()
        );
    }
}
