package com.kvizion.model;

import java.time.LocalDateTime;

public class Orcamento extends Pedido {

    public Orcamento(int id, Cliente cliente) {
        super(id, cliente);
    }

    public Orcamento(int id, Cliente cliente, double total, LocalDateTime data) {
        super(id, cliente, total, data);
    }
}
