package io.github.lbandc.cv19api;

import java.time.Instant;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "deathRecordByTrust", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "trustId", "recordedOn", "dayOfDeath" }) })
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeathRecordByTrust {

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private Instant createdAt;

	@ManyToOne(fetch = FetchType.EAGER)
	private Trust trust;

	private LocalDate recordedOn;

	private LocalDate dayOfDeath;

	private Integer deaths;

	@ManyToOne(fetch = FetchType.EAGER)
	private Ingest source;

}
