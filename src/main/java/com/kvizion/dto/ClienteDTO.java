package com.kvizion.dto;

import com.kvizion.model.Cliente;

// Dados que o formulario de cliente envia
public class ClienteDTO {

    private String nome;
    private String endereco;
    private String telefone;

    // Monta o Cliente tirando os espacos das pontas (campo que nao veio vira texto vazio)
    public Cliente paraCliente(int id) {
        return new Cliente(id, limpar(nome), limpar(endereco), limpar(telefone));
    }

    private String limpar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
