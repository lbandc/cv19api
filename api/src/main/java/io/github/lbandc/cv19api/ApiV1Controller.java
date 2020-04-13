package io.github.lbandc.cv19api;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.lbandc.cv19api.DeathRecordByTrustRepository.DeathsByDayAndByTrust;
import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
@AllArgsConstructor
public class ApiV1Controller {

	private final TrustRepository trustRepository;
	private final DeathRecordByTrustRepository deathRepository;
	private final FileRetriever fileRetriever;

	@GetMapping("deaths/regions")
	public DeathSummaryResponse<Region> deathsByRegion(
			@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		date = todayIfNull(date);

		Collection<TrustRepository.RegionDeaths> deathsByRegion = trustRepository.deathsByRegion(date);
		Map<Region, Integer> deathsByRegionMap = deathsByRegion.stream().collect(
				Collectors.toMap(TrustRepository.RegionDeaths::getRegion, TrustRepository.RegionDeaths::getDeaths));

		return new DeathSummaryResponse<>(date, deathsByRegionMap);
	}

	@GetMapping("deaths/trusts")
	public Collection<DeathsByDayAndByTrust> deathsByDay(
			@PathVariable(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@PathVariable(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

		to = todayIfNull(to);
		from = todayIfNull(from).minusDays(30);
		Collection<DeathRecordByTrustRepository.DeathsByDayAndByTrust> dailyDeaths = deathRepository
				.latestDeathsByDayAndByTrust(from, to);
		return dailyDeaths;
	}

	@PostMapping("admin/ingests/{fileDate}")
	public CommandResponse ingests(
			@PathVariable(value = "fileDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fileDate) {
		this.fileRetriever.fetch(fileDate, null);
		return CommandResponse.OK();
	}

	private static LocalDate todayIfNull(LocalDate param) {
		return param == null ? LocalDate.now() : param;
	}

	@AllArgsConstructor
	@Getter
	private final static class DeathSummaryResponse<T> {
		private final LocalDate date;
		private final Map<T, Integer> deaths;

		public int getTotalDeaths() {
			return deaths.values().stream().mapToInt(i -> i).sum();
		}
	}

	@AllArgsConstructor
	@Getter
	private final static class CommandResponse {
		private final String status;

		static CommandResponse OK() {
			return new CommandResponse("OK");
		}
	}

}
