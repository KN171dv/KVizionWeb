package com.kvizion.config;

import com.kvizion.dao.Conexao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// Quando o sistema sobe, passa os dados do banco (lidos do application.properties)
// para a classe Conexao, que os DAOs ja usavam.
@Component
public class ConexaoConfig {

    public ConexaoConfig(@Value("${db.url}") String url,
                         @Value("${db.usuario}") String usuario,
                         @Value("${db.senha}") String senha) {
        Conexao.configurar(url, usuario, senha);
    }
}
