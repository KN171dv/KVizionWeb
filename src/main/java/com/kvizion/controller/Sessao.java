package com.kvizion.controller;

import com.kvizion.model.Usuario;
import jakarta.servlet.http.HttpSession;

// Funcoes para guardar e conferir o usuario logado na sessao HTTP
public class Sessao {

    public static final String USUARIO_LOGADO = "usuarioLogado";

    public static void exigirAdmin(HttpSession sessao) throws AcessoNegadoException {
        Usuario usuario = (Usuario) sessao.getAttribute(USUARIO_LOGADO);
        if (usuario == null || !usuario.isAdmin()) {
            throw new AcessoNegadoException("Somente o administrador pode fazer isso.");
        }
    }
}
