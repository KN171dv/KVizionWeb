package com.kvizion.service;

// Excecao usada quando uma regra de negocio ou validacao nao e atendida.
// A mensagem ja vem pronta para ser mostrada ao usuario (na tela ou na pagina web).
public class RegraNegocioException extends Exception {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
