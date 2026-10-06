// Página de login: envia login e senha para o servidor conferir no banco

async function entrar(evento) {
    evento.preventDefault(); // não deixa o formulário recarregar a página

    limparErrosCampos(["login", "senha"]);
    limparMensagem();

    var login = document.getElementById("login").value.trim();
    var senha = document.getElementById("senha").value;
    var valido = true;

    if (login === "") {
        mostrarErroCampo("login", "Informe o login.");
        valido = false;
    }
    if (senha === "") {
        mostrarErroCampo("senha", "Informe a senha.");
        valido = false;
    }
    if (!valido) {
        return;
    }

    try {
        var usuario = await fazerLogin(login, senha);

        // guarda nome e perfil só para mostrar no cabeçalho; quem controla o acesso é o servidor
        sessionStorage.setItem(CHAVE_USUARIO, JSON.stringify({ nome: usuario.nome, perfil: usuario.perfil }));
        window.location.href = "menu.html";
    } catch (erro) {
        mostrarMensagem(erro.message, "erro");
        document.getElementById("senha").value = "";
        document.getElementById("senha").focus();
    }
}

document.getElementById("formLogin").addEventListener("submit", entrar);
