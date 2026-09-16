package br.com.fiap.campusride;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsBadRequestForInvalidEnum() throws Exception {
        mockMvc.perform(post("/caronas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "motorista": "Ana Souza",
                                  "origem": "Campus Norte",
                                  "destino": "Campus Sul",
                                  "dataHoraPartida": "2030-12-20T08:00:00",
                                  "tipoVeiculo": "BICICLETA",
                                  "vagasTotais": 2
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Corpo da requisição inválido"))
                .andExpect(jsonPath("$.path").value("/caronas"));
    }

    @Test
    void returnsBadRequestWithFieldErrorsForInvalidInput() throws Exception {
        mockMvc.perform(post("/caronas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "motorista": "   ",
                                  "origem": "Campus Norte",
                                  "destino": "Campus Sul",
                                  "dataHoraPartida": "2030-12-20T08:00:00",
                                  "tipoVeiculo": "CARRO",
                                  "vagasTotais": 2
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dados de entrada inválidos"))
                .andExpect(jsonPath("$.fields.motorista").value("Motorista é obrigatório"));
    }

    @Test
    void returnsNotFoundForUnknownRoute() throws Exception {
        mockMvc.perform(get("/rota-inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Recurso não encontrado"));
    }
}
