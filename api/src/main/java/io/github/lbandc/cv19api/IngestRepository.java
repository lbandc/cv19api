package io.github.lbandc.cv19api;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface IngestRepository extends PagingAndSortingRepository<Ingest, String> {

	@Query("select case when count(i) > 0 then true else false end from Ingest i where i.url = :url")
	boolean existsByUrl(@Param("url") String url);

	Ingest findByUrl(String url);

	@Override
	@RestResource(exported = false)
	void delete(Ingest ingest);

	@Override
	@RestResource(exported = false)
	<S extends Ingest> S save(S s);
}
