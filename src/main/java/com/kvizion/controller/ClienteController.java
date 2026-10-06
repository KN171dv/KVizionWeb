package com.kvizion.controller;

import com.kvizion.dto.ClienteDTO;
import com.kvizion.model.Cliente;
import com.kvizion.service.ClienteService;
import com.kvizion.service.RegraNegocioException;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // GET /api/clientes?nome=...  (sem o parametro, lista todos)
    @GetMapping
    public List<Cliente> listar(@RequestParam(name = "nome", defaultValue = "") String nome) throws SQLException {
        return clienteService.pesquisar(nome);
    }

    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable("id") int id) throws RegraNegocioException, SQLException {
        return clienteService.buscarPorId(id);
    }

    @PostMapping
    public void cadastrar(@RequestBody ClienteDTO dados) throws RegraNegocioException, SQLException {
        clienteService.cadastrar(dados.paraCliente(0));
    }

    @PutMapping("/{id}")
    public void atualizar(@PathVariable("id") int id, @RequestBody ClienteDTO dados) throws RegraNegocioException, SQLException {
        clienteService.atualizar(dados.paraCliente(id));
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable("id") int id, HttpSession sessao)
            throws RegraNegocioException, AcessoNegadoException, SQLException {
        Sessao.exigirAdmin(sessao);
        clienteService.excluir(id);
    }
}
