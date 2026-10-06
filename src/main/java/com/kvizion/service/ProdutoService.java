package com.kvizion.service;

import com.kvizion.dao.ProdutoDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.kvizion.model.Produto;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private static final double PRECO_MAXIMO = 99999999.99; // limite da coluna DECIMAL(10,2)

    private ProdutoDAO produtoDAO = new ProdutoDAO();

    public void cadastrar(Produto produto) throws RegraNegocioException, SQLException {
        validar(produto);
        produtoDAO.inserir(produto);
    }

    public void atualizar(Produto produto) throws RegraNegocioException, SQLException {
        buscarPorId(produto.getId()); // confere se o produto existe
        validar(produto);
        produtoDAO.atualizar(produto);
    }

    public void excluir(int id) throws RegraNegocioException, SQLException {
        if (produtoDAO.possuiMovimentacao(id)) {
            throw new RegraNegocioException("Este produto já foi usado em vendas ou orçamentos e não pode ser excluído.");
        }
        produtoDAO.excluir(id);
    }

    public Produto buscarPorId(int id) throws RegraNegocioException, SQLException {
        Produto produto = produtoDAO.buscarPorId(id);
        if (produto == null) {
            throw new RegraNegocioException("Produto não encontrado.");
        }
        return produto;
    }

    public List<Produto> pesquisar(String nome) throws SQLException {
        return produtoDAO.pesquisar(nome);
    }

    // produtos que chegaram no estoque minimo
    public List<Produto> listarEstoqueBaixo() throws SQLException {
        List<Produto> baixos = new ArrayList<>();
        for (Produto p : produtoDAO.listar()) {
            if (p.isEstoqueBaixo()) {
                baixos.add(p);
            }
        }
        return baixos;
    }

    public void validar(Produto produto) throws RegraNegocioException, SQLException {
        String nome = produto.getNome();

        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("Informe o nome do produto.");
        }
        if (nome.length() > 100) {
            throw new RegraNegocioException("O nome deve ter no máximo 100 caracteres.");
        }
        if (produto.getPreco() <= 0 || produto.getPreco() > PRECO_MAXIMO) {
            throw new RegraNegocioException("O preço deve ser maior que zero.");
        }
        if (produto.getQuantidade() < 0) {
            throw new RegraNegocioException("O estoque não pode ser negativo.");
        }
        if (produto.getEstoqueMinimo() < 0) {
            throw new RegraNegocioException("O estoque mínimo não pode ser negativo.");
        }
        if (produtoDAO.existeNome(nome, produto.getId())) {
            throw new RegraNegocioException("Já existe um produto cadastrado com esse nome.");
        }
    }
}
