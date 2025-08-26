package com.crediya.auth.model;

import com.crediya.auth.model.constants.UsuarioConstants;
import com.crediya.auth.model.exception.DatosInvalidosException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("15000000");
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("0.01");
    
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String documentoIdentidad;
    private String telefono;
    private String direccion;
    private Long idRol;
    private BigDecimal salarioBase;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    public Usuario validarSalarioBase() {
        if (salarioBase == null) {
            throw new DatosInvalidosException(UsuarioConstants.SALARIO_BASE_REQUERIDO);
        }
        
        if (salarioBase.compareTo(SALARIO_MINIMO) < 0) {
            throw new DatosInvalidosException(UsuarioConstants.SALARIO_MINIMO_INVALIDO);
        }
        
        if (salarioBase.compareTo(SALARIO_MAXIMO) > 0) {
            throw new DatosInvalidosException(UsuarioConstants.SALARIO_MAXIMO_INVALIDO);
        }
        
        return this;
    }
    
    public boolean tieneSalarioValido() {
        return salarioBase != null 
            && salarioBase.compareTo(SALARIO_MINIMO) >= 0 
            && salarioBase.compareTo(SALARIO_MAXIMO) <= 0;
    }
}
