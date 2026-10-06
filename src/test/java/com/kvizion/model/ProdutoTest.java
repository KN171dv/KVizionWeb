package com.kvizion.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ProdutoTest {

    @Test
    public void estoqueAcimaDoMinimoNaoEBaixo() {
        Produto produto = new Produto(1, "Cimento", 40.00, 21, 20);

        assertFalse(produto.isEstoqueBaixo());
    }

    @Test
    public void estoqueIgualAoMinimoEBaixo() {
        Produto produto = new Produto(1, "Cimento", 40.00, 20, 20);

        assertTrue(produto.isEstoqueBaixo());
    }

    @Test
    public void estoqueAbaixoDoMinimoEBaixo() {
        Produto produto = new Produto(1, "Cimento", 40.00, 5, 20);

        assertTrue(produto.isEstoqueBaixo());
    }
}
