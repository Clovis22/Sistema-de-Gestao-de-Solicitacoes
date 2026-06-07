package com.sergipetec.sgs.dtos;

public record SolicitacaoDTO() {

    public record CreateSolicitacaoDTO(Integer solicitanteId, Integer categoriaId, Integer statusId, String descricao, Float  valor, String dataSolicitacao){}

    public record ResponseSolicitacaoDTO(Integer id, String nomeDoSolicitante, String cpfcnpj, String nomeCategoria, String nomeStatus, Float  valor){}
    
    public record AtualizarStatusDTO(Integer id, Integer statusId){}
   
}


