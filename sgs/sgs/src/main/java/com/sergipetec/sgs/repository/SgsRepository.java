package com.sergipetec.sgs.repository;

import java.time.LocalDate;
import java.util.List;

import com.sergipetec.sgs.dtos.CategoriaDTO.ResponseCategoriaDTO;
import com.sergipetec.sgs.dtos.SolicitacaoDTO.CreateSolicitacaoDTO;
import com.sergipetec.sgs.dtos.SolicitacaoDTO.ResponseSolicitacaoDTO;
import com.sergipetec.sgs.dtos.SolicitanteDTO.ResponseSolicitanteDTO;
import com.sergipetec.sgs.dtos.StatusDTO.ResponseStatusDTO;

public interface SgsRepository {
    
    public void cadastrarSolicitacao(CreateSolicitacaoDTO requestSolicitacao);
    public ResponseSolicitacaoDTO buscarPorId(Integer id);
    public List<ResponseSolicitacaoDTO> buscarPorFiltro(String status, String categoria, LocalDate dataInicial, LocalDate dataFinal);
    public List<ResponseSolicitacaoDTO> listarSolicitacoes();
    public List<ResponseStatusDTO> listarStatus();
    public List<ResponseCategoriaDTO> listarCategorias();
    public List<ResponseSolicitanteDTO> listarSolicitantes();
    public void atualizarStatus(Integer id, Integer statusId);

}
