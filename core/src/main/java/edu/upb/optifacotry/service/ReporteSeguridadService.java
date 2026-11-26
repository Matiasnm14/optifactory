package edu.upb.optifacotry.service;

import edu.upb.optifacotry.repository.dto.request.ImagenEvidenciaRequestDto;
import edu.upb.optifacotry.repository.dto.request.ReporteSeguridadRequestDto;
import edu.upb.optifacotry.repository.dto.response.ImagenEvidenciaResponseDto;
import edu.upb.optifacotry.repository.dto.response.ReporteSeguridadResponseDto;
import edu.upb.optifacotry.repository.entity.Adjunto;
import edu.upb.optifacotry.repository.entity.Area;
import edu.upb.optifacotry.repository.entity.ImagenAdjunto;
import edu.upb.optifacotry.repository.entity.ReporteSeguridad;
import edu.upb.optifacotry.repository.entity.Usuario;
import edu.upb.optifacotry.repository.repos.AdjuntoRepository;
import edu.upb.optifacotry.repository.repos.AreaRepository;
import edu.upb.optifacotry.repository.repos.ImagenAdjuntoRepository;
import edu.upb.optifacotry.repository.repos.ReporteSeguridadRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

@Service
public class ReporteSeguridadService {

    private static final long MAX_PIXELES = 16_000_000L;

    private final PermisosService permisos;
    private final AreaRepository areas;
    private final AdjuntoRepository adjuntos;
    private final ImagenAdjuntoRepository imagenes;
    private final ReporteSeguridadRepository repository;
    private final EntidadesJpa entidades;

    public ReporteSeguridadService(ReporteSeguridadRepository repository,
                                   PermisosService permisos,
                                   AreaRepository areas,
                                   AdjuntoRepository adjuntos,
                                   ImagenAdjuntoRepository imagenes,
                                   EntidadesJpa entidades) {
        this.repository = repository;
        this.permisos = permisos;
        this.areas = areas;
        this.adjuntos = adjuntos;
        this.imagenes = imagenes;
        this.entidades = entidades;
    }

    @PreAuthorize("@permisosService.tieneCuentaActiva()")
    @Transactional(readOnly = true)
    public Page<ReporteSeguridadResponseDto> findAll(Pageable pageable) {
        return repository.findAll(visibles(), pageable).map(this::toResponse);
    }

