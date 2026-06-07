package com.sergipetec.sgs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitante {

    private Integer id;
    private String nome;
    private String cpfcnpj;

}