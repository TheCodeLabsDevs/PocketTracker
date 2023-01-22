package de.thecodelabs.pockettracker.importer.tvdb_v3;

import com.uwetrottmann.thetvdb.entities.Series;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowType;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class SeriesToShowConverter
{
	public Show toShow(Series series)
	{
		return new Show(
				series.seriesName,
				series.overview,
				parseDate(series.firstAired),
				null,
				null,
				ShowType.TV,
				isFinished(series.status));
	}

	@Nullable
	private static LocalDate parseDate(String firstAired)
	{
		if(firstAired == null)
		{
			return null;
		}
		return LocalDate.parse(firstAired, DateTimeFormatter.ISO_DATE);
	}

	private static boolean isFinished(String status)
	{
		return "ended".equalsIgnoreCase(status);
	}
}
