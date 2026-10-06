// Consulta de produtos: lista, pesquisa por nome, aviso de estoque baixo e exclusão

// Produtos vindos do servidor; a pesquisa filtra esta lista
var todosOsProdutos = [];

async function carregarProdutos() {
    try {
        todosOsProdutos = await listarProdutos();
    } catch (erro) {
        todosOsProdutos = [];
        mostrarMensagem(erro.message, "erro");
    }
    mostrarTabela();
}

function mostrarTabela() {
    var filtro = document.getElementById("pesquisa").value.trim().toLowerCase();
    var corpo = document.getElementById("corpoTabela");
    var mostrados = 0;

    corpo.innerHTML = "";

    for (var i = 0; i < todosOsProdutos.length; i++) {
        var produto = todosOsProdutos[i];

        if (produto.nome.toLowerCase().indexOf(filtro) === -1) {
            continue;
        }

        corpo.appendChild(criarLinha(produto));
        mostrados++;
    }

    if (mostrados === 0) {
        mostrarLinhaVazia(corpo, 7, "Nenhum produto encontrado.");
    }
}

function criarLinha(produto) {
    var linha = document.createElement("tr");
    linha.appendChild(criarCelula(produto.id));
    linha.appendChild(criarCelula(produto.nome));
    linha.appendChild(criarCelula(formatarMoeda(produto.preco), "numero"));
    linha.appendChild(criarCelula(produto.quantidade, "numero"));
    linha.appendChild(criarCelula(produto.estoqueMinimo, "numero"));

    // Situação do estoque (o servidor já informa se está baixo)
    var situacao = document.createElement("td");
    var etiqueta = document.createElement("span");
    if (produto.estoqueBaixo) {
        etiqueta.textContent = "Estoque baixo";
        etiqueta.className = "etiqueta baixo";
    } else {
        etiqueta.textContent = "OK";
        etiqueta.className = "etiqueta ok";
    }
    situacao.appendChild(etiqueta);
    linha.appendChild(situacao);

    var acoes = document.createElement("td");
    acoes.className = "acoes-linha";

    var linkEditar = document.createElement("a");
    linkEditar.href = "produto-form.html?id=" + produto.id;
    linkEditar.textContent = "Editar";
    linkEditar.className = "botao secundario pequeno";
    acoes.appendChild(linkEditar);
    acoes.appendChild(document.createTextNode(" "));

    var botaoExcluir = criarBotao("Excluir", "botao perigo pequeno", function () {
        confirmarExclusao(produto);
    });
    if (!usuarioEAdmin()) {
        botaoExcluir.disabled = true;
        botaoExcluir.title = "Somente o administrador pode excluir";
    }
    acoes.appendChild(botaoExcluir);

    linha.appendChild(acoes);
    return linha;
}

async function confirmarExclusao(produto) {
    if (!confirm("Excluir o produto " + produto.nome + "?")) {
        return;
    }

    try {
        await excluirProduto(produto.id);
        mostrarMensagem("Produto excluído com sucesso.", "sucesso");
    } catch (erro) {
        // por exemplo: produto que já foi vendido não pode ser excluído
        mostrarMensagem(erro.message, "erro");
    }
    carregarProdutos();
}

function limparFiltro() {
    document.getElementById("pesquisa").value = "";
    mostrarTabela();
}

iniciarPagina();
document.getElementById("pesquisa").addEventListener("input", mostrarTabela);
document.getElementById("btnLimpar").addEventListener("click", limparFiltro);
carregarProdutos();
