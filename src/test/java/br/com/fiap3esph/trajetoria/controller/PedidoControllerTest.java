package br.com.fiap3esph.trajetoria.controller;

import br.com.fiap3esph.trajetoria.application.service.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PedidoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PedidoService pedidoService;

    @Test
    @DisplayName("Expectativa: Requisição sem corpo retorna 400 (Bad Request).")
    @WithMockUser(roles = "JOVEM")
    void cadastrarSemCorpoRetornaBadRequest() throws Exception {
        mockMvc.perform(post("/pedidos"))
                .andExpect(status().isBadRequest());
    }
}
