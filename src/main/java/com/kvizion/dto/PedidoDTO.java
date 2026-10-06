package com.kvizion.dto;

import java.util.ArrayList;
import java.util.List;

// Dados enviados pelas paginas de nova venda e novo orcamento
public class PedidoDTO {

    private int clienteId;
    private List<ItemDTO> itens = new ArrayList<>();

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemDTO> itens) {
        this.itens = itens;
    }
}
