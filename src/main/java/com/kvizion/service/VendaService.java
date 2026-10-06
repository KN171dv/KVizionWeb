package com.kvizion.service;

import com.kvizion.dao.VendaDAO;
import java.sql.SQLException;
import java.util.List;
import com.kvizion.model.ItemPedido;
import com.kvizion.model.Produto;
import com.kvizion.model.Venda;
import org.springframework.stereotype.Service;

@Service
public class VendaService {

    private VendaDAO vendaDAO = new VendaDAO();

    // Coloca um produto na venda, conferindo a quantidade e o estoque
    public void adicionarItem(Venda venda, Produto produto, int quantidade) throws RegraNegocioException {
        if (quantidade <= 0) {
            throw new RegraNegocioException("A quantidade deve ser maior que zero.");
        }

        int jaNaVenda = venda.getQuantidadeDoProduto(produto.getId());
        if (jaNaVenda + quantidade > produto.getQuantidade()) {
            throw new RegraNegocioException("Estoque insuficiente para " + produto.getNome()
                    + ". Em estoque: " + produto.getQuantidade());
        }

        venda.adicionarItem(new ItemPedido(produto, quantidade));
    }

    public void finalizar(Venda venda) throws RegraNegocioException, SQLException {
        if (venda.getCliente() == null) {
            throw new RegraNegocioException("Selecione um cliente.");
        }
        if (venda.getItens().isEmpty()) {
            throw new RegraNegocioException("Adicione pelo menos um item à venda.");
        }
        vendaDAO.inserir(venda);
    }

    // cancelar uma venda devolve os produtos para o estoque
    public void cancelar(int idVenda) throws RegraNegocioException, SQLException {
        if (!vendaDAO.existe(idVenda)) {
            throw new RegraNegocioException("Venda não encontrada.");
        }
        vendaDAO.excluir(idVenda);
    }

    public List<Venda> listar(String nomeCliente) throws SQLException {
        return vendaDAO.listar(nomeCliente);
    }

    public List<ItemPedido> listarItens(int idVenda) throws SQLException {
        return vendaDAO.listarItens(idVenda);
    }
}
