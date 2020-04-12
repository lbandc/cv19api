package io.github.lbandc.cv19api;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

	private String source;

	private String name;

	@Enumerated(EnumType.STRING)
	private Region region;

	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "date")
	@Column(name = "deaths")
	@CollectionTable(name = "trust_deaths", joinColumns = @JoinColumn(name = "trust_code"))
	Map<LocalDate, Integer> deaths = new TreeMap<>();

}
