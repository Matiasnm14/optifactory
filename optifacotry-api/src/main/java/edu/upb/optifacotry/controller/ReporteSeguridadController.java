package edu.upb.optifacotry.controller;

import edu.upb.optifacotry.repository.dto.request.ImagenEvidenciaRequestDto;
import edu.upb.optifacotry.repository.dto.request.ReporteSeguridadRequestDto;
import edu.upb.optifacotry.repository.dto.response.AdjuntoResponseDto;
import edu.upb.optifacotry.repository.dto.response.ImagenEvidenciaResponseDto;
import edu.upb.optifacotry.repository.dto.response.ReporteSeguridadResponseDto;
import edu.upb.optifacotry.service.AdjuntoService;
import edu.upb.optifacotry.service.ReporteSeguridadService;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/v1/reportes-seguridad")
public class ReporteSeguridadController extends ReadOnlyCrudController<ReporteSeguridadResponseDto> {

    private final ReporteSeguridadService reportes;
    private final AdjuntoService adjuntos;

    public ReporteSeguridadController(ReporteSeguridadService reportes, AdjuntoService adjuntos) {
        super(reportes);
        this.reportes = reportes;
        this.adjuntos = adjuntos;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReporteSeguridadResponseDto> registrar(
            @Valid @RequestBody ReporteSeguridadRequestDto request) {
        return creado(reportes.registrar(request, null));
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReporteSeguridadResponseDto> registrarConImagen(
            @Valid @RequestPart("reporte") ReporteSeguridadRequestDto reporte,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        return creado(reportes.registrar(reporte, leerImagen(imagen)));
    }

    @GetMapping("/{id}/adjuntos")
    public PagedModel<AdjuntoResponseDto> consultarAdjuntos(
            @PathVariable UUID id, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        reportes.findById(id);
        return new PagedModel<>(adjuntos.consultarReporte(id, pageable));
    }

    @GetMapping("/{reporteId}/adjuntos/{adjuntoId}/imagen")
    public ResponseEntity<byte[]> consultarImagen(@PathVariable UUID reporteId, @PathVariable UUID adjuntoId) {
        ImagenEvidenciaResponseDto imagen = reportes.consultarImagen(reporteId, adjuntoId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imagen.tipoContenido()))
                .cacheControl(CacheControl.noStore())
                .body(imagen.contenido());
    }

    private static ImagenEvidenciaRequestDto leerImagen(MultipartFile imagen) {
        if (imagen == null) {
            return null;
        }
        if (imagen.getSize() > ImagenEvidenciaRequestDto.MAX_BYTES) {
            throw new MaxUploadSizeExceededException(ImagenEvidenciaRequestDto.MAX_BYTES);
        }
        try {
            return new ImagenEvidenciaRequestDto(imagen.getOriginalFilename(), imagen.getBytes());
        } catch (IOException e) {
            throw new InvalidRequestException("No se pudo leer la imagen enviada.");
        }
    }

    private static ResponseEntity<ReporteSeguridadResponseDto> creado(ReporteSeguridadResponseDto reporte) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(reporte.id().toString())
                .build()
                .toUri();
        return ResponseEntity.created(location).body(reporte);
    }
}
