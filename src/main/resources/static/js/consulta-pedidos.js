// ==========================================================
// Funções usadas nas consultas de vendas e de orçamentos:
// mostram os registros, filtram pelo nome do cliente e mostram
// os itens do registro escolhido.
// ==========================================================

// lista: vendas ou orçamentos vindos do servidor (já do mais novo para o mais antigo)
// textoBotao: texto do botão de exclusão ("Cancelar venda" ou "Excluir")
// aoVerItens e aoExcluir: funções chamadas quando o usuário clica nos botões da linha
function mostrarTabelaPedidos(lista, textoBotao, aoVerItens, aoExcluir) {
    var filtro = document.getElementById("pesquisa").value.trim().toLowerCase();
    var corpo = document.getElementById("corpoTabela");
    var mostrados = 0;

    corpo.innerHTML = "";
    esconderItens();

    for (var i = 0; i < lista.length; i++) {
        if (lista[i].cliente.nome.toLowerCase().indexOf(filtro) === -1) {
            continue;
        }
        corpo.appendChild(criarLinhaPedido(lista[i], textoBotao, aoVerItens, aoExcluir));
        mostrados++;
    }

    if (mostrados === 0) {
        mostrarLinhaVazia(corpo, 5, "Nenhum registro encontrado.");
    }
}

function criarLinhaPedido(pedido, textoBotao, aoVerItens, aoExcluir) {
    var linha = document.createElement("tr");
    linha.appendChild(criarCelula(pedido.id));
    linha.appendChild(criarCelula(formatarData(pedido.data)));
    linha.appendChild(criarCelula(pedido.cliente.nome));
    linha.appendChild(criarCelula(formatarMoeda(pedido.total), "numero"));

    var acoes = document.createElement("td");
    acoes.className = "acoes-linha";
    acoes.appendChild(criarBotao("Ver itens", "botao secundario pequeno", function () {
        aoVerItens(pedido);
    }));
    acoes.appendChild(document.createTextNode(" "));

    var botaoExcluir = criarBotao(textoBotao, "botao perigo pequeno", function () {
        aoExcluir(pedido);
    });
    if (!usuarioEAdmin()) {
        botaoExcluir.disabled = true;
        botaoExcluir.title = "Somente o administrador pode fazer isso";
    }
    acoes.appendChild(botaoExcluir);

    linha.appendChild(acoes);
    return linha;
}

// itens: lista devolvida pelo servidor, cada um com produto, quantidade e subtotal
function mostrarItens(pedido, itens) {
    var corpo = document.getElementById("corpoItens");
    corpo.innerHTML = "";

    for (var i = 0; i < itens.length; i++) {
        var linha = document.createElement("tr");
        // preço unitário cobrado = subtotal gravado dividido pela quantidade
        var precoUnitario = itens[i].subtotal / itens[i].quantidade;

        linha.appendChild(criarCelula(itens[i].produto.nome));
        linha.appendChild(criarCelula(formatarMoeda(precoUnitario), "numero"));
        linha.appendChild(criarCelula(itens[i].quantidade, "numero"));
        linha.appendChild(criarCelula(formatarMoeda(itens[i].subtotal), "numero"));
        corpo.appendChild(linha);
    }

    document.getElementById("tituloItens").textContent = "Itens do nº " + pedido.id + " - " + pedido.cliente.nome;
    document.getElementById("cartaoItens").style.display = "block";
}

function esconderItens() {
    document.getElementById("cartaoItens").style.display = "none";
}
