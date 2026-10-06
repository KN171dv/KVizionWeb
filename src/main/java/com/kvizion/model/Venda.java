package com.kvizion.model;

import java.time.LocalDateTime;

public class Venda extends Pedido {

    public Venda(int id, Cliente cliente) {
        super(id, cliente);
    }

    public Venda(int id, Cliente cliente, double total, LocalDateTime data) {
        super(id, cliente, total, data);
    }
}
