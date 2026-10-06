// Painel inicial: mostra os totais e os produtos com estoque baixo

async function carregarPainel() {
    try {
        var clientes = await listarClientes();
        var produtos = await listarProdutos();
        var vendas = await listarVendas();

        document.getElementById("totalClientes").textContent = clientes.length;
        document.getElementById("totalProdutos").textContent = produtos.length;
        document.getElementById("totalVendas").textContent = vendas.length;

        var corpo = document.getElementById("corpoEstoqueBaixo");
        var quantidadeBaixos = 0;

        for (var i = 0; i < produtos.length; i++) {
            if (produtos[i].estoqueBaixo) {
                quantidadeBaixos++;

                var linha = document.createElement("tr");
                linha.appendChild(criarCelula(produtos[i].nome));
                linha.appendChild(criarCelula(produtos[i].quantidade, "numero"));
                linha.appendChild(criarCelula(produtos[i].estoqueMinimo, "numero"));
                corpo.appendChild(linha);
            }
        }

        document.getElementById("totalEstoqueBaixo").textContent = quantidadeBaixos;

        if (quantidadeBaixos === 0) {
            mostrarLinhaVazia(corpo, 3, "Nenhum produto com estoque baixo.");
        }
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
    }
}

iniciarPagina();
carregarPainel();
