// ==========================================================
// KVizion - funções usadas em todas as páginas internas
// (controle de login, cabeçalho, mensagens e tabelas)
// ==========================================================

function getUsuarioLogado() {
    var texto = sessionStorage.getItem(CHAVE_USUARIO);
    if (texto === null) {
        return null;
    }
    return JSON.parse(texto);
}

function usuarioEAdmin() {
    var usuario = getUsuarioLogado();
    return usuario !== null && usuario.perfil === "ADMIN";
}

// Encerra a sessão no servidor e volta para o login
async function sair() {
    try {
        await chamarApi("POST", "/api/logout");
    } catch (erro) {
        // mesmo se o servidor não responder, sai da tela
    }
    sessionStorage.removeItem(CHAVE_USUARIO);
    window.location.href = "index.html";
}

// Chamada no começo de toda página interna: sem login, volta para a tela de login
function iniciarPagina() {
    var usuario = getUsuarioLogado();
    if (usuario === null) {
        window.location.href = "index.html";
        return;
    }

    document.getElementById("nomeUsuario").textContent = usuario.nome + " (" + usuario.perfil + ")";
    document.getElementById("btnSair").addEventListener("click", sair);
}

// Mostra uma mensagem verde (sucesso) ou vermelha (erro) no topo da página
function mostrarMensagem(texto, tipo) {
    var caixa = document.getElementById("mensagem");
    caixa.textContent = texto;
    caixa.className = "mensagem " + tipo;
}

function limparMensagem() {
    var caixa = document.getElementById("mensagem");
    caixa.textContent = "";
    caixa.className = "mensagem";
}

// Marca um campo como inválido e mostra o texto do erro embaixo dele
function mostrarErroCampo(idCampo, texto) {
    document.getElementById(idCampo).classList.add("invalido");
    document.getElementById("erro-" + idCampo).textContent = texto;
}

function limparErrosCampos(idsCampos) {
    for (var i = 0; i < idsCampos.length; i++) {
        document.getElementById(idsCampos[i]).classList.remove("invalido");
        document.getElementById("erro-" + idsCampos[i]).textContent = "";
    }
}

// Cria uma célula <td> com um texto (usa textContent para não interpretar HTML digitado pelo usuário)
function criarCelula(texto, classe) {
    var celula = document.createElement("td");
    celula.textContent = texto;
    if (classe) {
        celula.className = classe;
    }
    return celula;
}

function criarBotao(texto, classe, aoClicar) {
    var botao = document.createElement("button");
    botao.type = "button";
    botao.textContent = texto;
    botao.className = classe;
    botao.addEventListener("click", aoClicar);
    return botao;
}

// Coloca na tabela uma linha única avisando que não há registros
function mostrarLinhaVazia(corpoTabela, quantidadeColunas, texto) {
    var linha = document.createElement("tr");
    var celula = criarCelula(texto, "vazio");
    celula.colSpan = quantidadeColunas;
    linha.appendChild(celula);
    corpoTabela.appendChild(linha);
}

// 12.5 -> "R$ 12,50"
function formatarMoeda(valor) {
    return valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

// "2026-10-06T18:30:00" -> "06/10/2026 18:30"
function formatarData(textoData) {
    var data = new Date(textoData);
    return data.toLocaleDateString("pt-BR") + " " +
           data.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
}

// Lê o parâmetro ?id= da URL. Devolve 0 quando não existe (cadastro novo).
function lerIdDaUrl() {
    var parametros = new URLSearchParams(window.location.search);
    var id = parseInt(parametros.get("id"), 10);
    if (isNaN(id)) {
        return 0;
    }
    return id;
}
