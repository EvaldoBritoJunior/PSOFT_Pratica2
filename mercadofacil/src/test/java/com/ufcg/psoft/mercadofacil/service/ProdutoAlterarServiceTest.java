package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Testes para a alteração do Produto")
public class ProdutoAlterarServiceTest {

    @Autowired
    ProdutoAlterarService driver;
    @MockBean
    ProdutoRepository<Produto, Long> produtoRepository;
    Produto produto;

    @BeforeEach
    void setup() {
        Mockito.when(produtoRepository.find(10L))
                .thenReturn(Produto.builder()
                        .id(10L)
                        .codigoBarra("7899137500100")
                        .nome("Produto Dez")
                        .fabricante("Empresa Dez")
                        .preco(450.00)
                        .build()
                );
        produto = produtoRepository.find(10L);
        Mockito.when(produtoRepository.update(produto))
                .thenReturn(Produto.builder()
                        .id(10L)
                        .codigoBarra("7899137500117")
                        .nome("Nome Produto Alterado")
                        .fabricante("Nome Fabricante Alterado")
                        .preco(500.00)
                        .build()
                );
    }

    @Test
    @DisplayName("Quando altero o nome do produto com dados válidos")
    void alterarNomeDoProduto() {
        /* AAA Pattern */
        //Arrange
        produto.setNome("Nome Produto Alterado");
        //Act
        Produto resultado = driver.alterar(produto);
        //Assert
        assertEquals("Nome Produto Alterado", resultado.getNome());
    }

    @Test
    @DisplayName("Quando altero o nome do fabricante com dados válidos")
    void alterarFabricanteDoProduto() {
        /* AAA Pattern */
        //Arrange
        produto.setFabricante("Nome Fabricante Alterado");
        //Act
        Produto resultado = driver.alterar(produto);
        //Assert
        assertEquals("Nome Fabricante Alterado", resultado.getFabricante());
    }

    @Test
    @DisplayName("Quando altero o preço do produto com dados válidos")
    void alterarPrecoDoProduto() {
        /* AAA Pattern */
        //Arrange
        produto.setPreco(500.00);
        //Act
        Produto resultado = driver.alterar(produto);
        //Assert
        assertEquals(500.00, resultado.getPreco());
    }

    @Test
    @DisplayName("Quando altero o codigo de barra com dados válidos")
    void alterarCodigoDoProduto() {
        /* AAA Pattern */
        //Arrange
        produto.setCodigoBarra("7899137500117");
        //Act
        Produto resultado = driver.alterar(produto);
        //Assert
        assertEquals("7899137500117", resultado.getCodigoBarra());
    }

    @Test
    @DisplayName("Quando altero o preco do produto com dados inválidos")
    void precoMenorIgualAZero() {
        //Arrange
        produto.setPreco(0.0);
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Preco invalido!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando altero o nome do produto com dados invalidos")
    void alterarNomeInvalido() {
        //Arrange
        produto.setNome(null);
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Atributos faltando!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando altero o fabricante do produto com dados invalidos")
    void alterarFabricanteInvalido() {
        //Arrange
        produto.setFabricante(null);
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Atributos faltando!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o codigo de barras tem digito verificador invalido")
    void digitoVerificadorInvalido() {
        //Arrange
        produto.setCodigoBarra("7899137500104");
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Codigo de barra com digito verificador incorreto!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o codigo de barras tem pais invalido")
    void codigoPaisInvalido() {
        //Arrange
        produto.setCodigoBarra("9879137500104");
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Codigo de barra com país errado", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o codigo de barras tem empresa invalida")
    void codigoEmpresaInvalida() {
        //Arrange
        produto.setCodigoBarra("7895137500104");
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Codigo de barra com empresa errada!", thrown.getMessage());
    }
}



