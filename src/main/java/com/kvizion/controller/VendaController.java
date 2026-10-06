package com.kvizion.controller;

import com.kvizion.dto.ItemDTO;
import com.kvizion.dto.PedidoDTO;
import com.kvizion.model.Cliente;
import com.kvizion.model.ItemPedido;
import com.kvizion.model.Produto;
import com.kvizion.model.Venda;
import com.kvizion.service.ClienteService;
import com.kvizion.service.ProdutoService;
import com.kvizion.service.RegraNegocioException;
import com.kvizion.service.VendaService;
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
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public VendaController(VendaService vendaService, ClienteService clienteService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    // GET /api/vendas?cliente=...  (sem o parametro, lista todas)
    @GetMapping
    public List<Venda> listar(@RequestParam(name = "cliente", defaultValue = "") String cliente) throws SQLException {
        return vendaService.listar(cliente);
    }

    @GetMapping("/{id}/itens")
    public List<ItemPedido> listarItens(@PathVariable("id") int id) throws SQLException {
        return vendaService.listarItens(id);
    }

    // Recebe o cliente e os itens, confere as regras e grava a venda (com baixa de estoque)
    @PostMapping
    public Venda registrar(@RequestBody PedidoDTO dados) throws RegraNegocioException, SQLException {
        if (dados.getClienteId() == 0) {
            throw new RegraNegocioException("Selecione um cliente.");
        }
        Cliente cliente = clienteService.buscarPorId(dados.getClienteId());

        Venda venda = new Venda(0, cliente);
        for (ItemDTO item : dados.getItens()) {
            Produto produto = produtoService.buscarPorId(item.getProdutoId());
            vendaService.adicionarItem(venda, produto, item.getQuantidade());
        }

        vendaService.finalizar(venda);
        return venda;
    }

    // Cancelar uma venda devolve os produtos ao estoque
    @DeleteMapping("/{id}")
    public void cancelar(@PathVariable("id") int id, HttpSession sessao)
            throws RegraNegocioException, AcessoNegadoException, SQLException {
        Sessao.exigirAdmin(sessao);
        vendaService.cancelar(id);
    }
}
