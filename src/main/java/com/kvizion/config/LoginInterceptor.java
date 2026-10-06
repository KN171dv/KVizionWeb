package com.kvizion.config;

import com.kvizion.controller.Sessao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

// Roda antes de cada chamada /api/...: sem login, responde 401 e nao deixa continuar
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession sessao = request.getSession(false);

        if (sessao == null || sessao.getAttribute(Sessao.USUARIO_LOGADO) == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"mensagem\":\"Faça login para continuar.\"}");
            return false;
        }
        return true;
    }
}
