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
import com.kvizion.model.Produto;
import com.kvizion.model.Venda;

public class VendaDAO {

    // Grava a venda, os itens e da baixa no estoque na mesma transacao.
    // Se alguma parte falhar, o rollback desfaz tudo.
    public void inserir(Venda venda) throws SQLException {
        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                int idVenda = inserirVenda(conn, venda);
                for (ItemPedido item : venda.getItens()) {
                    inserirItem(conn, idVenda, item);
                    baixarEstoque(conn, item);
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private int inserirVenda(Connection conn, Venda venda) throws SQLException {
        String sql = "INSERT INTO venda (cliente_id, total) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, venda.getCliente().getId());
            stmt.setDouble(2, venda.getTotal());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private void inserirItem(Connection conn, int idVenda, ItemPedido item) throws SQLException {
        String sql = "INSERT INTO item_venda (venda_id, produto_id, quantidade, subtotal) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenda);
            stmt.setInt(2, item.getProduto().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getSubtotal());
            stmt.executeUpdate();
        }
    }

    private void baixarEstoque(Connection conn, ItemPedido item) throws SQLException {
        // so atualiza se ainda tiver quantidade suficiente
        String sql = "UPDATE produto SET quantidade = quantidade - ? WHERE id = ? AND quantidade >= ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getQuantidade());
            stmt.setInt(2, item.getProduto().getId());
            stmt.setInt(3, item.getQuantidade());

            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Estoque insuficiente para o produto " + item.getProduto().getNome());
            }
        }
    }

    // lista as vendas da mais nova para a mais antiga, filtrando pelo nome do cliente
    public List<Venda> listar(String nomeCliente) throws SQLException {
        List<Venda> lista = new ArrayList<>();
        String sql = "SELECT v.id, v.total, v.data_venda, c.id AS cliente_id, c.nome, c.endereco, c.telefone "
                   + "FROM venda v INNER JOIN cliente c ON c.id = v.cliente_id "
                   + "WHERE c.nome LIKE ? ORDER BY v.id DESC";

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

                    lista.add(new Venda(
                            rs.getInt("id"),
                            cliente,
                            rs.getDouble("total"),
                            rs.getTimestamp("data_venda").toLocalDateTime()));
                }
            }
        }
        return lista;
    }

    public List<ItemPedido> listarItens(int idVenda) throws SQLException {
        List<ItemPedido> lista = new ArrayList<>();
        String sql = "SELECT i.quantidade, i.subtotal, p.id, p.nome, p.preco, p.quantidade AS estoque, p.estoque_minimo "
                   + "FROM item_venda i INNER JOIN produto p ON p.id = i.produto_id "
                   + "WHERE i.venda_id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenda);

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

    // Cancela a venda: devolve os produtos ao estoque e apaga os itens e a venda
    public void excluir(int idVenda) throws SQLException {
        String sqlEstoque = "UPDATE produto p INNER JOIN item_venda i ON i.produto_id = p.id "
                          + "SET p.quantidade = p.quantidade + i.quantidade WHERE i.venda_id = ?";
        String sqlItens = "DELETE FROM item_venda WHERE venda_id = ?";
        String sqlVenda = "DELETE FROM venda WHERE id = ?";

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                executar(conn, sqlEstoque, idVenda);
                executar(conn, sqlItens, idVenda);
                executar(conn, sqlVenda, idVenda);
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
