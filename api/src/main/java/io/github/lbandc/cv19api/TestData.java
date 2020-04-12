package io.github.lbandc.cv19api;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Profile("testData")
@AllArgsConstructor
@Component
public class TestData implements CommandLineRunner {

    private final TrustRepository trustRepository;

    @Override
    public void run(String... args) throws Exception {
        addTrusts();
    }

    public void addTrusts() {
        LocalDate base = LocalDate.of(2020, 04, 11);
        Trust trust = Trust.builder()
                .code("RDD")
                .name("BASILDON AND THURROCK UNIVERSITY HOSPITALS NHS FOUNDATION TRUST")
                .region(Region.EAST_OF_ENGLAND)
                .deaths(Map.of(
                        base.minusDays(7), 30,
                        base.minusDays(6), 34,
                        base.minusDays(5), 38,
                        base.minusDays(4), 46,
                        base.minusDays(3), 68,
                        base.minusDays(2), 102,
                        base.minusDays(1), 174,
                        base, 244
                ))
                .source("https://www.england.nhs.uk/statistics/wp-content/uploads/sites/2/2020/04/COVID-19-daily-announced-deaths-2-April-2020.xlsx")
                .build();

        Trust trust2 = Trust.builder()
                .code("RC1")
                .name("BEDFORD HOSPITAL NHS TRUST")
                .region(Region.EAST_OF_ENGLAND)
                .deaths(Map.of(
                        base.minusDays(7), 30,
                        base.minusDays(6), 34,
                        base.minusDays(5), 38,
                        base.minusDays(4), 46,
                        base.minusDays(3), 68,
                        base.minusDays(2), 102,
                        base.minusDays(1), 174,
                        base, 244
                ))
                .source("https://www.england.nhs.uk/statistics/wp-content/uploads/sites/2/2020/04/COVID-19-daily-announced-deaths-2-April-2020.xlsx")
                .build();

        trustRepository.saveAll(List.of(trust, trust2));
    }
}
