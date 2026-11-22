package edu.upb.optifacotry.repository.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record MovimientoInventarioRequestDto(
    String tipo,
    UUID articuloId,
    UUID loteId,
    UUID almacenId,
    BigDecimal cantidad,
    UUID operacionOrdenId,
    UUID intervencionId,
    UUID detalleRecepcionId,
    UUID movimientoOrigenId,
    UUID responsableId,
    Instant registradoEn,
    UUID claveOperacion
) {
}
