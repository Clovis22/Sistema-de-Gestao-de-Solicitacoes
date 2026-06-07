window.onload = async () => {
    carregarSolicitantes();
    carregarCategorias();
    carregarStatus();
    formatarCampoValor();
};

document.getElementById("formularioCadastro").addEventListener("submit", async function(e) {

    e.preventDefault();

    const body = {

        solicitanteId: document.getElementById("solicitantesFiltro").value,

        categoriaId: document.getElementById("categoriasFiltro").value,

        statusId: document.getElementById("statusFiltro").value,

        descricao: document.getElementById("descricao").value,

        valor: parseFloat(document.getElementById("valor").value.replace(/\./g, "").replace(",", ".")),

        dataSolicitacao: document.getElementById("data").value

    };
    
    if(body.solicitanteId == "" || body.categoriaId == "" || body.statusId == "" || 
       body.descricao == "" || body.valor <= 0 || body.dataSolicitacao == "") {
       alert("Não é possível cadastrar, pois alguns campos estão vazios ou com valores negativos ou igual a zero!");
    }else {

        await fetch(`${API}`, {

            method: "POST",

            headers: {
                "Content-Type":
                "application/json"
            },

            body: JSON.stringify(body)

        });

        alert("Solicitação cadastrado com sucesso!");

        window.location.href = "index.html";

    }
});

async function carregarSolicitantes() {
    try {

        const response = await fetch(`${API}/listarSolicitantes`);

        const listaSolicitantes = await response.json();

        const selectSolicitantes = document.getElementById("solicitantesFiltro");

        listaSolicitantes.forEach(solicitantes => {
            const option = document.createElement("option");
            
            // valor do option
            option.value = solicitantes.id;

            // texto exibido
            option.textContent = solicitantes.nome;

            selectSolicitantes.appendChild(option);

        });

    }catch(error) {
        console.error("Erro ao carregar solicitantes", error);
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

async function carregarStatus() {
    try {

        const response = await fetch(`${API}/listarStatus`);

        listaStatus = await response.json();

        const selectStatus = document.getElementById("statusFiltro");
        
        listaStatus.forEach(status => {
            const option = document.createElement("option");
            
            if(status.descricao === "SOLICITADO") {
               // valor do option
               option.value = status.id;
               
               // texto exibido
               option.textContent = status.descricao;
               
               selectStatus.appendChild(option);
            }

        });

    }catch(error) {
        console.error("Erro ao carregar status", error);
    }
}

async function formatarCampoValor() {
    const campoValor = document.getElementById("valor");

    campoValor.addEventListener("input", function() {

        let valor = this.value.replace(/\D/g, "");

        if(valor === "") {
            this.value = "";
            return;
        }

        valor = Number(valor) / 100;

        this.value = valor.toLocaleString("pt-BR", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
        
    });
}

function voltarPagina() {
    window.location.href = "index.html";
}