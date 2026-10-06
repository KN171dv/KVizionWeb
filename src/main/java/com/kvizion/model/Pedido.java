package com.kvizion.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Classe base com o que Venda e Orcamento tem em comum
public abstract class Pedido {

    private int id;
    private Cliente cliente;
    private List<ItemPedido> itens = new ArrayList<>();
    private double total;
    private LocalDateTime data;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
    }

    // usado nas consultas, quando o total e a data vem do banco
    public Pedido(int id, Cliente cliente, double total, LocalDateTime data) {
        this.id = id;
        this.cliente = cliente;
        this.total = total;
        this.data = data;
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        total += item.getSubtotal();
    }

    public void removerItem(int posicao) {
        ItemPedido item = itens.remove(posicao);
        total -= item.getSubtotal();
    }

    // soma quanto de um produto ja foi colocado no pedido
    public int getQuantidadeDoProduto(int idProduto) {
        int soma = 0;
        for (ItemPedido item : itens) {
            if (item.getProduto().getId() == idProduto) {
                soma += item.getQuantidade();
            }
        }
        return soma;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public double getTotal() {
        return total;
    }

    public LocalDateTime getData() {
        return data;
    }
}
