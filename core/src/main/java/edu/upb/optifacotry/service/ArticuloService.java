package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ArticuloRequestDto;
import edu.upb.optifacotry.repository.dto.response.ArticuloResponseDto;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.repos.ArticuloRepository;
import org.springframework.stereotype.Service;

@Service
public class ArticuloService extends CrudService<Articulo, ArticuloRequestDto, ArticuloResponseDto> {

    public ArticuloService(ArticuloRepository repository) {
        super(repository);
    }

    @Override
    protected Articulo newEntity() {
        return new Articulo();
    }

    @Override
    protected void applyRequest(ArticuloRequestDto request, Articulo entity) {
        entity.setCodigo(request.codigo());
        entity.setNombre(request.nombre());
        entity.setCategoria(request.categoria());
        entity.setUnidad(request.unidad());
        entity.setRequiereLote(request.requiereLote());
        entity.setActivo(request.activo());
    }

    @Override
    protected ArticuloResponseDto toResponse(Articulo entity) {
        return new ArticuloResponseDto(
            entity.getId(),
            entity.getCodigo(),
            entity.getNombre(),
            entity.getCategoria(),
            entity.getUnidad(),
            entity.getRequiereLote(),
            entity.getActivo()
        );
    }
}
