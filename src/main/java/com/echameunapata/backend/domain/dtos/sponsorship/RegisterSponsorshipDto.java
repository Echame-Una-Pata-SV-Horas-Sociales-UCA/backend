package com.echameunapata.backend.domain.dtos.sponsorship;

import com.echameunapata.backend.domain.dtos.person.CreatePersonDto;
import com.echameunapata.backend.domain.models.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class RegisterSponsorshipDto {

    @NotNull(message = "El monto mensual es obligatorio.")
    @DecimalMin(value = "1.00", inclusive = true, message = "El monto mensual debe ser al menos $1.00.")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 dígitos enteros y 2 decimales.")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal monthlyAmount;

    @NotNull(message = "La fecha de inicio es obligatoria.")
    private String startDate;

    private String notes;

    @NotNull(message = "Debe insertar los datos del patrocinador.")
    private CreatePersonDto sponsor;

    @NotNull(message = "Debe seleccionar un animal.")
    private UUID animalId;
}
