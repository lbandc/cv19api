package io.github.lbandc.cv19api;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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
	@Query(nativeQuery = true, value = "select dr.day_of_death as date,sum(dr.deaths) as deaths from death_records_by_trust dr"
			+ " where dr.day_of_death >= :from and dr.day_of_death <= :to and dr.recorded_on >= :recordedOnFrom and dr.recorded_on <= :recordedOnTo"
			+ " group by (dr.day_of_death) order by date desc")
	Collection<DailyDeaths> getByDate(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("recordedOnFrom") LocalDate recordedOnFrom, @Param("recordedOnTo") LocalDate recordedOnTo);

	@RestResource(exported = false)
	@Query(nativeQuery = true, value = "select dr.day_of_death as date , t.name as trustName, t.code as trustCode, sum(dr.deaths) as deaths from death_records_by_trust dr"
			+ " left join trusts as t on t.code = dr.trust_id"
			+ " where dr.day_of_death >= :from and dr.day_of_death <= :to and dr.recorded_on >= :recordedOnFrom and dr.recorded_on <= :recordedOnTo"
			+ " group by (dr.day_of_death,t.code)" + " order by date desc, t.code")
	Collection<DeathsByDateAndByTrust> getByDateAndByTrust(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("recordedOnFrom") LocalDate recordedOnFrom, @Param("recordedOnTo") LocalDate recordedOnTo);

	@RestResource(exported = false)
	@Query(nativeQuery = true, value = "select dr.day_of_death as date ,t.region as region, sum(dr.deaths) as deaths from death_records_by_trust dr"
			+ " left join trusts as t on t.code = dr.trust_id"
			+ " where dr.day_of_death >= :from and dr.day_of_death <= :to and dr.recorded_on >= :recordedOnFrom and dr.recorded_on <= :recordedOnTo"
			+ " group by (dr.day_of_death,t.region)" + " order by date desc, region")
	Collection<DeathsByDateAndByRegion> getByDateAndByRegion(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("recordedOnFrom") LocalDate recordedOnFrom, @Param("recordedOnTo") LocalDate recordedOnTo);

	@Projection(name = "deathsByDayAndByTrust", types = DeathRecordByTrust.class)
	interface DeathsByDateAndByTrust {

		@Value("#{@trustMapper.buildTrustDto(target.trustName, target.trustCode)}")
		TrustDto getTrust();

		LocalDate getDate();

		Integer getDeaths();
	}

	@Projection(name = "deathsByDayAndByRegion", types = DeathRecordByTrust.class)
	interface DeathsByDateAndByRegion {

		String getRegion();

		LocalDate getDate();

		Integer getDeaths();
	}

	@Projection(name = "regionDeaths", types = DeathRecordByTrust.class)
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