package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.domain.dtos.sponsorship.FindAllSponsorshipsDto;
import com.echameunapata.backend.domain.dtos.sponsorship.FindSponsorshipDto;
import com.echameunapata.backend.domain.dtos.sponsorship.RegisterSponsorshipDto;
import com.echameunapata.backend.domain.models.Sponsorship;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.ISponsorshipService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.base-path}/sponsorship")
@AllArgsConstructor
public class SponsorshipController {

    private final ISponsorshipService sponsorshipService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> registerSponsorship(@RequestBody @Valid RegisterSponsorshipDto sponsorshipDto) {
        try {
            var sponsorship = sponsorshipService.registerSponsorship(sponsorshipDto);
            FindSponsorshipDto sponsorshipDtoResponse = modelMapper.map(sponsorship, FindSponsorshipDto.class);
            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success", sponsorshipDtoResponse);
        } catch (HttpError e) {
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-all")
    public ResponseEntity<GeneralResponse> finAllSponsorship(@RequestParam(required = false) String status) {
        try {
            List<Sponsorship> sponsorships = sponsorshipService.findAllSponsorshipByFilters(status);
            List<FindAllSponsorshipsDto> sponsorshipDtos = sponsorships.stream().map(sponsorship -> modelMapper.map(sponsorship, FindAllSponsorshipsDto.class)).toList();

            return GeneralResponse.getResponse(HttpStatus.OK, "Success all sponsorships", sponsorshipDtos);
        } catch (HttpError e) {
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<GeneralResponse> findById(@PathVariable UUID id) {
        try {
            Sponsorship sponsorship = sponsorshipService.findSponsorshipById(id);
            FindSponsorshipDto sponsorshipDto = modelMapper.map(sponsorship, FindSponsorshipDto.class);

            return GeneralResponse.getResponse(HttpStatus.OK, "Success sponsorship", sponsorshipDto);
        } catch (HttpError e) {
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }
}
