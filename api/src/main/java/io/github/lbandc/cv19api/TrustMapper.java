package io.github.lbandc.cv19api;

import org.springframework.stereotype.Component;

@Component(value = "trustMapper")
public class TrustMapper {

	public TrustDto buildTrustDto(String name, String code) {
		TrustDto dto = new TrustDto();
		dto.setName(name);
		dto.setCode(code);
		return dto;
	}

}
