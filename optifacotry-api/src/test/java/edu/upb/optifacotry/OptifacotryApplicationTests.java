package edu.upb.optifacotry;

import edu.upb.optifacotry.repository.repos.AdjuntoRepository;
import edu.upb.optifacotry.repository.repos.ReporteSeguridadRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

@SpringBootTest
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

	@Test
	void contextLoads() {
	}

	@Test
	void preservesJsonbDecimalPrecision() throws Exception {
		String decimal = "1.12345678901234567890123456789";
		String body = mvc.perform(post("/api/v1/mediciones-maquina").with(user("operador")).with(csrf())
						.contentType(MediaType.APPLICATION_JSON).content("{\"valor\":" + decimal + "}"))
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
		String datos = """
				{"identificacion":" CI-123 ","nombre":"Ana","activo":true}
				""";
		JsonNode persona = alta("personas", datos);
		String id = persona.path("id").asText();
		assertThat(persona.path("identificacion").asText()).isEqualTo("CI-123");
		assertThat(persona.path("usuarioId").isNull()).isTrue();
		enviarAlta("personas", datos.replace(" CI-123 ", "CI-123"), user("operador"))
				.andExpect(status().isConflict());
		enviarAlta("personas", """
				{"identificacion":" ","nombre":"Ana","activo":true}
				""", user("operador")).andExpect(status().isBadRequest());

		String usuario = altaUsuario("cuenta-persona", UUID.randomUUID().toString(), true);
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
		String persona = alta("personas", """
				{"identificacion":"CI-TURNOS","nombre":"Rosa","activo":true}
				""").path("id").asText();
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
		String persona = alta("personas", """
				{"identificacion":"CI-PERIODO","nombre":"Luis","activo":true}
				""").path("id").asText();
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
		String persona = alta("personas", """
				{"identificacion":"CI-CAP","nombre":"Isa","activo":false}
				""").path("id").asText();
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

	private String altaArea(String nombre) throws Exception {
		return alta("areas", """
				{"nombre":"%s","activo":true}
				""".formatted(nombre)).path("id").asText();
	}

	private String altaUsuario(String identidad, String area, boolean activo) throws Exception {
		return deserializar(enviarAlta("usuarios", """
				{"nombre":"%s","referenciaAutenticacion":"%s","areasIds":["%s"],"activo":%b}
				""".formatted(identidad, identidad, area, activo), user("administrador").roles("ADMIN"))
				.andExpect(status().isCreated())).path("id").asText();
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
