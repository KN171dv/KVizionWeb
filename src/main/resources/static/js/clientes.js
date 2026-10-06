// Consulta de clientes: lista, pesquisa por nome e exclusão

// Clientes vindos do servidor; a pesquisa filtra esta lista
var todosOsClientes = [];

async function carregarClientes() {
    try {
        todosOsClientes = await listarClientes();
    } catch (erro) {
        todosOsClientes = [];
        mostrarMensagem(erro.message, "erro");
    }
    mostrarTabela();
}

function mostrarTabela() {
    var filtro = document.getElementById("pesquisa").value.trim().toLowerCase();
    var corpo = document.getElementById("corpoTabela");
    var mostrados = 0;

    corpo.innerHTML = ""; // limpa a tabela antes de preencher de novo

    for (var i = 0; i < todosOsClientes.length; i++) {
        var cliente = todosOsClientes[i];

        if (cliente.nome.toLowerCase().indexOf(filtro) === -1) {
            continue; // não combina com a pesquisa
        }

        corpo.appendChild(criarLinha(cliente));
        mostrados++;
    }

    if (mostrados === 0) {
        mostrarLinhaVazia(corpo, 5, "Nenhum cliente encontrado.");
    }
}

function criarLinha(cliente) {
    var linha = document.createElement("tr");
    linha.appendChild(criarCelula(cliente.id));
    linha.appendChild(criarCelula(cliente.nome));
    linha.appendChild(criarCelula(cliente.endereco));
    linha.appendChild(criarCelula(cliente.telefone));

    var acoes = document.createElement("td");
    acoes.className = "acoes-linha";

    var linkEditar = document.createElement("a");
    linkEditar.href = "cliente-form.html?id=" + cliente.id;
    linkEditar.textContent = "Editar";
    linkEditar.className = "botao secundario pequeno";
    acoes.appendChild(linkEditar);
    acoes.appendChild(document.createTextNode(" "));

    var botaoExcluir = criarBotao("Excluir", "botao perigo pequeno", function () {
        confirmarExclusao(cliente);
    });
    if (!usuarioEAdmin()) {
        botaoExcluir.disabled = true;
        botaoExcluir.title = "Somente o administrador pode excluir";
    }
    acoes.appendChild(botaoExcluir);

    linha.appendChild(acoes);
    return linha;
}

async function confirmarExclusao(cliente) {
    if (!confirm("Excluir o cliente " + cliente.nome + "?")) {
        return;
    }

    try {
        await excluirCliente(cliente.id);
        mostrarMensagem("Cliente excluído com sucesso.", "sucesso");
    } catch (erro) {
        // por exemplo: cliente que já tem venda não pode ser excluído
        mostrarMensagem(erro.message, "erro");
    }
    carregarClientes();
}

function limparFiltro() {
    document.getElementById("pesquisa").value = "";
    mostrarTabela();
}

iniciarPagina();
document.getElementById("pesquisa").addEventListener("input", mostrarTabela);
document.getElementById("btnLimpar").addEventListener("click", limparFiltro);
carregarClientes();
