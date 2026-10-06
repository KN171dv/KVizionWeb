// Nova venda: confere o estoque ao adicionar; o servidor confere de novo e dá baixa ao finalizar

function adicionarItem() {
    var item = lerItemDoFormulario();
    if (item === null) {
        return;
    }

    // Regra de negócio: não vende mais do que existe em estoque,
    // contando o que já foi colocado na lista para o mesmo produto.
    var jaNaLista = quantidadeNaLista(item.produto.id);
    if (jaNaLista + item.quantidade > item.produto.quantidade) {
        mostrarErroCampo("quantidade", "Estoque insuficiente. Em estoque: " + item.produto.quantidade +
                ", já na venda: " + jaNaLista + ".");
        return;
    }

    adicionarItemNaLista(item.produto, item.quantidade);
}

async function finalizarVenda() {
    var pedido = montarPedido();
    if (pedido === null) {
        return;
    }

    try {
        var venda = await registrarVenda(pedido);

        limparPedido();
        await carregarProdutosNoSelect(true); // mostra o estoque já atualizado
        mostrarMensagem("Venda finalizada. Total: " + formatarMoeda(venda.total), "sucesso");
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
    }
}

async function iniciarVenda() {
    try {
        await carregarClientesNoSelect();
        await carregarProdutosNoSelect(true);
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
    }
    atualizarTabelaItens();
}

iniciarPagina();
iniciarVenda();
document.getElementById("btnAdicionar").addEventListener("click", adicionarItem);
document.getElementById("btnFinalizar").addEventListener("click", finalizarVenda);
