package io.github.lbandc.cv19api;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.core.config.Projection;

@RepositoryRestResource
public interface DeathRecordByAgeRepository extends PagingAndSortingRepository<DeathRecordByAge, String> {

	@Override
	@RestResource(exported = false)
	<S extends DeathRecordByAge> S save(S entity);

	@Override
	@RestResource(exported = false)
	void delete(DeathRecordByAge entity);

	DeathRecordByAge findByAgeRangeAndRecordedOnAndDayOfDeath(AgeRange ageRange, LocalDate recordedOn,
			LocalDate dayOfDeath);

	@RestResource(exported = false)
	@Query(nativeQuery = true, value = "select dr.age_range as ageRange, dr.day_of_death as date,sum(dr.deaths) as deaths from death_records_by_age dr"
			+ " where dr.day_of_death >= :from and dr.day_of_death <= :to and dr.recorded_on >= :recordedOnFrom and dr.recorded_on <= :recordedOnTo"
			+ " group by (dr.day_of_death, dr.age_range) order by date desc")
	Collection<DeathsByDateAndByAgeRange> getByDate(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("recordedOnFrom") LocalDate recordedOnFrom, @Param("recordedOnTo") LocalDate recordedOnTo);

	@Projection(name = "deathsByDayAndByAgeRange", types = DeathRecordByAge.class)
	interface DeathsByDateAndByAgeRange {

		AgeRange getAgeRange();

		LocalDate getDate();

		Integer getDeaths();
	}

}