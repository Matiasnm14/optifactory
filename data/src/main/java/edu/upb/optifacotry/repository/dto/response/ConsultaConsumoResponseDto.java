package edu.upb.optifacotry.repository.dto.response;

import org.springframework.data.web.PagedModel;

import java.util.List;

public record ConsultaConsumoResponseDto(
    PagedModel<RegistroConsumoResponseDto> registros,
    List<TotalConsumoResponseDto> totales
) {
}
