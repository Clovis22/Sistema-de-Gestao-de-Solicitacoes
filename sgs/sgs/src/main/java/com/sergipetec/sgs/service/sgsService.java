package com.sergipetec.sgs.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sergipetec.sgs.dtos.CategoriaDTO.ResponseCategoriaDTO;
import com.sergipetec.sgs.dtos.SolicitacaoDTO.AtualizarStatusDTO;
import com.sergipetec.sgs.dtos.SolicitacaoDTO.CreateSolicitacaoDTO;
import com.sergipetec.sgs.dtos.SolicitacaoDTO.ResponseSolicitacaoDTO;
import com.sergipetec.sgs.dtos.SolicitanteDTO.ResponseSolicitanteDTO;
import com.sergipetec.sgs.dtos.StatusDTO.ResponseStatusDTO;
import com.sergipetec.sgs.persistence.SgsJDBC;

@Service
public class SgsService {
    
    @Autowired
    private SgsJDBC repositoryJdbc;

    public void cadastrarSolicitacao(CreateSolicitacaoDTO objCreateSolicitacao) {
        repositoryJdbc.cadastrarSolicitacao(objCreateSolicitacao);
    }

    public ResponseSolicitacaoDTO buscarPorId(Integer id) {
       return repositoryJdbc.buscarPorId(id);
    }

    public List<ResponseSolicitacaoDTO> buscarPorFiltro(String status, String categoria, LocalDate dataInicial, LocalDate dataFinal) {
       return repositoryJdbc.buscarPorFiltro(status, categoria, dataInicial, dataFinal);
    }

    public List<ResponseSolicitacaoDTO> listarSolicitacoes() {
        return repositoryJdbc.listarSolicitacoes();
    }

    public List<ResponseStatusDTO> listarStatus() {
        return repositoryJdbc.listarStatus();
    }

    public List<ResponseCategoriaDTO> listarCategorias() {
        return repositoryJdbc.listarCategorias();
    }

    public List<ResponseSolicitanteDTO> listarSolicitantes() {
        return repositoryJdbc.listarSolicitantes();
    }

    public void atualizarStatus(AtualizarStatusDTO objAtualizarStatus) {
        repositoryJdbc.atualizarStatus(objAtualizarStatus.id(), objAtualizarStatus.statusId());
    }

}
