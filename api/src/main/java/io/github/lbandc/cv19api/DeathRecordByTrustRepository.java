package io.github.lbandc.cv19api;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.core.config.Projection;

@RepositoryRestResource
public interface DeathRecordByTrustRepository extends PagingAndSortingRepository<DeathRecordByTrust, String> {

	@Override
	@RestResource(exported = false)
	<S extends DeathRecordByTrust> S save(S entity);

	@Override
	@RestResource(exported = false)
	void delete(DeathRecordByTrust entity);

	DeathRecordByTrust findByTrustAndRecordedOnAndDayOfDeath(Trust trust, LocalDate recordedOn, LocalDate dayOfDeath);

	@RestResource(exported = false)
	@Query(nativeQuery = true, value = "select dr.day_of_death as dayOfDeath , t.name as trust, sum(dr.deaths) as deaths from death_records_by_trust dr"
			+ " left join trusts as t on t.code = dr.trust_id"
			+ " where dr.day_of_death >= :from and dr.day_of_death <= :to" + " group by (dr.day_of_death,t.code)")
	Collection<DeathsByDayAndByTrust> latestDeathsByDayAndByTrust(LocalDate from, LocalDate to);

	@Projection(name = "trustDeathsByDay", types = Trust.class)
	interface DeathsByDayAndByTrust {
		String getTrust();

		LocalDate getDayOfDeath();

		Integer getDeaths();
	}

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