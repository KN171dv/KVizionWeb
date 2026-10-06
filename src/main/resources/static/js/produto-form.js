// Cadastro e edição de produto
// Se a URL tiver ?id=..., a página abre em modo de edição.

var idEdicao = lerIdDaUrl();

async function preencherFormulario() {
    if (idEdicao === 0) {
        return;
    }

    try {
        var produto = await buscarProduto(idEdicao);

        document.getElementById("tituloPagina").textContent = "Editar produto";
        document.getElementById("btnSalvar").textContent = "Salvar alterações";
        document.getElementById("nome").value = produto.nome;
        document.getElementById("preco").value = produto.preco.toFixed(2).replace(".", ",");
        document.getElementById("quantidade").value = produto.quantidade;
        document.getElementById("estoqueMinimo").value = produto.estoqueMinimo;
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
        idEdicao = 0;
    }
}

// Aceita só números inteiros a partir de zero, como "0", "15", "200"
function textoEInteiro(texto) {
    return /^[0-9]+$/.test(texto);
}

async function salvar(evento) {
    evento.preventDefault();

    limparErrosCampos(["nome", "preco", "quantidade", "estoqueMinimo"]);
    limparMensagem();

    var nome = document.getElementById("nome").value.trim();
    // aceita preço digitado com vírgula: "12,50" vira "12.50"
    var textoPreco = document.getElementById("preco").value.trim().replace(",", ".");
    var textoQuantidade = document.getElementById("quantidade").value.trim();
    var textoMinimo = document.getElementById("estoqueMinimo").value.trim();
    var valido = true;

    if (nome === "") {
        mostrarErroCampo("nome", "Informe o nome do produto.");
        valido = false;
    } else if (nome.length > 100) {
        mostrarErroCampo("nome", "O nome deve ter no máximo 100 caracteres.");
        valido = false;
    }

    var preco = Number(textoPreco);
    if (textoPreco === "" || isNaN(preco)) {
        mostrarErroCampo("preco", "Preço inválido. Exemplo: 12,50");
        valido = false;
    } else if (preco <= 0) {
        mostrarErroCampo("preco", "O preço deve ser maior que zero.");
        valido = false;
    } else if (!/^[0-9]+(\.[0-9]{1,2})?$/.test(textoPreco)) {
        mostrarErroCampo("preco", "O preço deve ter no máximo 2 casas decimais.");
        valido = false;
    }

    if (!textoEInteiro(textoQuantidade)) {
        mostrarErroCampo("quantidade", "Informe o estoque com um número inteiro (zero ou mais).");
        valido = false;
    }

    // estoque mínimo é opcional: em branco vale zero
    if (textoMinimo === "") {
        textoMinimo = "0";
    }
    if (!textoEInteiro(textoMinimo)) {
        mostrarErroCampo("estoqueMinimo", "Informe o estoque mínimo com um número inteiro (zero ou mais).");
        valido = false;
    }

    if (!valido) {
        mostrarMensagem("Corrija os campos marcados.", "erro");
        return;
    }

    var produto = {
        nome: nome,
        preco: preco,
        quantidade: parseInt(textoQuantidade, 10),
        estoqueMinimo: parseInt(textoMinimo, 10)
    };

    try {
        if (idEdicao === 0) {
            await cadastrarProduto(produto);
            mostrarMensagem("Produto cadastrado com sucesso.", "sucesso");
            document.getElementById("formProduto").reset();
            document.getElementById("nome").focus();
        } else {
            await atualizarProduto(idEdicao, produto);
            mostrarMensagem("Produto atualizado com sucesso.", "sucesso");
        }
    } catch (erro) {
        // por exemplo: o servidor recusa nome de produto repetido
        mostrarMensagem(erro.message, "erro");
    }
}

iniciarPagina();
preencherFormulario();
document.getElementById("formProduto").addEventListener("submit", salvar);
