package com.sergipetec.sgs.dtos;

public record solicitacaoDTO() {

    public record createSolicitacaoDTO(Integer solicitanteId, Integer categoriaId, Integer statusId, String descricao, Float  valor, String dataSolicitacao){}

    public record responseSolicitacaoDTO(Integer id, String nomeDoSolicitante, String cpfcnpj, String nomeCategoria, String nomeStatus, Float  valor){}

    public record responseStatusSolicitacaoDTO(Integer id, String nomeStatus){}

    public record atualizarStatusDTO(Integer id, Integer statusId){}
   
}


