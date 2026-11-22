package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.AlertaRequestDto;
import edu.upb.optifacotry.repository.dto.response.AlertaResponseDto;
import edu.upb.optifacotry.repository.entity.Alerta;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.Linea;
import edu.upb.optifacotry.repository.entity.Lote;
import edu.upb.optifacotry.repository.entity.Maquina;
import edu.upb.optifacotry.repository.entity.OrdenProduccion;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.AlertaRepository;
import org.springframework.stereotype.Service;

@Service
public class AlertaService extends CrudService<Alerta, AlertaRequestDto, AlertaResponseDto> {

    public AlertaService(AlertaRepository repository) {
        super(repository);
    }

    @Override
    protected Alerta newEntity() {
        return new Alerta();
    }

    @Override
    protected void applyRequest(AlertaRequestDto request, Alerta entity) {
        entity.setArea(reference(Area.class, request.areaId()));
        entity.setOrigen(request.origen());
        entity.setMaquina(reference(Maquina.class, request.maquinaId()));
        entity.setLinea(reference(Linea.class, request.lineaId()));
        entity.setOrden(reference(OrdenProduccion.class, request.ordenId()));
        entity.setArticulo(reference(Articulo.class, request.articuloId()));
        entity.setLote(reference(Lote.class, request.loteId()));
        entity.setDetalle(request.detalle());
        entity.setGravedad(request.gravedad());
        entity.setEstado(request.estado());
        entity.setCreadaEn(request.creadaEn());
        entity.setReconocidoPor(reference(Usuario.class, request.reconocidoPor()));
        entity.setReconocidoEn(request.reconocidoEn());
        entity.setCerradoPor(reference(Usuario.class, request.cerradoPor()));
        entity.setCerradoEn(request.cerradoEn());
        entity.setResultadoCierre(request.resultadoCierre());
    }

    @Override
    protected AlertaResponseDto toResponse(Alerta entity) {
        return new AlertaResponseDto(
            entity.getId(),
            referenceId(entity.getArea()),
            entity.getOrigen(),
            referenceId(entity.getMaquina()),
            referenceId(entity.getLinea()),
            referenceId(entity.getOrden()),
            referenceId(entity.getArticulo()),
            referenceId(entity.getLote()),
            entity.getDetalle(),
            entity.getGravedad(),
            entity.getEstado(),
            entity.getCreadaEn(),
            referenceId(entity.getReconocidoPor()),
            entity.getReconocidoEn(),
            referenceId(entity.getCerradoPor()),
            entity.getCerradoEn(),
            entity.getResultadoCierre()
        );
    }
}
