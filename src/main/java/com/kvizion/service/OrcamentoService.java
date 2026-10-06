package com.kvizion.service;

import com.kvizion.dao.OrcamentoDAO;
import java.sql.SQLException;
import java.util.List;
import com.kvizion.model.ItemPedido;
import com.kvizion.model.Orcamento;
import com.kvizion.model.Produto;
import org.springframework.stereotype.Service;

@Service
public class OrcamentoService {

    private OrcamentoDAO orcamentoDAO = new OrcamentoDAO();

    // no orcamento nao precisa conferir estoque, so a quantidade
    public void adicionarItem(Orcamento orcamento, Produto produto, int quantidade) throws RegraNegocioException {
        if (quantidade <= 0) {
            throw new RegraNegocioException("A quantidade deve ser maior que zero.");
        }
        orcamento.adicionarItem(new ItemPedido(produto, quantidade));
    }

    public void salvar(Orcamento orcamento) throws RegraNegocioException, SQLException {
        if (orcamento.getCliente() == null) {
            throw new RegraNegocioException("Selecione um cliente.");
        }
        if (orcamento.getItens().isEmpty()) {
            throw new RegraNegocioException("Adicione pelo menos um item ao orçamento.");
        }
        orcamentoDAO.inserir(orcamento);
    }

    public void excluir(int idOrcamento) throws SQLException {
        orcamentoDAO.excluir(idOrcamento);
    }

    public List<Orcamento> listar(String nomeCliente) throws SQLException {
        return orcamentoDAO.listar(nomeCliente);
    }

    public List<ItemPedido> listarItens(int idOrcamento) throws SQLException {
        return orcamentoDAO.listarItens(idOrcamento);
    }
}
