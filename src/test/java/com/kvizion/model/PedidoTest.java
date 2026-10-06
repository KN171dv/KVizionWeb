package com.kvizion.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PedidoTest {

    private Cliente cliente;
    private Produto cimento;
    private Produto areia;

    @BeforeEach
    public void preparar() {
        cliente = new Cliente(1, "Carlos Silva", "Rua A", "99999-1111");
        cimento = new Produto(1, "Cimento", 40.00, 100, 20);
        areia = new Produto(2, "Areia", 20.00, 50, 10);
    }

    @Test
    public void vendaNovaComecaComTotalZero() {
        Venda venda = new Venda(0, cliente);

        assertEquals(0.00, venda.getTotal(), 0.001);
        assertEquals(0, venda.getItens().size());
    }

    @Test
    public void totalDeveSomarOsSubtotaisDosItens() {
        Venda venda = new Venda(0, cliente);

        venda.adicionarItem(new ItemPedido(cimento, 2)); // 80,00
        venda.adicionarItem(new ItemPedido(areia, 3));   // 60,00

        assertEquals(140.00, venda.getTotal(), 0.001);
    }

    @Test
    public void removerItemDeveDiminuirOTotal() {
        Venda venda = new Venda(0, cliente);
        venda.adicionarItem(new ItemPedido(cimento, 2)); // 80,00
        venda.adicionarItem(new ItemPedido(areia, 3));   // 60,00

        venda.removerItem(0); // tira o cimento

        assertEquals(60.00, venda.getTotal(), 0.001);
        assertEquals(1, venda.getItens().size());
    }

    @Test
    public void quantidadeDoProdutoDeveSomarItensRepetidos() {
        Venda venda = new Venda(0, cliente);
        venda.adicionarItem(new ItemPedido(cimento, 2));
        venda.adicionarItem(new ItemPedido(areia, 3));
        venda.adicionarItem(new ItemPedido(cimento, 5));

        assertEquals(7, venda.getQuantidadeDoProduto(cimento.getId()));
        assertEquals(3, venda.getQuantidadeDoProduto(areia.getId()));
    }

    @Test
    public void orcamentoCalculaTotalDoMesmoJeitoQueVenda() {
        Orcamento orcamento = new Orcamento(0, cliente);

        orcamento.adicionarItem(new ItemPedido(cimento, 10));

        assertEquals(400.00, orcamento.getTotal(), 0.001);
    }
}
