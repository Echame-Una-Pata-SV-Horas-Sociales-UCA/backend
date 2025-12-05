package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.enums.animals.AnimalState;
import com.echameunapata.backend.domain.enums.sponsorship.SponsorshipStatus;
import com.echameunapata.backend.repositories.AnimalRepository;
import com.echameunapata.backend.repositories.SponsorshipRepository;
import com.echameunapata.backend.services.contract.IDashboardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements IDashboardService {

    private final AnimalRepository animalRepository;
    private final SponsorshipRepository sponsorshipRepository;

    @Override
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        AnimalRepository animalRepo;
        long perrosEnSantuario = animalRepository.countByState(AnimalState.AVAILABLE);
        long perrosAdoptados = animalRepository.countByAdoptionIsNotNull();
        long padrinosGlobales = sponsorshipRepository.countBySponsorshipStatus(SponsorshipStatus.ACTIVE);

        List<Object[]> rescatesRaw = animalRepository.countRescuesByYear();
        Map<Integer, Integer> rescatesPorAno = rescatesRaw.stream()
                .collect(Collectors.toMap(
                        r -> ((Integer) r[0]),
                        r -> ((Long) r[1]).intValue()
                ));

        metrics.put("perrosEnSantuario", perrosEnSantuario);
        metrics.put("perrosAdoptados", perrosAdoptados);
        metrics.put("padrinosGlobales", padrinosGlobales);
        metrics.put("rescatesPorAno", rescatesPorAno);

        return metrics;
    }
}

