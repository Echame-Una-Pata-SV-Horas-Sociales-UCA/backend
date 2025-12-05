package com.echameunapata.backend.services.contract;


import com.echameunapata.backend.domain.dtos.sponsorship.RegisterSponsorshipDto;
import com.echameunapata.backend.domain.dtos.sponsorship.RenewSponsorshipDto;
import com.echameunapata.backend.domain.models.Sponsorship;

import java.util.List;
import java.util.UUID;

public interface ISponsorshipService {
    Sponsorship registerSponsorship(RegisterSponsorshipDto sponsorshipDto);
    Sponsorship renewSponsorship(UUID id, RenewSponsorshipDto renewDto);
    void validSponsorShips();
    List<Sponsorship>findAllSponsorshipByFilters(String statusString);
    Sponsorship findSponsorshipById(UUID id);
}
