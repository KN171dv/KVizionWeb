// Consulta de orçamentos

var todosOsOrcamentos = [];

async function carregarOrcamentos() {
    try {
        todosOsOrcamentos = await listarOrcamentos();
    } catch (erro) {
        todosOsOrcamentos = [];
        mostrarMensagem(erro.message, "erro");
    }
    mostrarTabela();
}

function mostrarTabela() {
    mostrarTabelaPedidos(todosOsOrcamentos, "Excluir", verItens, confirmarExclusao);
}

async function verItens(orcamento) {
    try {
        var itens = await listarItensOrcamento(orcamento.id);
        mostrarItens(orcamento, itens);
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
    }
}

async function confirmarExclusao(orcamento) {
    if (!confirm("Excluir o orçamento nº " + orcamento.id + " de " + orcamento.cliente.nome + "?")) {
        return;
    }

    try {
        await excluirOrcamento(orcamento.id);
        mostrarMensagem("Orçamento excluído com sucesso.", "sucesso");
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
    }
    carregarOrcamentos();
}

function limparFiltro() {
    document.getElementById("pesquisa").value = "";
    mostrarTabela();
}

iniciarPagina();
document.getElementById("pesquisa").addEventListener("input", mostrarTabela);
document.getElementById("btnLimpar").addEventListener("click", limparFiltro);
carregarOrcamentos();
