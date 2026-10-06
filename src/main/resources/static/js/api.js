// ==========================================================
// KVizion - comunicação com o back-end (API REST em Spring)
// Na etapa anterior os dados ficavam no navegador; agora cada
// função abaixo chama uma rota /api/... do servidor.
// ==========================================================

var CHAVE_USUARIO = "kvizion_usuario";

// Faz uma chamada à API e devolve a resposta já convertida de JSON.
// Quando o servidor responde com erro, lança um Error com a mensagem enviada por ele.
async function chamarApi(metodo, url, dados) {
    var opcoes = { method: metodo, headers: {} };

    if (dados !== undefined) {
        opcoes.headers["Content-Type"] = "application/json";
        opcoes.body = JSON.stringify(dados);
    }

    var resposta = await fetch(url, opcoes);

    // 401 = sem login no servidor (sessão expirou): volta para a tela de login
    if (resposta.status === 401) {
        sessionStorage.removeItem(CHAVE_USUARIO);
        window.location.href = "index.html";
        throw new Error("Faça login para continuar.");
    }

    var texto = await resposta.text();
    var corpo = null;
    if (texto !== "") {
        corpo = JSON.parse(texto);
    }

    if (!resposta.ok) {
        if (corpo !== null && corpo.mensagem) {
            throw new Error(corpo.mensagem);
        }
        throw new Error("Erro ao comunicar com o servidor.");
    }

    return corpo;
}

// ---------- Login ----------

function fazerLogin(login, senha) {
    return chamarApi("POST", "/api/login", { login: login, senha: senha });
}

// ---------- Clientes ----------

function listarClientes() {
    return chamarApi("GET", "/api/clientes");
}

function buscarCliente(id) {
    return chamarApi("GET", "/api/clientes/" + id);
}

function cadastrarCliente(cliente) {
    return chamarApi("POST", "/api/clientes", cliente);
}

function atualizarCliente(id, cliente) {
    return chamarApi("PUT", "/api/clientes/" + id, cliente);
}

function excluirCliente(id) {
    return chamarApi("DELETE", "/api/clientes/" + id);
}

// ---------- Produtos ----------

function listarProdutos() {
    return chamarApi("GET", "/api/produtos");
}

function buscarProduto(id) {
    return chamarApi("GET", "/api/produtos/" + id);
}

function cadastrarProduto(produto) {
    return chamarApi("POST", "/api/produtos", produto);
}

function atualizarProduto(id, produto) {
    return chamarApi("PUT", "/api/produtos/" + id, produto);
}

function excluirProduto(id) {
    return chamarApi("DELETE", "/api/produtos/" + id);
}

// ---------- Vendas ----------

function listarVendas() {
    return chamarApi("GET", "/api/vendas");
}

function listarItensVenda(id) {
    return chamarApi("GET", "/api/vendas/" + id + "/itens");
}

// pedido = { clienteId: 1, itens: [ { produtoId: 2, quantidade: 3 } ] }
function registrarVenda(pedido) {
    return chamarApi("POST", "/api/vendas", pedido);
}

function cancelarVenda(id) {
    return chamarApi("DELETE", "/api/vendas/" + id);
}

// ---------- Orçamentos ----------

function listarOrcamentos() {
    return chamarApi("GET", "/api/orcamentos");
}

function listarItensOrcamento(id) {
    return chamarApi("GET", "/api/orcamentos/" + id + "/itens");
}

function registrarOrcamento(pedido) {
    return chamarApi("POST", "/api/orcamentos", pedido);
}

function excluirOrcamento(id) {
    return chamarApi("DELETE", "/api/orcamentos/" + id);
}
