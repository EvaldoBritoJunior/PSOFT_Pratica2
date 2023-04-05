package com.ufcg.psoft.mercadofacil.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Produtos")
public class ProdutoV1ControllerTests {
    @Autowired
    MockMvc driver;

    @Autowired
    ProdutoRepository<Produto, Long> produtoRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Produto produto;

    @BeforeEach
    void setup() {
        produto = produtoRepository.find(10L);
    }

    @AfterEach
    void tearDown() {
        produto = null;
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de campos obrigatórios")
    class ProdutoValidacaoCamposObrigatorios {

        @Test
        @DisplayName("Quando alteramos o nome do produto com dados válidos")
        void quandoAlteramosNomeDoProdutoValido() throws Exception {
            // Arrange
            produto.setNome("Produto Dez Alterado");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals(resultado.getNome(), "Nome Produto Alterado");
        }

        @Test
        @DisplayName("Quando altero o nome do fabricante com dados válidos")
        void alterarFabricanteDoProduto() throws Exception {
            /* AAA Pattern */
            //Arrange
            produto.setFabricante("Nome Fabricante Alterado");
            //Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();
            //Assert
            assertEquals("Nome Fabricante Alterado", resultado.getFabricante());
        }

        @Test
        @DisplayName("Quando altero o nome do produto com dados invalidos")
        void alterarNomeInvalido() {
            //Arrange
            produto.setNome(null);
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString());
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Atributos faltando!", thrown.getMessage());
        }

        @Test
        @DisplayName("Quando o fabricante é invalido")
        void alterarFabricanteInvalido() {
            //Arrange
            produto.setFabricante(null);
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString());
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Atributos faltando!", thrown.getMessage());
        }

    }

    @Nested
    @DisplayName("Conjunto de casos de verificação da regra sobre o preço")
    class ProdutoValidacaoRegrasDoPreco {

        @Test
        @DisplayName("Quando altero o preço do produto com dados válidos")
        void alterarPrecoDoProduto() throws Exception {
            /* AAA Pattern */
            //Arrange
            produto.setPreco(500.00);
            //Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();
            //Assert
            assertEquals(500.00, resultado.getPreco());
        }

        @Test
        @DisplayName("Quando o preço é menor ou igual a zero")
        void precoMenorIgualAZero() {
            //Arrange
            produto.setPreco(0.0);
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString()
            );
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Preco invalido!", thrown.getMessage());
        }

    }

    @Nested
    @DisplayName("Conjunto de casos de verificação da validação do código de barras")
    class ProdutoValidacaoCodigoDeBarras {

        @Test
        @DisplayName("Quando altero o codigo de barra com dados válidos")
        void alterarCodigoDoProduto() throws Exception {
            /* AAA Pattern */
            //Arrange
            produto.setCodigoBarra("7899137500117");
            //Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();
            //Assert
            assertEquals("7899137500117", resultado.getCodigoBarra());
        }

        @Test
        @DisplayName("Quando o codigo de barras tem digito verificador invalido")
        void digitoVerificadorInvalido() {
            //Arrange
            produto.setCodigoBarra("7899137500104");
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString()
            );
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Codigo de barra com digito verificador incorreto!", thrown.getMessage());
        }

        @Test
        @DisplayName("Quando o codigo de barras tem pais invalido")
        void codigoPaisInvalido() {
            //Arrange
            produto.setCodigoBarra("9879137500104");
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString()
            );
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Codigo de barra com país errado", thrown.getMessage());
        }

        @Test
        @DisplayName("Quando o codigo de barras tem empresa invalida")
        void codigoEmpresaInvalida() {
            //Arrange
            produto.setCodigoBarra("7895137500104");
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString()
            );
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Codigo de barra com empresa errada!", thrown.getMessage());
        }
    }

}
