package com.kvizion.model;

public class Produto {

    private int id;
    private String nome;
    private double preco;
    private int quantidade;
    private int estoqueMinimo;

    public Produto(int id, String nome, double preco, int quantidade, int estoqueMinimo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoqueMinimo = estoqueMinimo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public boolean isEstoqueBaixo() {
        return quantidade <= estoqueMinimo;
    }
}
