package com.kvizion.controller;

import com.kvizion.service.RegraNegocioException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Transforma as excecoes em respostas JSON no formato {"mensagem": "..."}
@RestControllerAdvice
public class TratadorDeErros {

    // regra de negocio ou validacao nao atendida
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, String>> regraDeNegocio(RegraNegocioException e) {
        return responder(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<Map<String, String>> acessoNegado(AcessoNegadoException e) {
        return responder(HttpStatus.FORBIDDEN, e.getMessage());
    }

    // o JSON enviado nao pode ser lido (por exemplo, letra em campo numerico)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> dadosInvalidos(HttpMessageNotReadableException e) {
        return responder(HttpStatus.BAD_REQUEST, "Dados inválidos.");
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, String>> erroDeBanco(SQLException e) {
        e.printStackTrace(); // o detalhe tecnico fica no console do servidor
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao acessar o banco de dados.");
    }

    private ResponseEntity<Map<String, String>> responder(HttpStatus status, String mensagem) {
        Map<String, String> corpo = new HashMap<>();
        corpo.put("mensagem", mensagem);
        return ResponseEntity.status(status).body(corpo);
    }
}
