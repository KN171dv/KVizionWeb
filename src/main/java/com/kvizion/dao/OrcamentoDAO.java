package com.kvizion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.kvizion.model.Cliente;
import com.kvizion.model.ItemPedido;
import com.kvizion.model.Orcamento;
import com.kvizion.model.Produto;

public class OrcamentoDAO {

    // Grava o orcamento e os itens na mesma transacao. Orcamento nao mexe no estoque.
    public void inserir(Orcamento orcamento) throws SQLException {
        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                int idOrcamento = inserirOrcamento(conn, orcamento);
                for (ItemPedido item : orcamento.getItens()) {
                    inserirItem(conn, idOrcamento, item);
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private int inserirOrcamento(Connection conn, Orcamento orcamento) throws SQLException {
        String sql = "INSERT INTO orcamento (cliente_id, total) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, orcamento.getCliente().getId());
            stmt.setDouble(2, orcamento.getTotal());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private void inserirItem(Connection conn, int idOrcamento, ItemPedido item) throws SQLException {
        String sql = "INSERT INTO item_orcamento (orcamento_id, produto_id, quantidade, subtotal) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOrcamento);
            stmt.setInt(2, item.getProduto().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getSubtotal());
            stmt.executeUpdate();
        }
    }

    public List<Orcamento> listar(String nomeCliente) throws SQLException {
        List<Orcamento> lista = new ArrayList<>();
        String sql = "SELECT o.id, o.total, o.data_orcamento, c.id AS cliente_id, c.nome, c.endereco, c.telefone "
                   + "FROM orcamento o INNER JOIN cliente c ON c.id = o.cliente_id "
                   + "WHERE c.nome LIKE ? ORDER BY o.id DESC";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeCliente + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getInt("cliente_id"),
                            rs.getString("nome"),
                            rs.getString("endereco"),
                            rs.getString("telefone"));

                    lista.add(new Orcamento(
                            rs.getInt("id"),
                            cliente,
                            rs.getDouble("total"),
                            rs.getTimestamp("data_orcamento").toLocalDateTime()));
                }
            }
        }
        return lista;
    }

    public List<ItemPedido> listarItens(int idOrcamento) throws SQLException {
        List<ItemPedido> lista = new ArrayList<>();
        String sql = "SELECT i.quantidade, i.subtotal, p.id, p.nome, p.preco, p.quantidade AS estoque, p.estoque_minimo "
                   + "FROM item_orcamento i INNER JOIN produto p ON p.id = i.produto_id "
                   + "WHERE i.orcamento_id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOrcamento);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getDouble("preco"),
                            rs.getInt("estoque"),
                            rs.getInt("estoque_minimo"));

                    lista.add(new ItemPedido(produto, rs.getInt("quantidade"), rs.getDouble("subtotal")));
                }
            }
        }
        return lista;
    }

    // confere se existe orcamento com esse id
    public boolean existe(int idOrcamento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orcamento WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOrcamento);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public void excluir(int idOrcamento) throws SQLException {
        String sqlItens = "DELETE FROM item_orcamento WHERE orcamento_id = ?";
        String sqlOrcamento = "DELETE FROM orcamento WHERE id = ?";

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                executar(conn, sqlItens, idOrcamento);
                executar(conn, sqlOrcamento, idOrcamento);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private void executar(Connection conn, String sql, int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
