package com.kvizion.dao;

import com.kvizion.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // a senha fica gravada como hash SHA-256, entao a comparacao e feita no banco
    // retorna null quando o login ou a senha estao errados
    public Usuario autenticar(String login, String senha) throws SQLException {
        String sql = "SELECT id, nome, login, perfil FROM usuario WHERE login = ? AND senha = SHA2(?, 256)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("login"),
                            rs.getString("perfil")
                    );
                }
            }
        }

        return null;
    }
}
