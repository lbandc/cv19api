package io.github.lbandc.cv19api;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Table(name = "trusts")
public class Trust {

	@Id
	private String code;

	private String name;

	@Enumerated(EnumType.STRING)
	private Region region;

}
