package io.github.lbandc.cv19api;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

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

    @ElementCollection
    @MapKeyColumn(name="date")
    @Column(name="deaths")
    @CollectionTable(name="trust_deaths", joinColumns=@JoinColumn(name = "trust_code"))
    Map<LocalDate, Integer> deaths = new TreeMap<>();

}
