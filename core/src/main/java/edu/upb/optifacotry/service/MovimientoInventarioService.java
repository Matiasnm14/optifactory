package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.MovimientoInventarioRequestDto;
import edu.upb.optifacotry.repository.dto.response.MovimientoInventarioResponseDto;
import edu.upb.optifacotry.repository.entity.Almacen;
import edu.upb.optifacotry.repository.entity.Articulo;
import edu.upb.optifacotry.repository.entity.DetalleRecepcion;
import edu.upb.optifacotry.repository.entity.Intervencion;
import edu.upb.optifacotry.repository.entity.Lote;
import edu.upb.optifacotry.repository.entity.MovimientoInventario;
import edu.upb.optifacotry.repository.entity.OperacionOrden;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;

@Service
public class MovimientoInventarioService extends CrudService<MovimientoInventario, MovimientoInventarioRequestDto, MovimientoInventarioResponseDto> {

    public MovimientoInventarioService(MovimientoInventarioRepository repository) {
        super(repository);
    }

    @Override
    protected MovimientoInventario newEntity() {
        return new MovimientoInventario();
    }

    @Override
    protected void applyRequest(MovimientoInventarioRequestDto request, MovimientoInventario entity) {
        entity.setTipo(request.tipo());
        entity.setArticulo(reference(Articulo.class, request.articuloId()));
        entity.setLote(reference(Lote.class, request.loteId()));
        entity.setAlmacen(reference(Almacen.class, request.almacenId()));
        entity.setCantidad(request.cantidad());
        entity.setOperacionOrden(reference(OperacionOrden.class, request.operacionOrdenId()));
        entity.setIntervencion(reference(Intervencion.class, request.intervencionId()));
        entity.setDetalleRecepcion(reference(DetalleRecepcion.class, request.detalleRecepcionId()));
        entity.setMovimientoOrigen(reference(MovimientoInventario.class, request.movimientoOrigenId()));
        entity.setResponsable(reference(Usuario.class, request.responsableId()));
        entity.setRegistradoEn(request.registradoEn());
        entity.setClaveOperacion(request.claveOperacion());
    }

    @Override
    protected MovimientoInventarioResponseDto toResponse(MovimientoInventario entity) {
        return new MovimientoInventarioResponseDto(
            entity.getId(),
            entity.getTipo(),
            referenceId(entity.getArticulo()),
            referenceId(entity.getLote()),
            referenceId(entity.getAlmacen()),
            entity.getCantidad(),
            referenceId(entity.getOperacionOrden()),
            referenceId(entity.getIntervencion()),
            referenceId(entity.getDetalleRecepcion()),
            referenceId(entity.getMovimientoOrigen()),
            referenceId(entity.getResponsable()),
            entity.getRegistradoEn(),
            entity.getClaveOperacion()
        );
    }
}
