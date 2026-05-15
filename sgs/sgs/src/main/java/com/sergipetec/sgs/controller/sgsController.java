package com.sergipetec.sgs.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sergipetec.sgs.dtos.categoriaDTO.responseCategoriaDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.atualizarStatusDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.createSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.responseSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.responseStatusSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitanteDTO.responseSolicitanteDTO;
import com.sergipetec.sgs.dtos.statusDTO.responseStatusDTO;
import com.sergipetec.sgs.service.sgsService;

@RestController
@RequestMapping("/sgs")
public class sgsController {
    
    @Autowired
    private sgsService service;

    @PostMapping("/cadastrar")
    public void cadastrarsSolicitacao(@RequestBody createSolicitacaoDTO objCreateSolicitacao) {
        service.cadastrarSolicitacao(objCreateSolicitacao);
    }

    @GetMapping("/{id}")
    public responseSolicitacaoDTO buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/solicitacaoId/{id}")
    public responseStatusSolicitacaoDTO buscarStatusPorIdSolicitacao(@PathVariable Integer id) {
       return service.buscarStatusPorIdSolicitacao(id);
    }

    @GetMapping("/status/categoria/data")
    public List<responseSolicitacaoDTO> buscarPorFiltro(

        @RequestParam(required = false) String status,
        @RequestParam(required = false) String categoria,
        
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal

    ) {
        return service.buscarPorFiltro(status, categoria, dataInicial, dataFinal);
    }
    
    @GetMapping("/listar")
    public ResponseEntity<?> listarSolicitacoes() {
        List<responseSolicitacaoDTO> listarSolicitacoes = service.listarSolicitacoes();

        Map<String, String> error = new HashMap<>();
        error.put("mensagem", "Solicitações não encontrada!");
        if(listarSolicitacoes == null || listarSolicitacoes.isEmpty())
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        return ResponseEntity.ok(listarSolicitacoes);
    }

    @GetMapping("/listarStatus")
    public List<responseStatusDTO> listarStatus() {
        return service.listarStatus();
    }

    @GetMapping("/listarCategorias")
    public List<responseCategoriaDTO> listarCategorias() {
        return service.listarCategorias();
    }

    @GetMapping("/listarSolicitantes")
    public List<responseSolicitanteDTO> listarSolicitantes() {
        return service.listarSolicitantes();
    }

    @PutMapping("/status")
    public ResponseEntity<?> atualizarStatus(@RequestBody atualizarStatusDTO objAtualizarStatus) {
        try {

            service.atualizarStatus(objAtualizarStatus);

            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Status da solicitação atualizado com sucesso!");

            return ResponseEntity.ok(response);

        } catch (RuntimeException re) {

            Map<String, String> error = new HashMap<>();
            error.put("mensagem", re.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    
        }
    }
    
}
