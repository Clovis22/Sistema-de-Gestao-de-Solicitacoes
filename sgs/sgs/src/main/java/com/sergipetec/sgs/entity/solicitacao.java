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
public class solicitacao {
    
    private Integer id;
    private solicitante solicitante;
    private categoria categoria;
    private status status;
    private String descricao;
    private Float valor; 
    private Date data_solicitacao;
    
}
