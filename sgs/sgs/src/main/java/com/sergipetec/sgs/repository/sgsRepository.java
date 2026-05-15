package com.sergipetec.sgs.repository;

import java.time.LocalDate;
import java.util.List;

import com.sergipetec.sgs.dtos.categoriaDTO.responseCategoriaDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.createSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.responseSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.responseStatusSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitanteDTO.responseSolicitanteDTO;
import com.sergipetec.sgs.dtos.statusDTO.responseStatusDTO;

public interface sgsRepository {
    
    public void cadastrarSolicitacao(createSolicitacaoDTO requestSolicitacao);
    public responseSolicitacaoDTO buscarPorId(Integer id);
    public responseStatusSolicitacaoDTO buscarStatusPorIdSolicitacao(Integer id);
    public List<responseSolicitacaoDTO> buscarPorFiltro(String status, String categoria, LocalDate dataInicial, LocalDate dataFinal);
    public List<responseSolicitacaoDTO> listarSolicitacoes();
    public List<responseStatusDTO> listarStatus();
    public List<responseCategoriaDTO> listarCategorias();
    public List<responseSolicitanteDTO> listarSolicitantes();
    public void atualizarStatus(Integer id, Integer statusId);

}
