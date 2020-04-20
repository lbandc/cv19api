package io.github.lbandc.cv19api;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

	private final DeathRecordByTrustRepository deathRepository;
	private final FileRetriever fileRetriever;

	@GetMapping("deaths")
	public ResponseWrapper<Collection<DeathRecordByTrustRepository.DailyDeaths>> deathsByDay(
			@RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(value = "recordedOnFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordedOnFrom,
			@RequestParam(value = "recordedOnTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordedOnTo) {

		to = todayIfNull(to);
		from = minus30DaysIfNull(from);
		recordedOnTo = todayIfNull(recordedOnTo);
		recordedOnFrom = null == recordedOnFrom ? LocalDate.of(2020, 01, 01) : recordedOnFrom;
		Collection<DeathRecordByTrustRepository.DailyDeaths> dailyDeaths = this.deathRepository.getByDate(from, to,
				recordedOnFrom, recordedOnTo);
		return new ResponseWrapper<>(dailyDeaths).withMetadata("from", from.toString())
				.withMetadata("to", to.toString()).withMetadata("recordedOnFrom", recordedOnFrom.toString())
				.withMetadata("recordedOnTo", recordedOnTo.toString());
	}

	@GetMapping("deaths/regions")
	public ResponseWrapper<Collection<DeathRecordByTrustRepository.DeathsByDateAndByRegion>> deathsByDateAndByRegion(
			@RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(value = "recordedOnFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordedOnFrom,
			@RequestParam(value = "recordedOnTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordedOnTo) {
		to = todayIfNull(to);
		from = minus30DaysIfNull(from);
		recordedOnTo = todayIfNull(recordedOnTo);
		recordedOnFrom = null == recordedOnFrom ? LocalDate.of(2020, 01, 01) : recordedOnFrom;
		Collection<DeathRecordByTrustRepository.DeathsByDateAndByRegion> dailyDeaths = deathRepository
				.getByDateAndByRegion(from, to, recordedOnFrom, recordedOnTo);
		return new ResponseWrapper<>(dailyDeaths).withMetadata("from", from.toString())
				.withMetadata("to", to.toString()).withMetadata("recordedOnFrom", recordedOnFrom.toString())
				.withMetadata("recordedOnTo", recordedOnTo.toString());
	}

	@GetMapping("deaths/trusts")
	public ResponseWrapper<Collection<DeathRecordByTrustRepository.DeathsByDateAndByTrust>> deathsByDayAndByTrust(
			@RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@RequestParam(value = "recordedOnFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordedOnFrom,
			@RequestParam(value = "recordedOnTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordedOnTo) {

		to = todayIfNull(to);
		from = minus30DaysIfNull(from);
		recordedOnTo = todayIfNull(recordedOnTo);
		recordedOnFrom = null == recordedOnFrom ? LocalDate.of(2020, 01, 01) : recordedOnFrom;
		Collection<DeathRecordByTrustRepository.DeathsByDateAndByTrust> dailyDeaths = deathRepository
				.getByDateAndByTrust(from, to, recordedOnFrom, recordedOnTo);
		return new ResponseWrapper<>(dailyDeaths).withMetadata("from", from.toString())
				.withMetadata("to", to.toString()).withMetadata("recordedOnFrom", recordedOnFrom.toString())
				.withMetadata("recordedOnTo", recordedOnTo.toString());
	}

	@PostMapping("admin/ingests/{fileDate}")
	public CommandResponse ingests(
			@PathVariable(value = "fileDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fileDate,
			@RequestParam(value = "force", required = false) boolean force) {
		this.fileRetriever.fetchAsync(fileDate, null);
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
