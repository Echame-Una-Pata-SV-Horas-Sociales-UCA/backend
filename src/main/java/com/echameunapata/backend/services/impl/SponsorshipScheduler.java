package com.echameunapata.backend.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SponsorshipScheduler {

    private final SponsorshipServiceImpl sponsorshipService;

    @Scheduled(cron = "0 0 0 * * *") // todos los días a medianoche
    public void checkExpiredSponsorships() {
        sponsorshipService.validSponsorShips();
    }
}

