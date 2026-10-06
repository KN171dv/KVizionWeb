package com.kvizion.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static String url;
    private static String usuario;
    private static String senha;

    // Chamado uma vez quando o sistema sobe (classe ConexaoConfig),
    // com os dados lidos do application.properties / config.properties
    public static void configurar(String novaUrl, String novoUsuario, String novaSenha) {
        url = novaUrl;
        usuario = novoUsuario;
        senha = novaSenha;
    }

    public static Connection conectar() throws SQLException {
        if (url == null) {
            throw new SQLException("A conexao com o banco ainda nao foi configurada.");
        }
        return DriverManager.getConnection(url, usuario, senha);
    }
}
