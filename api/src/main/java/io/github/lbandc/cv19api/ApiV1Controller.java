package io.github.lbandc.cv19api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
@AllArgsConstructor
public class ApiV1Controller {

    private final TrustRepository trustRepository;

    @RequestMapping("deaths")
    public DeathSummaryResponse deathsByTrust(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Collection<TrustRepository.TrustDeaths> deathsByTrust = trustRepository.deathsByTrust(date);
        Map<String, Integer> deathsByTrustMap = deathsByTrust.stream().collect(Collectors.toMap(
                TrustRepository.TrustDeaths::getTrust,
                TrustRepository.TrustDeaths::getDeaths
        ));

        return new DeathSummaryResponse(
                date,
                deathsByTrustMap
        );
    }

    @AllArgsConstructor
    @Getter
    private final static class DeathSummaryResponse {
        private final LocalDate date;
        private final Map<String, Integer> deathsByTrust;

        public int getTotalDeaths() {
            return deathsByTrust.values().stream()
                    .mapToInt(i -> i)
                    .sum();
        }
    }

}
