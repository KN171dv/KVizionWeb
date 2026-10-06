package com.kvizion.model;

// Item de uma venda ou de um orcamento.
// No desktop existiam ItemVenda e ItemOrcamento com o mesmo codigo.
public class ItemPedido {

    private Produto produto;
    private int quantidade;
    private double subtotal;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.subtotal = produto.getPreco() * quantidade;
    }

    // usado quando o item vem do banco e o subtotal ja esta gravado
    public ItemPedido(Produto produto, int quantidade, double subtotal) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
