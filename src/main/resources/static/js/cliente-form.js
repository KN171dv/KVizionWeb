// Cadastro e edição de cliente
// Se a URL tiver ?id=..., a página abre em modo de edição.

var idEdicao = lerIdDaUrl();

async function preencherFormulario() {
    if (idEdicao === 0) {
        return; // cadastro novo: formulário em branco
    }

    try {
        var cliente = await buscarCliente(idEdicao);

        document.getElementById("tituloPagina").textContent = "Editar cliente";
        document.getElementById("btnSalvar").textContent = "Salvar alterações";
        document.getElementById("nome").value = cliente.nome;
        document.getElementById("endereco").value = cliente.endereco;
        document.getElementById("telefone").value = cliente.telefone;
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
        idEdicao = 0;
    }
}

// Confere os campos antes de enviar ao servidor. Devolve true quando está tudo certo.
function validarCliente(nome, endereco, telefone) {
    var valido = true;

    if (nome === "") {
        mostrarErroCampo("nome", "Informe o nome do cliente.");
        valido = false;
    } else if (nome.length > 100) {
        mostrarErroCampo("nome", "O nome deve ter no máximo 100 caracteres.");
        valido = false;
    }

    if (endereco.length > 150) {
        mostrarErroCampo("endereco", "O endereço deve ter no máximo 150 caracteres.");
        valido = false;
    }

    // de 8 a 20 caracteres: números, espaço, parênteses, + e -
    var formatoTelefone = /^[0-9()+\- ]{8,20}$/;
    if (telefone === "") {
        mostrarErroCampo("telefone", "Informe o telefone do cliente.");
        valido = false;
    } else if (!formatoTelefone.test(telefone)) {
        mostrarErroCampo("telefone", "Telefone inválido. Exemplo: (21) 99999-0000");
        valido = false;
    }

    return valido;
}

async function salvar(evento) {
    evento.preventDefault();

    limparErrosCampos(["nome", "endereco", "telefone"]);
    limparMensagem();

    var cliente = {
        nome: document.getElementById("nome").value.trim(),
        endereco: document.getElementById("endereco").value.trim(),
        telefone: document.getElementById("telefone").value.trim()
    };

    if (!validarCliente(cliente.nome, cliente.endereco, cliente.telefone)) {
        mostrarMensagem("Corrija os campos marcados.", "erro");
        return;
    }

    try {
        if (idEdicao === 0) {
            await cadastrarCliente(cliente);
            mostrarMensagem("Cliente cadastrado com sucesso.", "sucesso");
            document.getElementById("formCliente").reset();
            document.getElementById("nome").focus();
        } else {
            await atualizarCliente(idEdicao, cliente);
            mostrarMensagem("Cliente atualizado com sucesso.", "sucesso");
        }
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
    }
}

iniciarPagina();
preencherFormulario();
document.getElementById("formCliente").addEventListener("submit", salvar);
