package com.kvizion.controller;

import com.kvizion.dto.ProdutoDTO;
import com.kvizion.model.Produto;
import com.kvizion.service.ProdutoService;
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
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // GET /api/produtos?nome=...  (sem o parametro, lista todos)
    @GetMapping
    public List<Produto> listar(@RequestParam(name = "nome", defaultValue = "") String nome) throws SQLException {
        return produtoService.pesquisar(nome);
    }

    @GetMapping("/{id}")
    public Produto buscar(@PathVariable("id") int id) throws RegraNegocioException, SQLException {
        return produtoService.buscarPorId(id);
    }

    @PostMapping
    public void cadastrar(@RequestBody ProdutoDTO dados) throws RegraNegocioException, SQLException {
        produtoService.cadastrar(dados.paraProduto(0));
    }

    @PutMapping("/{id}")
    public void atualizar(@PathVariable("id") int id, @RequestBody ProdutoDTO dados) throws RegraNegocioException, SQLException {
        produtoService.atualizar(dados.paraProduto(id));
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable("id") int id, HttpSession sessao)
            throws RegraNegocioException, AcessoNegadoException, SQLException {
        Sessao.exigirAdmin(sessao);
        produtoService.excluir(id);
    }
}
