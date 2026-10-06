package com.kvizion.dto;

import com.kvizion.model.Produto;
import com.kvizion.service.RegraNegocioException;

// Dados que o formulario de produto envia
public class ProdutoDTO {

    private String nome;
    private Double preco;
    private Integer quantidade;
    private Integer estoqueMinimo;

    public Produto paraProduto(int id) throws RegraNegocioException {
        if (preco == null) {
            throw new RegraNegocioException("Informe o preço do produto.");
        }
        if (quantidade == null) {
            throw new RegraNegocioException("Informe o estoque do produto.");
        }

        String nomeLimpo = (nome == null) ? "" : nome.trim();
        int minimo = (estoqueMinimo == null) ? 0 : estoqueMinimo; // estoque minimo e opcional

        return new Produto(id, nomeLimpo, preco, quantidade, minimo);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }
}
