package edu.upb.optifacotry;

import edu.upb.optifacotry.repository.repos.AdjuntoRepository;
import edu.upb.optifacotry.repository.entity.MensajeRecibido;
import edu.upb.optifacotry.repository.repos.MaquinaRepository;
import edu.upb.optifacotry.repository.repos.MensajeRecibidoRepository;
import edu.upb.optifacotry.repository.repos.ReporteSeguridadRepository;
import edu.upb.optifacotry.repository.repos.RolRepository;
import edu.upb.optifacotry.repository.repos.UsuarioRepository;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
		"optifactory.security.bootstrap-admin=administrador",
		"spring.security.user.name=administrador",
		"spring.security.user.password=clave-de-prueba"
})
@AutoConfigureMockMvc
@Testcontainers
class OptifacotryApplicationTests {

	@Container
	@ServiceConnection
	static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

	@Autowired
	MockMvc mvc;

	@Autowired
	JsonMapper jsonMapper;

	@Autowired
	ReporteSeguridadRepository reportes;

	@Autowired
	AdjuntoRepository adjuntos;

	@Autowired
	RolRepository roles;

	@Autowired
	UsuarioRepository usuarios;

	@Autowired
	MaquinaRepository maquinas;

	@Autowired
	MensajeRecibidoRepository mensajes;

	@Autowired
	JdbcTemplate jdbc;

	private String areaPruebas;

	@BeforeEach
	void prepararCuentaOperativa() throws Exception {
		var cuentas = usuarios.findTop2ByReferenciaAutenticacion("operador");
		if (!cuentas.isEmpty()) {
			areaPruebas = cuentas.getFirst().getAreasIds()[0].toString();
			return;
		}
		areaPruebas = altaArea("Área de pruebas");
		String operador = altaUsuario("operador", areaPruebas, true);
		for (String codigo : new String[]{"OPERACION", "SUPERVISION_PRODUCCION", "PERSONAL", "CALIDAD",
				"AMBIENTE", "RESPONSABLE_MANTENIMIENTO", "INVENTARIO"}) {
			asignarRol(operador, codigo);
		}
	}

	@Test
	void contextLoads() {
	}

	@Test
	void preservesJsonbDecimalPrecision() throws Exception {
		String linea = alta("lineas", """
				{"nombre":"Mediciones","areaId":"%s"}
				""".formatted(areaPruebas)).path("id").asText();
		String maquina = alta("maquinas", """
				{"codigo":"MEDICION","lineaId":"%s"}
				""".formatted(linea)).path("id").asText();
		MensajeRecibido mensaje = new MensajeRecibido();
		mensaje.setMaquina(maquinas.findById(UUID.fromString(maquina)).orElseThrow());
		mensajes.saveAndFlush(mensaje);

		String decimal = "1.12345678901234567890123456789";
		String body = mvc.perform(post("/api/v1/mediciones-maquina").with(user("operador")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"valor\":" + decimal + ",\"mensajeId\":\"" + mensaje.getId() + "\"}"))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		String id = jsonMapper.readTree(body).path("id").asText();
		String saved = mvc.perform(get("/api/v1/mediciones-maquina/{id}", id).with(user("operador")))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		assertThat(jsonMapper.readTree(saved).path("valor").decimalValue()).isEqualByComparingTo(decimal);
	}

