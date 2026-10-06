package com.kvizion.dto;

// Um item enviado pela pagina de venda ou de orcamento
public class ItemDTO {

    private int produtoId;
    private int quantidade;

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
