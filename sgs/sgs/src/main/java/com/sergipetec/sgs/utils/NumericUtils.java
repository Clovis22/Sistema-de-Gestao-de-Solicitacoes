package com.sergipetec.sgs.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NumericUtils {

    private Boolean isNumeric;

    public boolean isNumeric(String value) {
        if(value == null) return this.isNumeric;
        try {
            Double.parseDouble(value);
            this.isNumeric = true;
            return this.isNumeric;
        } catch(NumberFormatException e) {
            this.isNumeric = false;
            return this.isNumeric;
        }
    }
    
}
