// Consulta de vendas. Cancelar uma venda devolve os produtos ao estoque.

var todasAsVendas = [];

async function carregarVendas() {
    try {
        todasAsVendas = await listarVendas();
    } catch (erro) {
        todasAsVendas = [];
        mostrarMensagem(erro.message, "erro");
    }
    mostrarTabela();
}

function mostrarTabela() {
    mostrarTabelaPedidos(todasAsVendas, "Cancelar venda", verItens, confirmarCancelamento);
}

async function verItens(venda) {
    try {
        var itens = await listarItensVenda(venda.id);
        mostrarItens(venda, itens);
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
    }
}

async function confirmarCancelamento(venda) {
    if (!confirm("Cancelar a venda nº " + venda.id + " de " + venda.cliente.nome + "?\nOs produtos voltam para o estoque.")) {
        return;
    }

    try {
        await cancelarVenda(venda.id);
        mostrarMensagem("Venda cancelada. O estoque foi devolvido.", "sucesso");
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
    }
    carregarVendas();
}

function limparFiltro() {
    document.getElementById("pesquisa").value = "";
    mostrarTabela();
}

iniciarPagina();
document.getElementById("pesquisa").addEventListener("input", mostrarTabela);
document.getElementById("btnLimpar").addEventListener("click", limparFiltro);
carregarVendas();
