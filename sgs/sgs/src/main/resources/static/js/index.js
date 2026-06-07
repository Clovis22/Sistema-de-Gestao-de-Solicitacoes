window.onload = async () => {
    const navegacao = performance.getEntriesByType("navigation");
    const origemButton = sessionStorage.getItem("origemButton");
    
    if((navegacao.length > 0 || navegacao[0].type === "reload" || navegacao[0].type === "navigate") && 
       (origemButton !== "detalhes" && origemButton !== "cadastro"))
        sessionStorage.clear();
       
    sessionStorage.setItem("origemButton", "");

    await carregarStatus();
    await carregarCategorias();
    
    const status = sessionStorage.getItem("statusFiltro");
    const categoria = sessionStorage.getItem("categoriasFiltro");
    const dataInicial = sessionStorage.getItem("dataInicialFiltro");
    const dataFinal = sessionStorage.getItem("dataFinalFiltro");

    if(status)
        document.getElementById("statusFiltro").value = status;

    if(categoria)
        document.getElementById("categoriasFiltro").value = categoria;

    if(dataInicial)
        document.getElementById("dataInicial").value = dataInicial;

    if(dataFinal)
        document.getElementById("dataFinal").value = dataFinal;

    if(status || categoria || dataInicial || dataFinal)
        await filtrar();
    else
        await listarSolicitacoes();
};

function irParaCadastro() {
    sessionStorage.setItem("origemButton", "cadastro");
    window.location.href = "cadastro.html";
}

async function listarSolicitacoes() {
    const response = await fetch(`${API}/listar`);

    const dados = await response.json();
    
    if(!response.ok)
        alert(dados.mensagem);
    else 
        montarTabela(dados);
}

async function carregarStatus() {
    try {

        const response = await fetch(`${API}/listarStatus`);

        const listaStatus = await response.json();

        const selectStatus = document.getElementById("statusFiltro");

        listaStatus.forEach(status => {
            const option = document.createElement("option");

            // valor do option
            option.value = status.id;

            // texto exibido
            option.textContent = status.descricao;

            selectStatus.appendChild(option);
        });

    }catch(error) {
        console.error("Erro ao carregar status", error);
    }
}

async function carregarCategorias() {
    try {

        const response = await fetch(`${API}/listarCategorias`);

        const listaCategorias = await response.json();

        const selectCategorias = document.getElementById("categoriasFiltro");

        listaCategorias.forEach(categorias => {
            const option = document.createElement("option");

            // valor do option
            option.value = categorias.id;

            // texto exibido
            option.textContent = categorias.nome;

            selectCategorias.appendChild(option);

        });

    }catch(error) {
        console.error("Erro ao carregar categorias", error);
    }
}

async function montarTabela(lista) {
    const tbody = document.getElementById("tbodySolicitacoes");

    tbody.innerHTML = "";

    const response = await fetch(`${API}/listarStatus`);

    const listaStatus = await response.json();

    lista.forEach(s => {
        let desabilitado = s.nomeStatus === "REJEITADO" || s.nomeStatus === "CANCELADO";

        let options = "";
        
        listaStatus.forEach(status => {
            options += `
                <option value="${status.id}" ${status.descricao === s.nomeStatus ? "selected" : ""}>
                    ${status.descricao}
                </option>
            `;
        });

        tbody.innerHTML += `
            <tr>

                <td>${s.nomeDoSolicitante}</td>

                <td>${s.cpfcnpj}</td>

                <td>${s.nomeCategoria}</td>

                <td>
                    <select onchange="alterarStatus(${s.id}, this.value)" ${desabilitado ? "disabled" : ""}>
                        ${options}
                    </select>
                </td>

                <td>${formatarMoeda(s.valor)}</td>

                <td>

                    <button onclick="detalhes(${s.id})">
                        Detalhes
                    </button>

                </td>

            </tr>
        `;

    });
}

async function filtrar() {
    const status = document.getElementById("statusFiltro").value;

    const categoria = document.getElementById("categoriasFiltro").value;

    const dataInicial = document.getElementById("dataInicial").value;

    const dataFinal = document.getElementById("dataFinal").value;

    sessionStorage.setItem("statusFiltro", status);
    sessionStorage.setItem("categoriasFiltro", categoria);
    sessionStorage.setItem("dataInicialFiltro", dataInicial);
    sessionStorage.setItem("dataFinalFiltro", dataFinal);

    const response = await fetch(
        `${API}/status/categoria/data?status=${status}&categoria=${categoria}&dataInicial=${dataInicial}&dataFinal=${dataFinal}`
    );

    const dados = await response.json();

    montarTabela(dados);
}

async function alterarStatus(id, statusId) {
    const responseAtualizarStatus = await fetch(`${API}/status`, {
        method: "PUT",
        headers: {
            "Content-Type":
            "application/json"
        },
        body: JSON.stringify({
            "id": id,
            "statusId": statusId
        })
    });
    
    const dadosResponseAtualizarStatus = await responseAtualizarStatus.json();
    alert(dadosResponseAtualizarStatus.mensagem);
    listarSolicitacoes();
}

function formatarMoeda(valor) {
    return Number(valor).toLocaleString('pt-BR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

function detalhes(id) {
    sessionStorage.setItem("origemButton", "detalhes");
    window.location.href = `detalhes.html?id=${id}`;
}