	@Test
	void registraPersonaYRechazaIdentificacionDuplicada() throws Exception {
		String usuario = altaUsuario("cuenta-persona", areaPruebas, true);
		String datos = """
				{"identificacion":" CI-123 ","nombre":"Ana","activo":true,"usuarioId":"%s"}
				""".formatted(usuario);
		JsonNode persona = alta("personas", datos);
		String id = persona.path("id").asText();
		assertThat(persona.path("identificacion").asText()).isEqualTo("CI-123");
		assertThat(persona.path("usuarioId").asText()).isEqualTo(usuario);
		enviarAlta("personas", datos.replace(" CI-123 ", "CI-123"), user("operador"))
				.andExpect(status().isConflict());
		enviarAlta("personas", """
				{"identificacion":" ","nombre":"Ana","activo":true}
				""", user("operador")).andExpect(status().isBadRequest());

		mvc.perform(put("/api/v1/personas/{id}", id).with(user("operador")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON).content("""
								{"identificacion":"CI-123","nombre":"Ana","activo":false,"usuarioId":"%s"}
								""".formatted(usuario)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.activo").value(false))
				.andExpect(jsonPath("$.usuarioId").value(usuario));
		mvc.perform(get("/api/v1/personas/{id}", id).with(user("operador")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.activo").value(false));
	}

	@Test
	void registraTurnoNocturnoYConsultaPeriodosQueSeSolapan() throws Exception {
		String persona = altaPersona("CI-TURNOS", "Rosa", true);
		String turno = alta("turnos", """
				{"nombre":"Noche","inicio":"22:00:00","fin":"06:00:00"}
				""").path("id").asText();
		alta("periodos-personal", """
				{"personaId":"%s","turnoId":"%s","tipo":"TURNO",
				"inicio":"2026-11-24T22:00:00-04:00","fin":"2026-11-25T06:00:00-04:00"}
				""".formatted(persona, turno));
		alta("periodos-personal", """
				{"personaId":"%s","tipo":"AUSENCIA","motivo":"Permiso",
				"inicio":"2026-11-25T04:00:00Z","fin":"2026-11-25T05:00:00Z"}
				""".formatted(persona));
		alta("periodos-personal", """
				{"personaId":"%s","tipo":"DISPONIBILIDAD",
				"inicio":"2026-11-25T06:00:00Z","fin":"2026-11-25T07:00:00Z"}
				""".formatted(persona));

		mvc.perform(get("/api/v1/periodos-personal/consulta").with(user("operador"))
						.param("personaId", persona)
						.param("desde", "2026-11-25T04:00:00Z")
						.param("hasta", "2026-11-25T06:00:00Z"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(2))
				.andExpect(jsonPath("$.content[0].turnoId").value(turno))
				.andExpect(jsonPath("$.content[1].tipo").value("AUSENCIA"));
		mvc.perform(get("/api/v1/periodos-personal/consulta").with(user("operador"))
						.param("personaId", persona)
						.param("desde", "2026-11-25T04:00:00Z")
						.param("hasta", "2026-11-25T06:00:00.000000100Z"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(3));
	}

	@Test
	void rechazaTurnosSinDuracionYPeriodosInvalidos() throws Exception {
		enviarAlta("turnos", """
				{"nombre":"Inválido","inicio":"06:00:00","fin":"06:00:00"}
				""", user("operador")).andExpect(status().isBadRequest());
		enviarAlta("turnos", """
				{"nombre":"Inválido","inicio":"06:00:00","fin":"06:00:00.000000100"}
				""", user("operador")).andExpect(status().isBadRequest());
		String persona = altaPersona("CI-PERIODO", "Luis", true);
		String periodo = """
				{"personaId":"%s","tipo":"%s","inicio":"2026-11-24T12:00:00Z","fin":"%s"}
				""";
		enviarAlta("periodos-personal", periodo.formatted(persona, "AUSENCIA", "2026-11-24T11:00:00Z"),
				user("operador")).andExpect(status().isBadRequest());
		enviarAlta("periodos-personal", periodo.formatted(persona, "AUSENCIA", "2026-11-24T12:00:00.000000100Z"),
				user("operador")).andExpect(status().isBadRequest());
		enviarAlta("periodos-personal", periodo.formatted(persona, "TURNO", "2026-11-24T13:00:00Z"),
				user("operador")).andExpect(status().isBadRequest());
		enviarAlta("periodos-personal", periodo.formatted(persona, "OTRO", "2026-11-24T13:00:00Z"),
				user("operador")).andExpect(status().isBadRequest());
		mvc.perform(get("/api/v1/periodos-personal/consulta").with(user("operador"))
						.param("personaId", persona)
						.param("desde", "2026-11-24T12:00:00Z")
						.param("hasta", "2026-11-24T12:00:00Z"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void conservaCapacitacionesVencidasYValidaFechas() throws Exception {
		String persona = altaPersona("CI-CAP", "Isa", false);
		String capacitacion = alta("capacitaciones", """
				{"codigo":"SEG","nombre":"Seguridad","activo":true}
				""").path("id").asText();
		String datos = """
				{"personaId":"%s","capacitacionId":"%s","obtenidaEn":"2020-01-01","venceEn":%s}
				""";
		JsonNode vencida = alta("persona-capacitaciones", datos.formatted(persona, capacitacion, "\"2021-01-01\""));
		assertThat(vencida.path("venceEn").asText()).isEqualTo("2021-01-01");
		JsonNode permanente = alta("persona-capacitaciones", datos.formatted(persona, capacitacion, "null"));
		assertThat(permanente.path("venceEn").isNull()).isTrue();
		enviarAlta("persona-capacitaciones", datos.formatted(persona, capacitacion, "\"2019-12-31\""),
				user("operador")).andExpect(status().isBadRequest());
		enviarAlta("persona-capacitaciones", datos.formatted(persona, UUID.randomUUID(), "null"),
				user("operador")).andExpect(status().isConflict());
	}

	@Test
	void validaCriterioYAlcanceDelControlDeCalidad() throws Exception {
		String version = alta("versiones-producto", """
				{"version":1}
				""").path("id").asText();
		String otraVersion = alta("versiones-producto", """
				{"version":2}
				""").path("id").asText();
		String operacion = alta("operaciones-producto", """
				{"versionId":"%s","nombre":"Corte","secuencia":1}
				""".formatted(version)).path("id").asText();
		String datos = """
				{"versionId":"%s","operacionId":"%s","nombre":"Espesor","unidad":"mm",
				"criterio":{"minimo":1.125,"maximo":1.375},"obligatorio":true}
				""";
		JsonNode control = alta("controles-calidad", datos.formatted(version, operacion));
		assertThat(control.path("obligatorio").asBoolean()).isTrue();
		assertThat(control.path("criterio").path("minimo").decimalValue()).isEqualByComparingTo("1.125");
		enviarAlta("controles-calidad", datos.formatted(otraVersion, operacion), user("operador"))
				.andExpect(status().isBadRequest());
		enviarAlta("controles-calidad", """
				{"versionId":"%s","nombre":"Espesor","unidad":"mm","criterio":{},"obligatorio":true}
				""".formatted(version), user("operador")).andExpect(status().isBadRequest());
		enviarAlta("controles-calidad", """
				{"nombre":"Espesor","unidad":"mm","criterio":{"minimo":1},"obligatorio":true}
				""", user("operador")).andExpect(status().isBadRequest());
	}

	@Test
	void consultaUnidadesCompatiblesYExigeUnidadEnLosCatalogos() throws Exception {
		String agua = alta("recursos-ambientales", """
				{"codigo":"AGUA","nombre":"Agua","unidad":"m3","activo":true}
				""").path("id").asText();
		mvc.perform(get("/api/v1/recursos-ambientales/{id}/unidades", agua).with(user("operador")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.unidad").value("m3"))
				.andExpect(jsonPath("$.factores.L").value(0.001))
				.andExpect(jsonPath("$.factores.kg").doesNotExist());
		String paquetes = alta("recursos-ambientales", """
				{"codigo":"PAQUETES","nombre":"Paquetes","unidad":"paquete","activo":true}
				""").path("id").asText();
		mvc.perform(get("/api/v1/recursos-ambientales/{id}/unidades", paquetes).with(user("operador")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.factores").isMap())
				.andExpect(jsonPath("$.factores.paquete").value(1));
		JsonNode residuo = alta("tipos-residuo", """
				{"codigo":"METAL","nombre":"Metal","unidad":"kg","activo":true}
				""");
		assertThat(residuo.path("unidad").asText()).isEqualTo("kg");
		for (String recurso : new String[]{"recursos-ambientales", "tipos-residuo"}) {
			enviarAlta(recurso, """
					{"codigo":"SIN-UNIDAD","nombre":"Sin unidad","unidad":" ","activo":true}
					""", user("operador")).andExpect(status().isBadRequest());
		}
	}

	@Test
	void registraReportesConYSinEvidenciaConAutorYFechaDelServidor() throws Exception {
		String area = altaArea("Calderas");
		String autor = altaUsuario("reportante", area, true);
		String otraCuenta = altaUsuario("otra-cuenta", area, true);
		Instant antes = Instant.now().minusSeconds(1);
		JsonNode reporte = deserializar(enviarAlta("reportes-seguridad", """
				{"areaId":"%s","ubicacion":"Válvula 3","descripcion":"Hay una fuga","autorId":"%s",
				"reportadoEn":"2000-01-01T00:00:00Z","estado":"CERRADO",
				"evidencias":[{"nombre":"Foto","archivo":"evidencias/fuga.jpg","autorId":"%s"}]}
				""".formatted(area, otraCuenta, otraCuenta), user("reportante"))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location")));
		String id = reporte.path("id").asText();
		String fecha = reporte.path("reportadoEn").asText();
		assertThat(reporte.path("autorId").asText()).isEqualTo(autor);
		assertThat(reporte.path("estado").asText()).isEqualTo("ABIERTO");
		assertThat(Instant.parse(fecha)).isBetween(antes, Instant.now());

		mvc.perform(get("/api/v1/reportes-seguridad/{id}/adjuntos", id).with(user("reportante")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(1))
				.andExpect(jsonPath("$.content[0].archivo").value("evidencias/fuga.jpg"))
				.andExpect(jsonPath("$.content[0].reporteSeguridadId").value(id))
				.andExpect(jsonPath("$.content[0].autorId").value(autor))
				.andExpect(jsonPath("$.content[0].creadoEn").value(fecha));

		JsonNode sinEvidencia = deserializar(enviarAlta("reportes-seguridad", reporteEn(area), user("reportante"))
				.andExpect(status().isCreated()));
		mvc.perform(get("/api/v1/reportes-seguridad/{id}/adjuntos", sinEvidencia.path("id").asText())
						.with(user("reportante")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(0));
	}

	@Test
	void guardaLaImagenMultipartYLaSirveSoloParaElReporteYAreaCorrespondientes() throws Exception {
		String area = altaArea("Soldadura");
		String areaAjena = altaArea("Pintura");
		String autor = altaUsuario("reportante-imagen", area, true);
		altaUsuario("lector-area-ajena", areaAjena, true);
		RequestPostProcessor reportante = user("reportante-imagen");

		for (String formato : new String[]{"png", "jpeg"}) {
			byte[] bytes = datosImagen(formato);
			JsonNode reporte = deserializar(mvc.perform(multipart("/api/v1/reportes-seguridad")
							.file(parteReporte(area))
							.file(new MockMultipartFile("imagen", "peligro." + formato, "application/octet-stream", bytes))
							.with(reportante).with(csrf()))
					.andExpect(status().isCreated()));
			String id = reporte.path("id").asText();
			JsonNode evidencias = deserializar(mvc.perform(get("/api/v1/reportes-seguridad/{id}/adjuntos", id)
							.with(reportante))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.content.length()").value(1))
					.andExpect(jsonPath("$.content[0].autorId").value(autor))
					.andExpect(jsonPath("$.content[0].reporteSeguridadId").value(id)));
			JsonNode evidencia = evidencias.path("content").get(0);
			String ruta = evidencia.path("archivo").asText();
			assertThat(ruta).isEqualTo("/api/v1/reportes-seguridad/" + id + "/adjuntos/"
					+ evidencia.path("id").asText() + "/imagen");

			mvc.perform(get(ruta).with(reportante))
					.andExpect(status().isOk())
					.andExpect(content().contentType("image/" + formato))
					.andExpect(content().bytes(bytes));
			mvc.perform(get(ruta).with(user("lector-area-ajena"))).andExpect(status().isForbidden());

			JsonNode otroReporte = deserializar(enviarAlta("reportes-seguridad", reporteEn(area), reportante)
					.andExpect(status().isCreated()));
			mvc.perform(get("/api/v1/reportes-seguridad/{reporte}/adjuntos/{adjunto}/imagen",
							otroReporte.path("id").asText(), evidencia.path("id").asText()).with(reportante))
					.andExpect(status().isNotFound());
		}
	}

	@Test
	void rechazaImagenesInvalidasOGrandesSinCrearElReporte() throws Exception {
		String area = altaArea("Fundición");
		altaUsuario("reportante-archivo", area, true);
		RequestPostProcessor reportante = user("reportante-archivo");
		long totalReportes = reportes.count();
		long totalAdjuntos = adjuntos.count();
		mvc.perform(multipart("/api/v1/reportes-seguridad").file(parteReporte(area))
						.file(new MockMultipartFile("imagen", "falso.png", "image/png", new byte[]{1, 2, 3}))
						.with(reportante).with(csrf()))
				.andExpect(status().isBadRequest());
		mvc.perform(multipart("/api/v1/reportes-seguridad").file(parteReporte(area))
						.file(new MockMultipartFile("imagen", "vacio.png", "image/png", new byte[0]))
						.with(reportante).with(csrf()))
				.andExpect(status().isBadRequest());
		mvc.perform(multipart("/api/v1/reportes-seguridad").file(parteReporte(area))
						.file(new MockMultipartFile("imagen", "grande.png", "image/png", new byte[5 * 1024 * 1024 + 1]))
						.with(reportante).with(csrf()))
				.andExpect(status().is(413));
		assertThat(reportes.count()).isEqualTo(totalReportes);
		assertThat(adjuntos.count()).isEqualTo(totalAdjuntos);
	}

	@Test
	void revierteReporteYEvidenciasSiUnaEvidenciaFalla() throws Exception {
		String area = altaArea("Recepción");
		altaUsuario("reportante-transaccion", area, true);
		long totalReportes = reportes.count();
		long totalAdjuntos = adjuntos.count();
		enviarAlta("reportes-seguridad", """
				{"areaId":"%s","ubicacion":"Pasillo","descripcion":"Derrame",
				"evidencias":[{"nombre":"Foto","archivo":"foto.jpg"},
				              {"nombre":"Inválido","archivo":"archivo\\u0000.jpg"}]}
				""".formatted(area), user("reportante-transaccion"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("Uno de los valores no cumple las restricciones de la base de datos."));
		assertThat(reportes.count()).isEqualTo(totalReportes);
		assertThat(adjuntos.count()).isEqualTo(totalAdjuntos);
	}

	@Test
	void rechazaReportesFueraDelAreaOConIdentidadInvalida() throws Exception {
		String area = altaArea("Producción");
		String otraArea = altaArea("Almacén");
		altaUsuario("reportante-inactivo", area, false);
		altaUsuario("reportante-otra-area", otraArea, true);
		altaUsuario("reportante-ambiguo", area, true);
		altaUsuario("reportante-ambiguo", area, true);
		String datos = reporteEn(area);
		mvc.perform(post("/api/v1/reportes-seguridad").with(csrf()).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(datos))
				.andExpect(status().isUnauthorized());
		for (String identidad : new String[]{"sin-vinculo", "reportante-inactivo", "reportante-otra-area", "reportante-ambiguo"}) {
			enviarAlta("reportes-seguridad", datos, user(identidad)).andExpect(status().isForbidden());
		}
	}

	@Test
	void asignaAccionesDeSeguridadConResponsableActivoDelArea() throws Exception {
		String area = altaArea("Área para seguimiento");
		String responsable = altaUsuario("responsable-accion", area, true);
		String jefe = altaUsuario("jefe-seguridad", area, true);
		asignarRol(jefe, "SEGURIDAD_INDUSTRIAL");
		RequestPostProcessor seguridad = user("jefe-seguridad");
		RequestPostProcessor reportante = user("responsable-accion");
		String reporte = deserializar(enviarAlta("reportes-seguridad", reporteEn(area), reportante)
				.andExpect(status().isCreated())).path("id").asText();
		String datos = """
				{"reporteId":"%s","responsableId":"%s","accion":" Revisar el resguardo ",
				"fechaObjetivo":"2026-12-04","estado":"CERRADA"}
				""".formatted(reporte, responsable);
		enviarAlta("acciones-seguridad", datos, reportante).andExpect(status().isForbidden());
		JsonNode accion = deserializar(enviarAlta("acciones-seguridad", datos, seguridad)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.estado").value("PENDIENTE"))
				.andExpect(jsonPath("$.responsableId").value(responsable))
				.andExpect(jsonPath("$.reporteId").value(reporte))
				.andExpect(jsonPath("$.fechaObjetivo").value("2026-12-04")));
		String id = accion.path("id").asText();
		mvc.perform(get("/api/v1/acciones-seguridad/{id}", id).with(seguridad))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accion").value("Revisar el resguardo"));
		mvc.perform(put("/api/v1/acciones-seguridad/{id}", id).with(seguridad).with(csrf())
						.contentType(MediaType.APPLICATION_JSON).content(datos))
				.andExpect(status().isForbidden());
	}

	@Test
	void rechazaAccionesConReporteOResponsableInvalido() throws Exception {
		String area = altaArea("Área de incidentes");
		String otraArea = altaArea("Área distinta del incidente");
		String jefe = altaUsuario("seguridad-incidentes", area, true);
		String jefeAjeno = altaUsuario("seguridad-externa", otraArea, true);
		String inactivo = altaUsuario("responsable-inactivo", area, false);
		asignarRol(jefe, "SEGURIDAD_INDUSTRIAL");
		asignarRol(jefeAjeno, "SEGURIDAD_INDUSTRIAL");
		RequestPostProcessor seguridad = user("seguridad-incidentes");
		String reporte = deserializar(enviarAlta("reportes-seguridad", reporteEn(area), seguridad)
				.andExpect(status().isCreated())).path("id").asText();
		String datos = """
				{"reporteId":"%s","responsableId":"%s","accion":"Revisar"}
				""";
		enviarAlta("acciones-seguridad", datos.formatted(reporte, jefe), user("seguridad-externa"))
				.andExpect(status().isForbidden());
		enviarAlta("acciones-seguridad", datos.formatted(reporte, jefeAjeno), seguridad)
				.andExpect(status().isBadRequest());
		enviarAlta("acciones-seguridad", datos.formatted(reporte, inactivo), seguridad)
				.andExpect(status().isBadRequest());
		enviarAlta("acciones-seguridad", datos.formatted(reporte, UUID.randomUUID()), seguridad)
				.andExpect(status().isNotFound());
		enviarAlta("acciones-seguridad", datos.formatted(UUID.randomUUID(), jefe), seguridad)
				.andExpect(status().isNotFound());
	}

	@Test
	void guardaConsumoMedidoYEstimadoEnSuUnidadOriginal() throws Exception {
		String recurso = alta("recursos-ambientales", """
				{"codigo":"AGUA-CONSUMO","nombre":"Agua de proceso","unidad":"m3","activo":true}
				""").path("id").asText();
		String actor = usuarios.findTop2ByReferenciaAutenticacion("operador").getFirst().getId().toString();
		String datos = agregarCampo(consumoEn(recurso, areaPruebas), "responsableId", UUID.randomUUID().toString());
		JsonNode medido = alta("registros-consumo", datos);
		assertThat(medido.path("tipoDato").asText()).isEqualTo("MEDIDO");
		assertThat(medido.path("unidad").asText()).isEqualTo("L");
		assertThat(medido.path("cantidad").decimalValue()).isEqualByComparingTo("125.25");
		assertThat(medido.path("responsableId").asText()).isEqualTo(actor);

		JsonNode estimado = alta("registros-consumo", datos.replace("125.25", "0")
				.replace("medido", "estimado").replace("\"L\"", "\"m3\""));
		assertThat(estimado.path("tipoDato").asText()).isEqualTo("ESTIMADO");
		assertThat(estimado.path("cantidad").decimalValue()).isEqualByComparingTo("0");
		mvc.perform(get("/api/v1/registros-consumo/{id}", medido.path("id").asText()).with(user("operador")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.fuente").value("Medidor línea A"))
				.andExpect(jsonPath("$.periodoInicio").value("2026-11-25T12:00:00Z"));
	}

	@Test
	void validaUnidadTipoPeriodoYPermisosDelConsumoAlCrearYEditar() throws Exception {
		String recurso = alta("recursos-ambientales", """
				{"codigo":"AGUA-VALIDACION","nombre":"Agua","unidad":"m3","activo":true}
				""").path("id").asText();
		String inactivo = alta("recursos-ambientales", """
				{"codigo":"AGUA-INACTIVA","nombre":"Agua inactiva","unidad":"m3","activo":false}
				""").path("id").asText();
		String otraArea = altaArea("Área ajena para consumo");
		altaUsuario("sin-rol-ambiental", areaPruebas, true);
		RequestPostProcessor operador = user("operador");
		String datos = consumoEn(recurso, areaPruebas);
		String id = alta("registros-consumo", datos).path("id").asText();
		enviarAlta("registros-consumo", consumoEn(recurso, otraArea), operador).andExpect(status().isForbidden());
		enviarAlta("registros-consumo", datos, user("sin-rol-ambiental")).andExpect(status().isForbidden());
		enviarAlta("registros-consumo", consumoEn(inactivo, areaPruebas), operador).andExpect(status().isBadRequest());
		enviarAlta("registros-consumo", datos.replace("medido", "calculado"), operador)
				.andExpect(status().isBadRequest());
		enviarAlta("registros-consumo", datos.replace("125.25", "-1"), operador)
				.andExpect(status().isBadRequest());
		enviarAlta("registros-consumo", datos.replace("T13:00", "T11:00"), operador)
				.andExpect(status().isBadRequest());
		mvc.perform(put("/api/v1/registros-consumo/{id}", id).with(operador).with(csrf())
						.contentType(MediaType.APPLICATION_JSON).content(datos.replace("\"L\"", "\"kg\"")))
				.andExpect(status().isBadRequest());
		mvc.perform(get("/api/v1/registros-consumo/{id}", id).with(operador))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.unidad").value("L"));
	}

	@Test
	void guardaGeneracionDeResiduoYConservaSuDestinoAlCorregirElRegistro() throws Exception {
		String tipo = alta("tipos-residuo", """
				{"codigo":"ALUMINIO","nombre":"Aluminio","unidad":"kg","activo":true}
				""").path("id").asText();
		String actor = usuarios.findTop2ByReferenciaAutenticacion("operador").getFirst().getId().toString();
		String datos = agregarCampo(residuoEn(tipo, areaPruebas), "destinoFinal", "Inventado");
		JsonNode registro = alta("registros-residuo", datos);
		String id = registro.path("id").asText();
		assertThat(registro.path("areaId").asText()).isEqualTo(areaPruebas);
		assertThat(registro.path("tipoId").asText()).isEqualTo(tipo);
		assertThat(registro.path("cantidad").decimalValue()).isEqualByComparingTo("12.5");
		assertThat(registro.path("generadoEn").asText()).isEqualTo("2026-11-25T12:00:00Z");
		assertThat(registro.path("responsableId").asText()).isEqualTo(actor);
		assertThat(registro.path("destinoFinal").isNull()).isTrue();
		jdbc.update("UPDATE registros_residuo SET destino_final = 'Reciclaje' WHERE id = ?", UUID.fromString(id));
		mvc.perform(put("/api/v1/registros-residuo/{id}", id).with(user("operador")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON).content(datos))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.destinoFinal").value("Reciclaje"));
	}

	@Test
	void rechazaGeneracionDeResiduoSinCantidadFechaOUnidadValida() throws Exception {
		String tipo = alta("tipos-residuo", """
				{"codigo":"CARTON","nombre":"Cartón","unidad":"kg","activo":true}
				""").path("id").asText();
		String otraArea = altaArea("Área ajena para residuos");
		RequestPostProcessor operador = user("operador");
		String datos = residuoEn(tipo, areaPruebas);
		enviarAlta("registros-residuo", datos.replace("\"kg\"", "\"g\""), operador)
				.andExpect(status().isBadRequest());
		enviarAlta("registros-residuo", datos.replace("12.5", "0"), operador)
				.andExpect(status().isBadRequest());
		enviarAlta("registros-residuo", datos.replace("\"2026-11-25T12:00:00Z\"", "null"), operador)
				.andExpect(status().isBadRequest());
		enviarAlta("registros-residuo", residuoEn(tipo, otraArea), operador)
				.andExpect(status().isForbidden());
	}

	private static String consumoEn(String recurso, String area) {
		return """
				{"recursoId":"%s","areaId":"%s","cantidad":125.25,"unidad":"L","tipoDato":"medido",
				"periodoInicio":"2026-11-25T12:00:00Z","periodoFin":"2026-11-25T13:00:00Z","fuente":" Medidor línea A "}
				""".formatted(recurso, area);
	}

	private static String residuoEn(String tipo, String area) {
		return """
				{"tipoId":"%s","areaId":"%s","cantidad":12.5,"unidad":"kg","generadoEn":"2026-11-25T12:00:00Z"}
				""".formatted(tipo, area);
	}

	private static String agregarCampo(String json, String nombre, String valor) {
		String objeto = json.strip();
		return objeto.substring(0, objeto.length() - 1) + ",\"" + nombre + "\":\"" + valor + "\"}";
	}

	@Test
	void protegeViasAlternativasDeEscritura() throws Exception {
		String area = altaArea("Despacho");
		String usuario = altaUsuario("reportante-crud", area, true);
		RequestPostProcessor reportante = user("reportante-crud");
		String id = deserializar(enviarAlta("reportes-seguridad", reporteEn(area), reportante)
				.andExpect(status().isCreated())).path("id").asText();

		enviarAlta("usuarios", "{}", reportante).andExpect(status().isForbidden());
		mvc.perform(put("/api/v1/usuarios/{id}", usuario).with(reportante).with(csrf())
						.contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isForbidden());
		mvc.perform(delete("/api/v1/usuarios/{id}", usuario).with(reportante).with(csrf()))
				.andExpect(status().isForbidden());
		mvc.perform(put("/api/v1/reportes-seguridad/{id}", id).with(reportante).with(csrf())
						.contentType(MediaType.APPLICATION_JSON).content(reporteEn(area)))
				.andExpect(status().isMethodNotAllowed());
		mvc.perform(delete("/api/v1/reportes-seguridad/{id}", id).with(reportante).with(csrf()))
				.andExpect(status().isMethodNotAllowed());
		enviarAlta("adjuntos", "{}", reportante).andExpect(status().isMethodNotAllowed());
		mvc.perform(put("/api/v1/adjuntos/{id}", id).with(reportante).with(csrf())
						.contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isMethodNotAllowed());
		mvc.perform(delete("/api/v1/adjuntos/{id}", id).with(reportante).with(csrf()))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	void soloUnAdministradorPersistidoAsignaRetiraYGeneraAuditoria() throws Exception {
		String destinatario = altaUsuario("destinatario-rol", areaPruebas, true);
		String actor = usuarios.findTop2ByReferenciaAutenticacion("administrador").getFirst().getId().toString();
		String comando = comandoRol(destinatario, "OPERACION");
		enviarAlta("usuario-roles", comando, user("destinatario-rol").roles("ADMIN"))
				.andExpect(status().isForbidden());
		mvc.perform(post("/api/v1/usuario-roles").with(csrf()).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(comando))
				.andExpect(status().isUnauthorized());

		JsonNode asignacion = deserializar(enviarAlta("usuario-roles",
				comando.strip().replace("}", ",\"asignadoPor\":\"" + destinatario
						+ "\",\"asignadoEn\":\"2000-01-01T00:00:00Z\"}"),
				httpBasic("administrador", "clave-de-prueba"))
				.andExpect(status().isCreated()));
		String id = asignacion.path("id").asText();
		assertThat(asignacion.path("asignadoPor").asText()).isEqualTo(actor);
		assertThat(Instant.parse(asignacion.path("asignadoEn").asText())).isAfter(Instant.parse("2000-01-01T00:00:00Z"));
		enviarAlta("usuario-roles", comando, user("administrador")).andExpect(status().isConflict());
		mvc.perform(get("/api/v1/turnos").with(user("destinatario-rol"))).andExpect(status().isOk());

		RequestPostProcessor admin = user("administrador");
		JsonNode retiro = deserializar(mvc.perform(post("/api/v1/usuario-roles/{id}/retiro", id).with(admin).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.retiradoPor").value(actor)));
		mvc.perform(post("/api/v1/usuario-roles/{id}/retiro", id).with(admin).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.retiradoEn").value(retiro.path("retiradoEn").asText()));
		mvc.perform(get("/api/v1/turnos").with(user("destinatario-rol"))).andExpect(status().isForbidden());

		String auditor = altaUsuario("auditor-roles", areaPruebas, true);
		asignarRol(auditor, "AUDITORIA");
		JsonNode historial = deserializar(mvc.perform(get("/api/v1/auditoria-cambios")
						.param("size", "100").with(user("auditor-roles")))
				.andExpect(status().isOk()));
		var cambios = StreamSupport.stream(historial.path("content").spliterator(), false)
				.filter(nodo -> id.equals(nodo.path("registroId").asText())).toList();
		assertThat(cambios).extracting(nodo -> nodo.path("accion").asText())
				.containsExactlyInAnyOrder("ASIGNAR_ROL", "RETIRAR_ROL");
		assertThat(cambios).allSatisfy(cambio -> {
			assertThat(cambio.path("usuarioId").asText()).isEqualTo(actor);
			assertThat(cambio.path("valoresNuevos").path("usuarioAfectadoId").asText()).isEqualTo(destinatario);
			assertThat(cambio.path("valoresNuevos").path("rolCodigo").asText()).isEqualTo("OPERACION");
			assertThat(cambio.path("ocurridoEn").asText()).isNotBlank();
		});
		mvc.perform(get("/api/v1/usuario-roles").with(user("auditor-roles"))).andExpect(status().isForbidden());
		enviarAlta("roles", "{}", admin).andExpect(status().isMethodNotAllowed());
		enviarAlta("auditoria-cambios", "{}", admin).andExpect(status().isMethodNotAllowed());
		mvc.perform(put("/api/v1/usuario-roles/{id}", id).with(admin).with(csrf())
						.contentType(MediaType.APPLICATION_JSON).content(comando))
				.andExpect(status().isMethodNotAllowed());
		mvc.perform(delete("/api/v1/usuario-roles/{id}", id).with(admin).with(csrf()))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	void dosAsignacionesConcurrentesDejanUnaSolaVigente() throws Exception {
		String destinatario = altaUsuario("concurrencia", areaPruebas, true);
		String comando = comandoRol(destinatario, "AMBIENTE");
		CyclicBarrier inicio = new CyclicBarrier(2);
		try (var ejecutor = Executors.newFixedThreadPool(2)) {
			java.util.concurrent.Callable<Integer> asignar = () -> {
				inicio.await(10, TimeUnit.SECONDS);
				return enviarAlta("usuario-roles", comando, user("administrador"))
						.andReturn().getResponse().getStatus();
			};
			var primera = ejecutor.submit(asignar);
			var segunda = ejecutor.submit(asignar);
			assertThat(new int[]{primera.get(15, TimeUnit.SECONDS), segunda.get(15, TimeUnit.SECONDS)})
					.containsExactlyInAnyOrder(201, 409);
		}
	}

	@Test
	void revierteLaAsignacionCuandoFallaSuAuditoria() throws Exception {
		String destinatario = altaUsuario("rollback-auditoria", areaPruebas, true);
		jdbc.execute("""
				ALTER TABLE auditoria_cambios ADD CONSTRAINT prueba_rechazar_auditoria
				CHECK ((valores_nuevos ->> 'usuarioAfectadoId') IS DISTINCT FROM '%s') NOT VALID
				""".formatted(destinatario));
		try {
			enviarAlta("usuario-roles", comandoRol(destinatario, "OPERACION"), user("administrador"))
					.andExpect(status().isBadRequest());
			mvc.perform(get("/api/v1/turnos").with(user("rollback-auditoria")))
					.andExpect(status().isForbidden());
			assertThat(jdbc.queryForObject("SELECT count(*) FROM usuario_roles WHERE usuario_id = ?", Long.class,
					UUID.fromString(destinatario))).isZero();
		} finally {
			jdbc.execute("ALTER TABLE auditoria_cambios DROP CONSTRAINT prueba_rechazar_auditoria");
		}
	}

	@Test
	void migracionConservaAsignacionesAnterioresSinConcederPermisos() {
		String esquema = "roles_legacy_test";
		var migraciones = Flyway.configure()
				.dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
				.schemas(esquema).defaultSchema(esquema)
				.locations("classpath:db/migration");
		migraciones.target(MigrationVersion.fromVersion("2")).load().migrate();
		UUID usuario = UUID.randomUUID();
		UUID rol = UUID.randomUUID();
		jdbc.update("INSERT INTO roles_legacy_test.usuarios (id, activo) VALUES (?, true)", usuario);
		jdbc.update("INSERT INTO roles_legacy_test.roles (id, codigo) VALUES (?, 'ADMIN')", rol);
		for (int i = 0; i < 2; i++) {
			jdbc.update("""
					INSERT INTO roles_legacy_test.usuario_roles (id, usuario_id, rol_id, asignado_en)
					VALUES (?, ?, ?, current_timestamp)
					""", UUID.randomUUID(), usuario, rol);
		}

		migraciones.target(MigrationVersion.LATEST).load().migrate();
		assertThat(jdbc.queryForObject("SELECT count(*) FROM roles_legacy_test.usuario_roles", Long.class)).isEqualTo(2);
		assertThat(jdbc.queryForObject("""
				SELECT count(*) FROM roles_legacy_test.usuario_roles WHERE retirado_en IS NULL
				""", Long.class)).isZero();
	}

	@Test
	void aplicaRolYAreaATotalesIdentificadoresYEscrituras() throws Exception {
		String otraArea = altaArea("Área de órdenes ajena");
		String supervisor = altaUsuario("supervisor-otra-area", otraArea, true);
		asignarRol(supervisor, "SUPERVISION_PRODUCCION");
		asignarRol(supervisor, "RESPONSABLE_MANTENIMIENTO");
		String operador = altaUsuario("solo-operacion", areaPruebas, true);
		asignarRol(operador, "OPERACION");
		String lineaPropia = alta("lineas", """
				{"nombre":"Línea propia","areaId":"%s"}
				""".formatted(areaPruebas)).path("id").asText();
		RequestPostProcessor supervisorAjeno = user("supervisor-otra-area");
		String lineaAjena = deserializar(enviarAlta("lineas", """
				{"nombre":"Línea ajena","areaId":"%s"}
				""".formatted(otraArea), supervisorAjeno).andExpect(status().isCreated())).path("id").asText();
		String propia = alta("ordenes-produccion", datosOrden("PROPIA", lineaPropia)).path("id").asText();
		String ajena = deserializar(enviarAlta("ordenes-produccion", datosOrden("AJENA", lineaAjena), supervisorAjeno)
				.andExpect(status().isCreated())).path("id").asText();
		mvc.perform(get("/api/v1/ordenes-produccion").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
		mvc.perform(get("/api/v1/ordenes-produccion").with(user("solo-operacion")).param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.page.totalElements").value(1))
				.andExpect(jsonPath("$.content[0].id").value(propia));
		mvc.perform(get("/api/v1/ordenes-produccion/{id}", ajena).with(user("solo-operacion")))
				.andExpect(status().isNotFound());
		enviarAlta("ordenes-produccion", datosOrden("SIN-ROL", lineaPropia), user("solo-operacion"))
				.andExpect(status().isForbidden());
		enviarAlta("ordenes-produccion", datosOrden("SIN-AREA", lineaAjena), user("operador"))
				.andExpect(status().isForbidden());
		enviarAlta("ordenes-produccion", datosOrden("SIN-LINEA-VALIDA", UUID.randomUUID().toString()), user("operador"))
				.andExpect(status().isConflict());
		mvc.perform(put("/api/v1/ordenes-produccion/{id}", ajena).with(user("operador")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON).content(datosOrden("AJENA", lineaPropia)))
				.andExpect(status().isForbidden());
		mvc.perform(put("/api/v1/ordenes-produccion/{id}", propia).with(user("operador")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON).content(datosOrden("PROPIA", lineaAjena)))
				.andExpect(status().isForbidden());

		String avance = deserializar(enviarAlta("registros-produccion", """
				{"lineaId":"%s","total":4}
				""".formatted(lineaPropia), user("solo-operacion")).andExpect(status().isCreated())).path("id").asText();
		mvc.perform(delete("/api/v1/registros-produccion/{id}", avance).with(user("solo-operacion")).with(csrf()))
				.andExpect(status().isForbidden());

		String parada = UUID.randomUUID().toString();
		jdbc.update("INSERT INTO paradas (id, linea_id, orden_id) VALUES (?, ?, ?)",
				UUID.fromString(parada), UUID.fromString(lineaPropia), UUID.fromString(ajena));
		mvc.perform(get("/api/v1/paradas/{id}", parada).with(user("operador")))
				.andExpect(status().isNotFound());
		enviarAlta("paradas", """
				{"lineaId":"%s","ordenId":"%s"}
				""".formatted(lineaPropia, ajena), user("operador")).andExpect(status().isForbidden());
	}

	private static String datosOrden(String codigo, String linea) {
		return """
				{"codigo":"%s","lineaId":"%s"}
				""".formatted(codigo, linea);
	}

	private String altaArea(String nombre) throws Exception {
		return deserializar(enviarAlta("areas", """
				{"nombre":"%s","activo":true}
				""".formatted(nombre), user("administrador")).andExpect(status().isCreated())).path("id").asText();
	}

	private String altaUsuario(String identidad, String area, boolean activo) throws Exception {
		return deserializar(enviarAlta("usuarios", """
				{"nombre":"%s","referenciaAutenticacion":"%s","areasIds":["%s"],"activo":%b}
				""".formatted(identidad, identidad, area, activo), user("administrador"))
				.andExpect(status().isCreated())).path("id").asText();
	}

	private String altaPersona(String identificacion, String nombre, boolean activo) throws Exception {
		String usuario = altaUsuario("cuenta-" + identificacion, areaPruebas, true);
		return alta("personas", """
				{"identificacion":"%s","nombre":"%s","activo":%b,"usuarioId":"%s"}
				""".formatted(identificacion, nombre, activo, usuario)).path("id").asText();
	}

	private JsonNode asignarRol(String usuarioId, String codigo) throws Exception {
		return deserializar(enviarAlta("usuario-roles", comandoRol(usuarioId, codigo), user("administrador"))
				.andExpect(status().isCreated()));
	}

	private String comandoRol(String usuarioId, String codigo) {
		String id = roles.findAll().stream().filter(rol -> codigo.equals(rol.getCodigo()))
				.findFirst().orElseThrow().getId().toString();
		return """
				{"usuarioId":"%s","rolId":"%s"}
				""".formatted(usuarioId, id);
	}

	private static String reporteEn(String area) {
		return """
				{"areaId":"%s","ubicacion":"Pasillo","descripcion":"Hay un obstáculo"}
				""".formatted(area);
	}

	private static MockMultipartFile parteReporte(String area) {
		return new MockMultipartFile("reporte", "reporte.json", MediaType.APPLICATION_JSON_VALUE,
				reporteEn(area).getBytes(StandardCharsets.UTF_8));
	}

	private static byte[] datosImagen(String formato) throws Exception {
		BufferedImage imagen = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		assertThat(ImageIO.write(imagen, formato, bytes)).isTrue();
		return bytes.toByteArray();
	}

	private JsonNode alta(String recurso, String datos) throws Exception {
		return deserializar(enviarAlta(recurso, datos, user("operador")).andExpect(status().isCreated()));
	}

	private ResultActions enviarAlta(String recurso, String datos, RequestPostProcessor usuario) throws Exception {
		return mvc.perform(post("/api/v1/" + recurso).with(usuario).with(csrf())
				.contentType(MediaType.APPLICATION_JSON).content(datos));
	}

	private JsonNode deserializar(ResultActions resultado) throws Exception {
		return jsonMapper.readTree(resultado.andReturn().getResponse().getContentAsString());
	}

}
