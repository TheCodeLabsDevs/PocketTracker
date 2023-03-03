package de.thecodelabs.pockettracker.importer.tvdb_v3;

import com.uwetrottmann.thetvdb.TheTvdb;
import com.uwetrottmann.thetvdb.entities.*;
import de.thecodelabs.pockettracker.administration.apiconfiguration.APIConfigurationService;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.authentication.GeneralConfigurationProperties;
import de.thecodelabs.pockettracker.importer.ImportProcessException;
import de.thecodelabs.pockettracker.importer.ShowImporterService;
import de.thecodelabs.pockettracker.importer.factory.ImporteNotConfiguredException;
import de.thecodelabs.pockettracker.importer.factory.ImporterType;
import de.thecodelabs.pockettracker.importer.model.ShowSearchItem;
import de.thecodelabs.pockettracker.importer.tvdb_v3.converter.SeriesToShowConverter;
import de.thecodelabs.pockettracker.importer.tvdb_v3.converter.TVDBEpisodeToEpisodeConverter;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.show.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@ImporterType(APIType.TVDB_V3)
public class TVDBv3ImporterService implements ShowImporterService
{
	public static final APIType API_TYPE = APIType.TVDB_V3;

	private static final String ARTWORK_BASE_URL = "https://artworks.thetvdb.com/banners";

	private final APIConfigurationService apiConfigurationService;
	private final GeneralConfigurationProperties generalConfigurationProperties;

	private final SeriesToShowConverter showConverter;
	private final TVDBEpisodeToEpisodeConverter episodeConverter;

	@Autowired
	public TVDBv3ImporterService(APIConfigurationService apiConfigurationService, GeneralConfigurationProperties generalConfigurationProperties,
								 SeriesToShowConverter showConverter, TVDBEpisodeToEpisodeConverter episodeConverter)
	{
		this.apiConfigurationService = apiConfigurationService;
		this.generalConfigurationProperties = generalConfigurationProperties;
		this.showConverter = showConverter;
		this.episodeConverter = episodeConverter;
	}

	protected TheTvdb createApiClient() throws ImporteNotConfiguredException
	{
		final Optional<APIConfiguration> apiConfigurationOptional = apiConfigurationService.getConfigurationByType(API_TYPE);
		if(apiConfigurationOptional.isEmpty())
		{
			throw new ImporteNotConfiguredException("APIConfiguration not found " + API_TYPE);
		}
		return new TheTvdb(apiConfigurationOptional.get().getToken());
	}

	@Override
	public List<ShowSearchItem> searchForShow(String search) throws ImporteNotConfiguredException, ImportProcessException, IOException
	{
		final TheTvdb tvdb = createApiClient();

		final Response<SeriesResultsResponse> response = tvdb.search().series(search, null, null, null, generalConfigurationProperties.getLanguage()).execute();
		final SeriesResultsResponse body = response.body();
		if(body == null || body.data == null)
		{
			throw new ImportProcessException("Search request returned invalid data");
		}

		return body.data.stream()
				.map(series -> new ShowSearchItem(series.seriesName, series.firstAired, series.id))
				.toList();
	}

	@Override
	public Show createShow(String identifier) throws ImporteNotConfiguredException, IOException, ImportProcessException
	{
		final TheTvdb tvdb = createApiClient();

		final Response<SeriesResponse> response = tvdb.series().series(Integer.parseInt(identifier), generalConfigurationProperties.getLanguage()).execute();
		final SeriesResponse body = response.body();
		if(body == null || body.data == null)
		{
			throw new ImportProcessException("Series data from TVDB is null");
		}
		final Series series = body.data;

		final Show show = showConverter.toShow(series);
		show.setApiIdentifiers(List.of(new APIIdentifier(API_TYPE, identifier, show)));

		createAllSeasons(tvdb, series.id, show);
		return show;
	}

	private void createAllSeasons(TheTvdb tvdb, Integer seriesId, Show show) throws IOException, ImportProcessException
	{
		final Response<EpisodesSummaryResponse> response = tvdb.series().episodesSummary(seriesId).execute();
		final EpisodesSummaryResponse body = response.body();
		if(body == null || body.data == null || body.data.airedSeasons == null)
		{
			throw new ImportProcessException("Episode summary data from TVDB is null");
		}

		final List<Integer> airedSeasons = body.data.airedSeasons.stream().sorted().toList();
		for(Integer seasonId : airedSeasons)
		{
			createSeasonWithEpisodes(tvdb, seriesId, seasonId, show);
		}
	}

	private void createSeasonWithEpisodes(TheTvdb tvdb, Integer seriesId, int seasonId, Show show) throws IOException, ImportProcessException
	{
		final Season season = new Season("Staffel " + seasonId, "", seasonId, show);
		show.addSeason(season);

		final Response<EpisodesResponse> episodesResponse = tvdb.series().episodesQuery(seriesId, null, seasonId, null, null, null, null, null, null, generalConfigurationProperties.getLanguage()).execute();
		final EpisodesResponse body = episodesResponse.body();
		if(body == null || body.data == null)
		{
			throw new ImportProcessException("Episode data from TVDB is null");
		}

		final List<Episode> episodes = body.data;
		for(Episode episode : episodes)
		{
			season.addEpisode(episodeConverter.toEpisode(episode, season));
		}
	}

	public List<String> getShowPosterImageUrls(Integer identifier) throws ImportProcessException, IOException, ImporteNotConfiguredException
	{
		return getImageUrlsByType(identifier, "poster");
	}

	public List<String> getShowBannerImageUrls(Integer identifier) throws ImportProcessException, IOException, ImporteNotConfiguredException
	{
		return getImageUrlsByType(identifier, "series");
	}

	private List<String> getImageUrlsByType(Integer identifier, String type) throws ImportProcessException, IOException, ImporteNotConfiguredException
	{
		final TheTvdb tvdb = createApiClient();

		// local must be "en" otherwise no images will be found
		final Response<SeriesImageQueryResultResponse> imagesResponse = tvdb.series().imagesQuery(identifier, type, null, null, "en").execute();
		final SeriesImageQueryResultResponse body = imagesResponse.body();
		if(body == null || body.data == null)
		{
			throw new ImportProcessException("Artwork data from TVDB is null");
		}

		final List<String> urls = new ArrayList<>();
		final List<SeriesImageQueryResult> items = body.data;
		for(SeriesImageQueryResult item : items)
		{
			urls.add(MessageFormat.format("{0}/{1}", ARTWORK_BASE_URL, item.fileName));
		}

		return urls;
	}
}
