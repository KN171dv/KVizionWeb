package com.kvizion.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.kvizion.model.Cliente;
import org.junit.jupiter.api.Test;

// Testa so a validacao, que nao precisa do banco de dados
public class ClienteServiceTest {

    private ClienteService clienteService = new ClienteService();

    @Test
    public void clienteValidoPassaNaValidacao() throws RegraNegocioException {
        Cliente cliente = new Cliente(0, "Maria Souza", "Rua B, 20", "(21) 99999-2222");

        clienteService.validar(cliente); // nao pode lancar excecao
    }

    @Test
    public void nomeVazioERecusado() {
        assertThrows(RegraNegocioException.class, () -> clienteService.validar(new Cliente(0, "   ", "Rua B", "99999-2222")));
    }

    @Test
    public void nomeComMaisDe100CaracteresERecusado() {
        String nomeGrande = "a".repeat(101);

        assertThrows(RegraNegocioException.class, () -> clienteService.validar(new Cliente(0, nomeGrande, "Rua B", "99999-2222")));
    }

    @Test
    public void telefoneVazioERecusado() {
        assertThrows(RegraNegocioException.class, () -> clienteService.validar(new Cliente(0, "Maria Souza", "Rua B", "")));
    }

    @Test
    public void telefoneComLetrasERecusado() {
        assertThrows(RegraNegocioException.class, () -> clienteService.validar(new Cliente(0, "Maria Souza", "Rua B", "telefone")));
    }
}
