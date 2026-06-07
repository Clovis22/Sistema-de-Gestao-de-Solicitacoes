package com.sergipetec.sgs.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {
    
    private Integer id;
    private Solicitante solicitante;
    private Categoria categoria;
    private Status status;
    private String descricao;
    private Float valor; 
    private Date data_solicitacao;
    
}
