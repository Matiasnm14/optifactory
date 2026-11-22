package edu.upb.optifacotry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}
