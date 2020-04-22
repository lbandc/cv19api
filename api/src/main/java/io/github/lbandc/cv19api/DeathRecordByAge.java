package io.github.lbandc.cv19api;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "death_records_by_age", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "age_range", "recorded_on", "day_of_death" }) })
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeathRecordByAge {

	@Id
	private String id;

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private Instant createdAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "age_range")
	private AgeRange ageRange;

	@Column(name = "recorded_on", updatable = false)
	@NonNull
	private LocalDate recordedOn;

	@Column(name = "day_of_death", updatable = false)
	@NonNull
	private LocalDate dayOfDeath;

	@Column(name = "deaths")
	@NonNull
	private Integer deaths;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "source_id")
	@NonNull
	private Ingest source;

	@PrePersist
	void createId() {
		this.id = UUID.randomUUID().toString();
	}

}
