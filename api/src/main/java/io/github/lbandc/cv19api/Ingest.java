package io.github.lbandc.cv19api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Table(name = "ingests")
@EqualsAndHashCode(of = "url")
public class Ingest {

    public Ingest(String url, Instant timestamp) {
        this.url = url;
        this.timestamp = timestamp;
        this.id = UUID.randomUUID().toString();
    }

    @Id
    private String id;

    private String url;

    @UpdateTimestamp
    @Column(name = "timestamp")
    private Instant timestamp;

}
