// ==========================================================
// Funções usadas nas páginas "Nova venda" e "Novo orçamento".
// As duas telas montam uma lista de itens e mostram o total;
// a diferença (conferir estoque) fica em venda.js.
// ==========================================================

// Itens do pedido que está sendo montado na tela
var itensDoPedido = [];

// Produtos e clientes carregados do servidor
var produtosDisponiveis = [];
var clientesDisponiveis = [];

async function carregarClientesNoSelect() {
    clientesDisponiveis = await listarClientes();
    var select = document.getElementById("cliente");

    for (var i = 0; i < clientesDisponiveis.length; i++) {
        var opcao = document.createElement("option");
        opcao.value = clientesDisponiveis[i].id;
        opcao.textContent = clientesDisponiveis[i].nome;
        select.appendChild(opcao);
    }
}

// mostrarEstoque = true coloca "(estoque: 10)" ao lado do nome
async function carregarProdutosNoSelect(mostrarEstoque) {
    produtosDisponiveis = await listarProdutos();
    var select = document.getElementById("produto");

    // mantém só a primeira opção ("Selecione...") e recria as outras
    select.length = 1;

    for (var i = 0; i < produtosDisponiveis.length; i++) {
        var produto = produtosDisponiveis[i];
        var opcao = document.createElement("option");
        opcao.value = produto.id;
        opcao.textContent = produto.nome + " - " + formatarMoeda(produto.preco);
        if (mostrarEstoque) {
            opcao.textContent += " (estoque: " + produto.quantidade + ")";
        }
        select.appendChild(opcao);
    }
}

// Procura um produto carregado pelo id. Devolve null se não achar.
function buscarProdutoDisponivel(id) {
    for (var i = 0; i < produtosDisponiveis.length; i++) {
        if (produtosDisponiveis[i].id === id) {
            return produtosDisponiveis[i];
        }
    }
    return null;
}

// Soma os subtotais dos itens
function calcularTotal(itens) {
    var total = 0;
    for (var i = 0; i < itens.length; i++) {
        total += itens[i].subtotal;
    }
    return total;
}

// Soma quanto de um produto já está na lista
function quantidadeNaLista(idProduto) {
    var soma = 0;
    for (var i = 0; i < itensDoPedido.length; i++) {
        if (itensDoPedido[i].produtoId === idProduto) {
            soma += itensDoPedido[i].quantidade;
        }
    }
    return soma;
}

// Lê produto e quantidade do formulário e confere se estão preenchidos.
// Devolve { produto, quantidade } ou null quando há erro.
function lerItemDoFormulario() {
    limparErrosCampos(["produto", "quantidade"]);
    limparMensagem();

    var idProduto = parseInt(document.getElementById("produto").value, 10);
    var textoQuantidade = document.getElementById("quantidade").value.trim();
    var valido = true;

    var produto = buscarProdutoDisponivel(idProduto);
    if (produto === null) {
        mostrarErroCampo("produto", "Selecione um produto.");
        valido = false;
    }

    var quantidade = parseInt(textoQuantidade, 10);
    if (!/^[0-9]+$/.test(textoQuantidade) || quantidade <= 0) {
        mostrarErroCampo("quantidade", "Informe uma quantidade inteira maior que zero.");
        valido = false;
    }

    if (!valido) {
        return null;
    }
    return { produto: produto, quantidade: quantidade };
}

function adicionarItemNaLista(produto, quantidade) {
    itensDoPedido.push({
        produtoId: produto.id,
        nome: produto.nome,
        preco: produto.preco,
        quantidade: quantidade,
        subtotal: produto.preco * quantidade
    });

    document.getElementById("quantidade").value = "";
    document.getElementById("produto").value = "";
    atualizarTabelaItens();
}

function removerItem(posicao) {
    itensDoPedido.splice(posicao, 1);
    atualizarTabelaItens();
}

// Redesenha a tabela de itens e o total
function atualizarTabelaItens() {
    var corpo = document.getElementById("corpoItens");
    corpo.innerHTML = "";

    for (var i = 0; i < itensDoPedido.length; i++) {
        corpo.appendChild(criarLinhaItem(itensDoPedido[i], i));
    }

    if (itensDoPedido.length === 0) {
        mostrarLinhaVazia(corpo, 5, "Nenhum item adicionado.");
    }

    document.getElementById("total").textContent = "Total: " + formatarMoeda(calcularTotal(itensDoPedido));
}

function criarLinhaItem(item, posicao) {
    var linha = document.createElement("tr");
    linha.appendChild(criarCelula(item.nome));
    linha.appendChild(criarCelula(formatarMoeda(item.preco), "numero"));
    linha.appendChild(criarCelula(item.quantidade, "numero"));
    linha.appendChild(criarCelula(formatarMoeda(item.subtotal), "numero"));

    var acoes = document.createElement("td");
    acoes.appendChild(criarBotao("Remover", "botao perigo pequeno", function () {
        removerItem(posicao);
    }));
    linha.appendChild(acoes);

    return linha;
}

// Confere cliente e itens e monta o objeto que vai para o servidor.
// Devolve null quando falta alguma coisa.
function montarPedido() {
    limparErrosCampos(["cliente"]);
    limparMensagem();

    var idCliente = parseInt(document.getElementById("cliente").value, 10);

    if (isNaN(idCliente)) {
        mostrarErroCampo("cliente", "Selecione um cliente.");
        return null;
    }
    if (itensDoPedido.length === 0) {
        mostrarMensagem("Adicione pelo menos um item.", "erro");
        return null;
    }

    // o servidor só precisa do id do produto e da quantidade; preço e total ele mesmo calcula
    var itens = [];
    for (var i = 0; i < itensDoPedido.length; i++) {
        itens.push({ produtoId: itensDoPedido[i].produtoId, quantidade: itensDoPedido[i].quantidade });
    }

    return { clienteId: idCliente, itens: itens };
}

function limparPedido() {
    itensDoPedido = [];
    document.getElementById("cliente").value = "";
    atualizarTabelaItens();
}
