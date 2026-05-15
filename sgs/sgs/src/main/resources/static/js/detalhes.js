window.onload = async () => {
    const params = new URLSearchParams(window.location.search);

    const id = params.get("id");

    const response = await fetch(`${API}/${id}`);

    const s = await response.json();

    document.getElementById("detalhes").innerHTML = `
        <p>
            Nome: ${s.nomeDoSolicitante}
        </p>

        <p>
            CPF/CNPJ: ${s.cpfcnpj}
        </p>

        <p>
            Categoria: ${s.nomeCategoria}
        </p>

        <p>
            Status: ${s.nomeStatus}
        </p>

        <p>
            Valor: ${s.valor}
        </p>
    `;
};

function voltarPagina() {
    window.location.href = "index.html";
}