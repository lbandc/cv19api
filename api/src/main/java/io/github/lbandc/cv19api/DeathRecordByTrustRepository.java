package io.github.lbandc.cv19api;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface DeathRecordByTrustRepository extends PagingAndSortingRepository<DeathRecordByTrust, String> {

	@Override
	@RestResource(exported = false)
	<S extends DeathRecordByTrust> S save(S entity);

}