package com.kvizion.service;

import com.kvizion.dao.ClienteDAO;
import java.sql.SQLException;
import java.util.List;
import com.kvizion.model.Cliente;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private ClienteDAO clienteDAO = new ClienteDAO();

    public void cadastrar(Cliente cliente) throws RegraNegocioException, SQLException {
        validar(cliente);
        clienteDAO.inserir(cliente);
    }

    public void atualizar(Cliente cliente) throws RegraNegocioException, SQLException {
        buscarPorId(cliente.getId()); // confere se o cliente existe
        validar(cliente);
        clienteDAO.atualizar(cliente);
    }

    public void excluir(int id) throws RegraNegocioException, SQLException {
        if (clienteDAO.possuiMovimentacao(id)) {
            throw new RegraNegocioException("Este cliente possui vendas ou orçamentos e não pode ser excluído.");
        }
        clienteDAO.excluir(id);
    }

    public Cliente buscarPorId(int id) throws RegraNegocioException, SQLException {
        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente == null) {
            throw new RegraNegocioException("Cliente não encontrado.");
        }
        return cliente;
    }

    public List<Cliente> pesquisar(String nome) throws SQLException {
        return clienteDAO.pesquisar(nome);
    }

    public void validar(Cliente cliente) throws RegraNegocioException {
        String nome = cliente.getNome();
        String endereco = cliente.getEndereco();
        String telefone = cliente.getTelefone();

        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("Informe o nome do cliente.");
        }
        if (nome.length() > 100) {
            throw new RegraNegocioException("O nome deve ter no máximo 100 caracteres.");
        }
        if (endereco != null && endereco.length() > 150) {
            throw new RegraNegocioException("O endereço deve ter no máximo 150 caracteres.");
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new RegraNegocioException("Informe o telefone do cliente.");
        }
        // de 8 a 20 caracteres: numeros, espaco, parenteses, + e -
        if (!telefone.matches("[0-9()+\\- ]{8,20}")) {
            throw new RegraNegocioException("Telefone inválido.");
        }
    }
}
