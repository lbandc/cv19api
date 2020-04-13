package io.github.lbandc.cv19api;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
@AllArgsConstructor
public class ApiV1Controller {

	private final TrustRepository trustRepository;
	private final DeathRecordByTrustRepository deathRepository;
	private final FileRetriever fileRetriever;

	@GetMapping("deaths")
	public ResponseWrapper<Collection<DeathRecordByTrustRepository.DailyDeaths>> deathsByDay(
			@RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

		to = todayIfNull(to);
		from = minus30DaysIfNull(from);
		Collection<DeathRecordByTrustRepository.DailyDeaths> dailyDeaths = this.deathRepository.getByDate(from, to);
		return new ResponseWrapper<Collection<DeathRecordByTrustRepository.DailyDeaths>>(dailyDeaths);
	}

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
	public ResponseWrapper<Collection<DeathRecordByTrustRepository.DeathsByDateAndByTrust>> deathsByDayAndByTrust(
			@RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

		to = todayIfNull(to);
		from = minus30DaysIfNull(from);
		Collection<DeathRecordByTrustRepository.DeathsByDateAndByTrust> dailyDeaths = deathRepository
				.getByDateAndByTrust(from, to);
		return new ResponseWrapper<Collection<DeathRecordByTrustRepository.DeathsByDateAndByTrust>>(dailyDeaths);
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

	private static LocalDate minus30DaysIfNull(LocalDate param) {
		return param == null ? LocalDate.now().minusDays(30) : param;
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

	@Getter
	private final static class ResponseWrapper<T> {
		private final T data;
		private Map<String, String> metaData;

		private ResponseWrapper(T content) {
			this.data = content;
			this.metaData = new HashMap<>();
		}

		public static <T> ResponseWrapper<T> of(T content) {
			return new ResponseWrapper<>(content);
		}

		public ResponseWrapper<T> withMetadata(String key, String value) {
			metaData.put(key, value);
			return this;
		}

		public ResponseWrapper<T> withMetadata(Map<String, String> metadata) {
			this.metaData = metadata;
			return this;
		}
	}

}
