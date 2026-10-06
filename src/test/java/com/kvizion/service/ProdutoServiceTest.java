package com.kvizion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.kvizion.model.Produto;
import org.junit.jupiter.api.Test;

// Testa as validacoes de produto que sao recusadas antes de chegar ao banco de dados
public class ProdutoServiceTest {

    private ProdutoService produtoService = new ProdutoService();

    @Test
    public void nomeVazioERecusado() {
        Produto produto = new Produto(0, "", 10.00, 5, 1);

        assertThrows(RegraNegocioException.class, () -> produtoService.validar(produto));
    }

    @Test
    public void precoZeroERecusado() {
        Produto produto = new Produto(0, "Prego", 0, 5, 1);

        assertThrows(RegraNegocioException.class, () -> produtoService.validar(produto));
    }

    // Falha registrada no bugtracking: preco 1,999 era gravado como 2,00 sem aviso
    @Test
    public void precoComTresCasasDecimaisERecusado() {
        Produto produto = new Produto(0, "Prego", 1.999, 5, 1);

        RegraNegocioException erro = assertThrows(RegraNegocioException.class, () -> produtoService.validar(produto));

        assertEquals("O preço deve ter no máximo 2 casas decimais.", erro.getMessage());
    }

    @Test
    public void estoqueNegativoERecusado() {
        Produto produto = new Produto(0, "Prego", 10.00, -1, 1);

        assertThrows(RegraNegocioException.class, () -> produtoService.validar(produto));
    }
}
