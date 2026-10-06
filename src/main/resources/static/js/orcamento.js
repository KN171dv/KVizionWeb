// Novo orçamento: igual à venda, mas não confere nem altera o estoque

function adicionarItem() {
    var item = lerItemDoFormulario();
    if (item === null) {
        return;
    }
    adicionarItemNaLista(item.produto, item.quantidade);
}

async function salvarOrcamento() {
    var pedido = montarPedido();
    if (pedido === null) {
        return;
    }

    try {
        var orcamento = await registrarOrcamento(pedido);

        limparPedido();
        mostrarMensagem("Orçamento gravado. Total: " + formatarMoeda(orcamento.total), "sucesso");
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
    }
}

async function iniciarOrcamento() {
    try {
        await carregarClientesNoSelect();
        await carregarProdutosNoSelect(false);
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
    }
    atualizarTabelaItens();
}

iniciarPagina();
iniciarOrcamento();
document.getElementById("btnAdicionar").addEventListener("click", adicionarItem);
document.getElementById("btnFinalizar").addEventListener("click", salvarOrcamento);
