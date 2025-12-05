package com.echameunapata.backend.domain.dtos.sponsorship;

import com.echameunapata.backend.domain.dtos.animal.FindAnimalDto;
import com.echameunapata.backend.domain.dtos.person.PersonDto;
import com.echameunapata.backend.domain.enums.sponsorship.SponsorshipStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FindAllSponsorshipsDto {
    private UUID id;

    private BigDecimal monthlyAmount;
    private SponsorshipStatus sponsorshipStatus;

    private String startDate;
    private String endDate;
    private String notes;

    private PersonDto sponsor;
    private FindAnimalDto animal;
}
