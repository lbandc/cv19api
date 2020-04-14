package io.github.lbandc.cv19api;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class TrustDto {
	private String code;
	private String name;

}