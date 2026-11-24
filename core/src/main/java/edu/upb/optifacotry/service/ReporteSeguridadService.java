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
import edu.upb.optifacotry.repository.repos.UsuarioRepository;
import edu.upb.optifacotry.service.exception.ForbiddenOperationException;
import edu.upb.optifacotry.service.exception.InvalidRequestException;
import edu.upb.optifacotry.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class ReporteSeguridadService extends ReadOnlyCrudService<ReporteSeguridad, ReporteSeguridadResponseDto> {

    private static final long MAX_PIXELES = 16_000_000L;

    private final UsuarioRepository usuarios;
    private final AreaRepository areas;
    private final AdjuntoRepository adjuntos;
    private final ImagenAdjuntoRepository imagenes;

    public ReporteSeguridadService(ReporteSeguridadRepository repository, UsuarioRepository usuarios,
                                   AreaRepository areas, AdjuntoRepository adjuntos, ImagenAdjuntoRepository imagenes) {
        super(repository);
        this.usuarios = usuarios;
        this.areas = areas;
        this.adjuntos = adjuntos;
        this.imagenes = imagenes;
    }

    @Transactional
    public ReporteSeguridadResponseDto registrar(String identidad, ReporteSeguridadRequestDto request,
                                                 ImagenEvidenciaRequestDto imagen) {
        Usuario autor = usuarioEnArea(identidad, request.areaId());
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

    public ImagenEvidenciaResponseDto consultarImagen(String identidad, UUID reporteId, UUID adjuntoId) {
        ReporteSeguridad reporte = findEntity(reporteId);
        usuarioEnArea(identidad, referenceId(reporte.getArea()));
        ImagenAdjunto imagen = imagenes.findByIdAndAdjunto_ReporteSeguridad_Id(adjuntoId, reporteId)
                .orElseThrow(() -> new ResourceNotFoundException(adjuntoId));
        return new ImagenEvidenciaResponseDto(imagen.getTipoContenido(), imagen.getContenido());
    }

    private Usuario usuarioEnArea(String identidad, UUID areaId) {
        List<Usuario> coincidencias = usuarios.findTop2ByReferenciaAutenticacion(identidad);
        if (coincidencias.size() != 1 || !Boolean.TRUE.equals(coincidencias.getFirst().getActivo())) {
            throw new ForbiddenOperationException("Se requiere un usuario activo asociado a la identidad autenticada.");
        }
        Usuario usuario = coincidencias.getFirst();
        if (areaId == null || usuario.getAreasIds() == null || !Arrays.asList(usuario.getAreasIds()).contains(areaId)) {
            throw new ForbiddenOperationException("El usuario no tiene asignada el área del reporte.");
        }
        return usuario;
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

    @Override
    protected ReporteSeguridadResponseDto toResponse(ReporteSeguridad entity) {
        return new ReporteSeguridadResponseDto(
            entity.getId(),
            referenceId(entity.getArea()),
            entity.getUbicacion(),
            entity.getDescripcion(),
            entity.getEstado(),
            referenceId(entity.getAutor()),
            entity.getReportadoEn()
        );
    }
}
