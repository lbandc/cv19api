package io.github.lbandc.cv19api;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;

@RepositoryRestResource
public interface TrustRepository extends PagingAndSortingRepository<Trust, String> {

    @RestResource(exported = false)
    @Query(nativeQuery = true, value = "select t.name as trust, td.deaths from trust_deaths td " +
            "left join trusts t on td.trust_code = t.code " +
            "where date = ?")
    Collection<TrustDeaths> deathsByTrust(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    @RestResource(exported = false)
    @Query(nativeQuery = true, value = "select t.region as region, sum(td.deaths) as deaths from trust_deaths td " +
            "left join trusts t on td.trust_code = t.code " +
            "where date = ? " +
            "group by region")
    Collection<RegionDeaths> deathsByRegion(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    @RestResource(exported = false)
    @Query(nativeQuery = true, value = "select td.date as date, sum(td.deaths) as deaths from trust_deaths td " +
            "where date >= :from and date <= :to " +
            "group by td.date")
    Collection<DailyDeaths> deathsByDay(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Override
    @RestResource(exported = false)
    <S extends Trust> S save(S s);

    @Override
    @RestResource(exported = false)
    void delete(Trust trust);

    @Projection(name = "trustDeaths", types = Trust.class)
    interface TrustDeaths {
        String getTrust();
        Integer getDeaths();
    }

    @Projection(name = "regionDeaths", types = Trust.class)
    interface RegionDeaths {
        Region getRegion();
        Integer getDeaths();
    }

    @Projection(name = "dailyDeaths", types = Trust.class)
    interface DailyDeaths {
        LocalDate getDate();
        Integer getDeaths();
    }
}
