package com.kvizion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.kvizion.model.Produto;

public class ProdutoDAO {

    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, preco, quantidade, estoque_minimo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setInt(4, produto.getEstoqueMinimo());
            stmt.executeUpdate();
        }
    }

    public List<Produto> listar() throws SQLException {
        return pesquisar("");
    }

    public List<Produto> pesquisar(String nome) throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE nome LIKE ? ORDER BY nome";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarProduto(rs));
                }
            }
        }
        return lista;
    }

    // devolve null quando nao existe produto com esse id
    public Produto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM produto WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarProduto(rs);
                }
            }
        }
        return null;
    }

    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, preco = ?, quantidade = ?, estoque_minimo = ? WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setInt(4, produto.getEstoqueMinimo());
            stmt.setInt(5, produto.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // idIgnorar e o id do produto que esta sendo editado (0 se for novo)
    public boolean existeNome(String nome, int idIgnorar) throws SQLException {
        String sql = "SELECT COUNT(*) FROM produto WHERE nome = ? AND id <> ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setInt(2, idIgnorar);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    // verifica se o produto ja apareceu em alguma venda ou orcamento
    public boolean possuiMovimentacao(int id) throws SQLException {
        String sql = "SELECT (SELECT COUNT(*) FROM item_venda WHERE produto_id = ?) + "
                   + "(SELECT COUNT(*) FROM item_orcamento WHERE produto_id = ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setInt(2, id);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private Produto montarProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getDouble("preco"),
                rs.getInt("quantidade"),
                rs.getInt("estoque_minimo"));
    }
}