    @PreAuthorize("@permisosService.tieneCuentaActiva()")
    @Transactional(readOnly = true)
    public ReporteSeguridadResponseDto findById(UUID id) {
        var filtro = visibles();
        return repository.findOne(filtro.and((raiz, consulta, criterios) -> criterios.equal(raiz.get("id"), id)))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private Specification<ReporteSeguridad> visibles() {
        var actor = permisos.actorActual();
        UUID autorId = actor.roles().contains("SEGURIDAD_INDUSTRIAL") ? null : actor.usuarioId();
        return ReporteSeguridadRepository.visibles(actor.areas(), autorId);
    }

    @PreAuthorize("@permisosService.tieneCuentaActiva()")
    @Transactional
    public ReporteSeguridadResponseDto registrar(ReporteSeguridadRequestDto request, ImagenEvidenciaRequestDto imagen) {
        PermisosService.Actor actor = permisos.actorActual();
        if (!actor.areas().contains(request.areaId())) {
            throw new ForbiddenOperationException("El usuario no tiene asignada el área del reporte.");
        }
        Usuario autor = entidades.referencia(Usuario.class, actor.usuarioId());
        Area area = areas.findById(request.areaId()).orElseThrow(() -> new ResourceNotFoundException(request.areaId()));
        String tipoImagen = imagen == null ? null : validarImagen(imagen.contenido());
        Instant fecha = Instant.now().truncatedTo(ChronoUnit.MICROS);

        ReporteSeguridad reporte = new ReporteSeguridad();
        reporte.setArea(area);
        reporte.setUbicacion(request.ubicacion().strip());
        reporte.setDescripcion(request.descripcion().strip());
        reporte.setEstado("ABIERTO");
        reporte.setAutor(autor);
        reporte.setReportadoEn(fecha);
        repository.save(reporte);

        List<Adjunto> evidencias = request.evidencias().stream()
                .map(datos -> crearAdjunto(reporte, datos.nombre().strip(), datos.archivo().strip()))
                .toList();
        adjuntos.saveAll(evidencias);
        if (imagen != null) {
            String nombre = imagen.nombre() == null || imagen.nombre().isBlank() ? "Imagen" : imagen.nombre().strip();
            Adjunto adjunto = adjuntos.save(crearAdjunto(reporte, nombre, null));
            adjunto.setArchivo("/api/v1/reportes-seguridad/" + reporte.getId() + "/adjuntos/" + adjunto.getId() + "/imagen");
            imagenes.save(new ImagenAdjunto(adjunto, tipoImagen, imagen.contenido()));
        }
        adjuntos.flush();
        return toResponse(reporte);
    }

    @PreAuthorize("@permisosService.tieneCuentaActiva()")
    @Transactional(readOnly = true)
    public ImagenEvidenciaResponseDto consultarImagen(UUID reporteId, UUID adjuntoId) {
        var actor = permisos.actorActual();
        ReporteSeguridad reporte = repository.findById(reporteId)
                .orElseThrow(() -> new ResourceNotFoundException(reporteId));
        if (!actor.roles().contains("SEGURIDAD_INDUSTRIAL") && !actor.usuarioId().equals(entidades.id(reporte.getAutor()))) {
            throw new ForbiddenOperationException("El registro está fuera del alcance autorizado.");
        }
        AmbitoAreas.verificar(actor.areas(), () -> AmbitoAreas.presentes(entidades.id(reporte.getArea())));
        ImagenAdjunto imagen = imagenes.findByIdAndAdjunto_ReporteSeguridad_Id(adjuntoId, reporteId)
                .orElseThrow(() -> new ResourceNotFoundException(adjuntoId));
        return new ImagenEvidenciaResponseDto(imagen.getTipoContenido(), imagen.getContenido());
    }

    private Adjunto crearAdjunto(ReporteSeguridad reporte, String nombre, String archivo) {
        Adjunto adjunto = new Adjunto();
        adjunto.setReporteSeguridad(reporte);
        adjunto.setNombre(nombre);
        adjunto.setArchivo(archivo);
        adjunto.setAutor(reporte.getAutor());
        adjunto.setCreadoEn(reporte.getReportadoEn());
        return adjunto;
    }

    private String validarImagen(byte[] contenido) {
        if (contenido.length == 0 || contenido.length > ImagenEvidenciaRequestDto.MAX_BYTES) {
            throw new InvalidRequestException("La imagen debe tener contenido y pesar como máximo 5 MiB.");
        }
        try (var entrada = new MemoryCacheImageInputStream(new ByteArrayInputStream(contenido))) {
            Iterator<ImageReader> lectores = ImageIO.getImageReaders(entrada);
            if (!lectores.hasNext()) {
                throw new InvalidRequestException("La imagen debe ser un JPEG o PNG válido.");
            }
            ImageReader lector = lectores.next();
            try {
                String tipo = switch (lector.getFormatName().toLowerCase(Locale.ROOT)) {
                    case "png" -> "image/png";
                    case "jpeg" -> "image/jpeg";
                    default -> throw new InvalidRequestException("La imagen debe ser un JPEG o PNG válido.");
                };
                lector.setInput(entrada);
                if ((long) lector.getWidth(0) * lector.getHeight(0) > MAX_PIXELES) {
                    throw new InvalidRequestException("La imagen no puede superar 16 megapíxeles.");
                }
                lector.read(0);
                return tipo;
            } finally {
                lector.dispose();
            }
        } catch (IOException e) {
            throw new InvalidRequestException("La imagen debe ser un JPEG o PNG válido.");
        }
    }

    private ReporteSeguridadResponseDto toResponse(ReporteSeguridad entity) {
        return new ReporteSeguridadResponseDto(
            entity.getId(),
            entidades.id(entity.getArea()),
            entity.getUbicacion(),
            entity.getDescripcion(),
            entity.getEstado(),
            entidades.id(entity.getAutor()),
            entity.getReportadoEn()
        );
    }
}
