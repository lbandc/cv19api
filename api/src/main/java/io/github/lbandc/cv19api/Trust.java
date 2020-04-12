package io.github.lbandc.cv19api;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Table(name = "trusts")
public class Trust {

	@Id
	private String code;

	@UpdateTimestamp
	@Column(name = "last_updated")
	private Instant lastUpdatedUtc;

	private String name;

	@Enumerated(EnumType.STRING)
	private Region region;

	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "date")
	@Column(name = "deaths")
	@CollectionTable(name = "trust_deaths", joinColumns = @JoinColumn(name = "trust_code"))
	Map<LocalDate, Integer> deaths = new TreeMap<>();

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "trust_ingests", joinColumns = @JoinColumn(name = "trust_code"), inverseJoinColumns = @JoinColumn(name = "id"))
	Set<Ingest> sources = new HashSet<>();

}
