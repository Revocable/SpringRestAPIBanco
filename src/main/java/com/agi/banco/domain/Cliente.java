package com.agi.banco.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")

@Getter
@Setter


@NoArgsConstructor
@AllArgsConstructor
@Builder

@Schema(description = "Entidade que representa um cliente do banco")

public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do cliente", example = "1")
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
    @Column(nullable = false)
    @Schema(description = "Nome completo do cliente", example = "João da Silva")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true)
    @Schema(description = "CPF do cliente", example = "123.456.789-10")
    private String cpf;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(nullable = false, unique = true)
    @Schema(description = "Email do cliente", example = "joao@email.com")
    private String email;

    @Past(message = "Data de nascimento deve ser no passado")
    @Column(nullable = false)
    @Schema(description = "Data de nascimento do cliente", example = "1990-01-01")
    private LocalDate dataNascimento;

    @Column(nullable = true)
    @Size(min = 11, message = "Você deve inserir um telefone válido")
    @Schema(description = "Telefone do cliente", example = "11999999999")
    private String telefone;

    @DecimalMin(value = "0.0", message = "Saldo não pode ser negativo")
    @Column(nullable = false)
    @Schema(description = "Saldo atual da conta do cliente", example = "1000.00")
    private BigDecimal saldo;

    
    @PrePersist
    @PreUpdate
    private void validarDados() {
        if (!cpfValido(this.cpf)) {
            throw new IllegalArgumentException("CPF inválido");
        }
        if (this.telefone != null && !telefoneValido(this.telefone)) {
            throw new IllegalArgumentException("Telefone inválido");
        }
        if(!nascimentoValido(this.dataNascimento)){
            throw new IllegalArgumentException("Você precisa ter mais de 18 anos");
        }
    }

    private boolean cpfValido(String cpf) {
        if (cpf == null || cpf.length() != 14 || !formatoValido(this.cpf)) return false;
        return true;
    }

    //verifica se o cpf contem apenas digitos validos para um cpf (0-9 e . -)
    private boolean formatoValido(String cpf) {
        String digitosValidos = "1234567890.-";
        
        for (char c : cpf.toCharArray()) {
            if (!digitosValidos.contains(String.valueOf(c))) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean telefoneValido(String telefone) {
        if (telefone == null) return true;
        return telefone.length() == 10 || telefone.length() == 11;
    }

    private boolean nascimentoValido(LocalDate nasc) {
        return nasc.plusYears(18).isBefore(LocalDate.now()) || nasc.plusYears(18).isEqual(LocalDate.now());
    }
    
}
