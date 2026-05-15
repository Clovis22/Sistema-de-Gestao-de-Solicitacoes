package com.sergipetec.sgs.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sergipetec.sgs.dtos.categoriaDTO.responseCategoriaDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.atualizarStatusDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.createSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.responseSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.responseStatusSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitanteDTO.responseSolicitanteDTO;
import com.sergipetec.sgs.dtos.statusDTO.responseStatusDTO;
import com.sergipetec.sgs.persistence.sgsJDBC;

@Service
public class sgsService {
    
    @Autowired
    private sgsJDBC repositoryJdbc;

    public void cadastrarSolicitacao(createSolicitacaoDTO objCreateSolicitacao) {
        repositoryJdbc.cadastrarSolicitacao(objCreateSolicitacao);
    }

    public responseSolicitacaoDTO buscarPorId(Integer id) {
       return repositoryJdbc.buscarPorId(id);
    }

    public responseStatusSolicitacaoDTO buscarStatusPorIdSolicitacao(Integer id) {
       return repositoryJdbc.buscarStatusPorIdSolicitacao(id);
    }

    public List<responseSolicitacaoDTO> buscarPorFiltro(String status, String categoria, LocalDate dataInicial, LocalDate dataFinal) {
       return repositoryJdbc.buscarPorFiltro(status, categoria, dataInicial, dataFinal);
    }

    public List<responseSolicitacaoDTO> listarSolicitacoes() {
        return repositoryJdbc.listarSolicitacoes();
    }

    public List<responseStatusDTO> listarStatus() {
        return repositoryJdbc.listarStatus();
    }

    public List<responseCategoriaDTO> listarCategorias() {
        return repositoryJdbc.listarCategorias();
    }

    public List<responseSolicitanteDTO> listarSolicitantes() {
        return repositoryJdbc.listarSolicitantes();
    }

    public void atualizarStatus(atualizarStatusDTO objAtualizarStatus) {
        repositoryJdbc.atualizarStatus(objAtualizarStatus.id(), objAtualizarStatus.statusId());
    }

}
