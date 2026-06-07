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

import com.sergipetec.sgs.dtos.CategoriaDTO.ResponseCategoriaDTO;
import com.sergipetec.sgs.dtos.SolicitacaoDTO.AtualizarStatusDTO;
import com.sergipetec.sgs.dtos.SolicitacaoDTO.CreateSolicitacaoDTO;
import com.sergipetec.sgs.dtos.SolicitacaoDTO.ResponseSolicitacaoDTO;
import com.sergipetec.sgs.dtos.SolicitanteDTO.ResponseSolicitanteDTO;
import com.sergipetec.sgs.dtos.StatusDTO.ResponseStatusDTO;
import com.sergipetec.sgs.service.SgsService;

@RestController
@RequestMapping("/sgs")
public class SgsController {
    
    @Autowired
    private SgsService service;

    @PostMapping
    public void cadastrarsSolicitacao(@RequestBody CreateSolicitacaoDTO objCreateSolicitacao) {
        service.cadastrarSolicitacao(objCreateSolicitacao);
    }

    @GetMapping("/{id}")
    public ResponseSolicitacaoDTO buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/status/categoria/data")
    public List<ResponseSolicitacaoDTO> buscarPorFiltro(

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
        List<ResponseSolicitacaoDTO> listarSolicitacoes = service.listarSolicitacoes();

        Map<String, String> error = new HashMap<>();
        error.put("mensagem", "Solicitações não encontrada!");
        if(listarSolicitacoes == null || listarSolicitacoes.isEmpty())
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        return ResponseEntity.ok(listarSolicitacoes);
    }

    @GetMapping("/listarStatus")
    public List<ResponseStatusDTO> listarStatus() {
        return service.listarStatus();
    }

    @GetMapping("/listarCategorias")
    public List<ResponseCategoriaDTO> listarCategorias() {
        return service.listarCategorias();
    }

    @GetMapping("/listarSolicitantes")
    public List<ResponseSolicitanteDTO> listarSolicitantes() {
        return service.listarSolicitantes();
    }

    @PutMapping("/status")
    public ResponseEntity<?> atualizarStatus(@RequestBody AtualizarStatusDTO objAtualizarStatus) {
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
