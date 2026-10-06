package com.kvizion.controller;

import com.kvizion.dto.LoginDTO;
import com.kvizion.model.Usuario;
import com.kvizion.service.LoginService;
import com.kvizion.service.RegraNegocioException;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // Confere login e senha no banco e guarda o usuario na sessao
    @PostMapping("/login")
    public Usuario entrar(@RequestBody LoginDTO dados, HttpSession sessao) throws RegraNegocioException, SQLException {
        Usuario usuario = loginService.autenticar(dados.getLogin(), dados.getSenha());
        sessao.setAttribute(Sessao.USUARIO_LOGADO, usuario);
        return usuario;
    }

    @PostMapping("/logout")
    public void sair(HttpSession sessao) {
        sessao.invalidate();
    }
}
