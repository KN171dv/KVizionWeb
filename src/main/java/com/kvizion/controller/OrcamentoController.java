package com.kvizion.controller;

import com.kvizion.dto.ItemDTO;
import com.kvizion.dto.PedidoDTO;
import com.kvizion.model.Cliente;
import com.kvizion.model.ItemPedido;
import com.kvizion.model.Orcamento;
import com.kvizion.model.Produto;
import com.kvizion.service.ClienteService;
import com.kvizion.service.OrcamentoService;
import com.kvizion.service.ProdutoService;
import com.kvizion.service.RegraNegocioException;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public OrcamentoController(OrcamentoService orcamentoService, ClienteService clienteService, ProdutoService produtoService) {
        this.orcamentoService = orcamentoService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    // GET /api/orcamentos?cliente=...  (sem o parametro, lista todos)
    @GetMapping
    public List<Orcamento> listar(@RequestParam(name = "cliente", defaultValue = "") String cliente) throws SQLException {
        return orcamentoService.listar(cliente);
    }

    @GetMapping("/{id}/itens")
    public List<ItemPedido> listarItens(@PathVariable("id") int id) throws SQLException {
        return orcamentoService.listarItens(id);
    }

    // Recebe o cliente e os itens e grava o orcamento (nao mexe no estoque)
    @PostMapping
    public Orcamento registrar(@RequestBody PedidoDTO dados) throws RegraNegocioException, SQLException {
        if (dados.getClienteId() == 0) {
            throw new RegraNegocioException("Selecione um cliente.");
        }
        Cliente cliente = clienteService.buscarPorId(dados.getClienteId());

        Orcamento orcamento = new Orcamento(0, cliente);
        for (ItemDTO item : dados.getItens()) {
            Produto produto = produtoService.buscarPorId(item.getProdutoId());
            orcamentoService.adicionarItem(orcamento, produto, item.getQuantidade());
        }

        orcamentoService.salvar(orcamento);
        return orcamento;
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable("id") int id, HttpSession sessao) throws AcessoNegadoException, SQLException {
        Sessao.exigirAdmin(sessao);
        orcamentoService.excluir(id);
    }
}
