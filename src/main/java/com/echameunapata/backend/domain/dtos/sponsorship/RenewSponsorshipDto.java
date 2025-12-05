package com.echameunapata.backend.domain.dtos.sponsorship;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RenewSponsorshipDto {

    @NotNull(message = "El monto mensual es obligatorio.")
    @DecimalMin(value = "1.00", inclusive = true, message = "El monto mensual debe ser al menos $1.00.")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 dígitos enteros y 2 decimales.")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal monthlyAmount;

    private String startDate; // Optional: if not provided, uses current date

    private String notes;
}

