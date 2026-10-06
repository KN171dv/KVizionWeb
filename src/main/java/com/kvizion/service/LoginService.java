package com.kvizion.service;

import com.kvizion.dao.UsuarioDAO;
import java.sql.SQLException;
import com.kvizion.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticar(String login, String senha) throws RegraNegocioException, SQLException {
        if (login == null || login.trim().isEmpty() || senha == null || senha.isEmpty()) {
            throw new RegraNegocioException("Informe o login e a senha.");
        }

        Usuario usuario = usuarioDAO.autenticar(login.trim(), senha);
        if (usuario == null) {
            throw new RegraNegocioException("Login ou senha inválidos.");
        }
        return usuario;
    }
}
