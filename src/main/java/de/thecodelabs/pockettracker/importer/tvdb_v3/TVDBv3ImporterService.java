package de.thecodelabs.pockettracker.importer.tvdb_v3;

import com.uwetrottmann.thetvdb.TheTvdb;
import com.uwetrottmann.thetvdb.entities.*;
import de.thecodelabs.pockettracker.administration.apiconfiguration.APIConfigurationService;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.authentication.GeneralConfigurationProperties;
import de.thecodelabs.pockettracker.importer.ImportProcessException;
import de.thecodelabs.pockettracker.importer.MovieImporterService;
import de.thecodelabs.pockettracker.importer.ShowImporterService;
import de.thecodelabs.pockettracker.importer.factory.ImporterNotConfiguredException;
import de.thecodelabs.pockettracker.importer.factory.ImporterType;
import de.thecodelabs.pockettracker.importer.model.MovieSearchItem;
import de.thecodelabs.pockettracker.importer.model.ShowSearchItem;
import de.thecodelabs.pockettracker.importer.tvdb_v3.converter.SeriesToShowConverter;
import de.thecodelabs.pockettracker.importer.tvdb_v3.converter.TVDBEpisodeToEpisodeConverter;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.show.controller.EpisodeInfo;
import de.thecodelabs.pockettracker.show.controller.SeasonInfo;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.show.model.Show;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@ImporterType(APIType.TVDB_V3)
@RequiredArgsConstructor
public class TVDBv3ImporterService implements ShowImporterService, MovieImporterService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TVDBv3ImporterService.class);

	public static final APIType API_TYPE = APIType.TVDB_V3;

	private static final String ARTWORK_BASE_URL = "https://artworks.thetvdb.com/banners";

	private final APIConfigurationService apiConfigurationService;
	private final GeneralConfigurationProperties generalConfigurationProperties;

	private final SeriesToShowConverter showConverter;
	private final TVDBEpisodeToEpisodeConverter episodeConverter;

	private final MessageSource messageSource;

	protected TheTvdb createApiClient() throws ImporterNotConfiguredException
	{
		final Optional<APIConfiguration> apiConfigurationOptional = apiConfigurationService.getConfigurationByType(API_TYPE);
		if(apiConfigurationOptional.isEmpty())
		{
			throw new ImporterNotConfiguredException("APIConfiguration not found " + API_TYPE);
		}
		return new TheTvdb(apiConfigurationOptional.get().getToken());
	}

	protected TVDBv3MovieAPIClient createMovieApiClient() throws ImporterNotConfiguredException
	{
		final Optional<APIConfiguration> apiConfigurationOptional = apiConfigurationService.getConfigurationByType(API_TYPE);
		if(apiConfigurationOptional.isEmpty())
		{
			throw new ImporterNotConfiguredException("APIConfiguration not found " + API_TYPE);
		}
		return new TVDBv3MovieAPIClient(apiConfigurationOptional.get(), generalConfigurationProperties);
	}

	@Override
	public List<ShowSearchItem> searchForShow(String search) throws ImporterNotConfiguredException, ImportProcessException, IOException
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
	public Show createShow(String identifier) throws ImporterNotConfiguredException, IOException, ImportProcessException
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

	private void createAllSeasons(TheTvdb tvdb, Integer seriesId, Show show) throws IOException, ImportProcessException, ImporterNotConfiguredException
	{
		final List<Integer> airedSeasons = getAiredSeasons(tvdb, seriesId);
		for(Integer seasonId : airedSeasons)
		{
			final Season season = createSeasonWithEpisodes(seriesId, seasonId);
			season.setShow(show);
			show.addSeason(season);
		}
	}

	private List<Integer> getAiredSeasons(TheTvdb tvdb, Integer seriesId) throws ImportProcessException, IOException
	{
		final Response<EpisodesSummaryResponse> response = tvdb.series().episodesSummary(seriesId).execute();
		final EpisodesSummaryResponse body = response.body();
		if(body == null || body.data == null || body.data.airedSeasons == null)
		{
			throw new ImportProcessException("Episode summary data from TVDB is null");
		}

		return body.data.airedSeasons.stream().sorted().toList();
	}

	private List<Episode> getEpisodes(TheTvdb tvdb, Integer seriesId, Integer seasonId) throws ImportProcessException, IOException
	{
		final Response<EpisodesResponse> episodesResponse = tvdb.series().episodesQuery(seriesId, null, seasonId, null, null, null, null, null, null, generalConfigurationProperties.getLanguage()).execute();
		final EpisodesResponse body = episodesResponse.body();
		if(body == null || body.data == null)
		{
			throw new ImportProcessException("Episode data from TVDB is null");
		}

		return body.data;
	}

	@Override
	public List<SeasonInfo> getAllAvailableSeasonInfo(Integer identifier) throws ImporterNotConfiguredException, ImportProcessException, IOException
	{
		final TheTvdb tvdb = createApiClient();

		final List<SeasonInfo> result = new ArrayList<>();

		final List<Integer> airedSeasons = getAiredSeasons(tvdb, identifier);
		for(Integer seasonId : airedSeasons)
		{
			final List<Episode> episodes = getEpisodes(tvdb, identifier, seasonId);
			result.add(new SeasonInfo(String.valueOf(seasonId), String.valueOf(seasonId), episodes.size()));
		}

		return result;
	}

	@Override
	public List<EpisodeInfo> getAllAvailableEpisodeInfo(Integer identifier, int seasonNumber) throws ImporterNotConfiguredException, ImportProcessException, IOException
	{
		final TheTvdb tvdb = createApiClient();

		final List<EpisodeInfo> result = new ArrayList<>();

		final List<Episode> episodes = getEpisodes(tvdb, identifier, seasonNumber);
		for(Episode episode : episodes)
		{
			result.add(new EpisodeInfo(episode.episodeName, episode.airedEpisodeNumber));
		}

		return result;
	}

	@Override
	public Season createSeasonWithEpisodes(Integer identifier, int seasonId) throws IOException, ImportProcessException, ImporterNotConfiguredException
	{
		final TheTvdb tvdb = createApiClient();

		final Season season = new Season(messageSource.getMessage("season.defaultName", new Object[]{seasonId}, LocaleContextHolder.getLocale()), "", seasonId);

		final List<Episode> episodes = getEpisodes(tvdb, identifier, seasonId);
		for(Episode episode : episodes)
		{
			season.addEpisode(episodeConverter.toEpisode(episode, season));
		}

		return season;
	}

	@Override
	public List<String> getShowPosterImageUrls(Integer identifier) throws IOException, ImporterNotConfiguredException
	{
		return getImageUrlsByTypeForAllSupportedLanguages(identifier, "poster");
	}

	@Override
	public List<String> getShowBannerImageUrls(Integer identifier) throws IOException, ImporterNotConfiguredException
	{
		return getImageUrlsByTypeForAllSupportedLanguages(identifier, "series");
	}

	private List<String> getImageUrlsByTypeForAllSupportedLanguages(Integer identifier, String type) throws IOException, ImporterNotConfiguredException
	{
		final List<String> urls = new ArrayList<>();
		for(TVDBv3SupportedLanguage language : TVDBv3SupportedLanguage.values())
		{
			try
			{
				urls.addAll(getImageUrlsByType(identifier, type, language.getKey()));
			}
			catch(ImportProcessException e)
			{
				// ignore if no artworks found for particular language
			}
		}

		return urls;
	}

	private List<String> getImageUrlsByType(Integer identifier, String type, String language) throws ImportProcessException, IOException, ImporterNotConfiguredException
	{
		final TheTvdb tvdb = createApiClient();

		// local must be "en" otherwise no images will be found
		final Response<SeriesImageQueryResultResponse> imagesResponse = tvdb.series().imagesQuery(identifier, type, null, null, language).execute();
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

	@Override
	public Season updateSeasonFromApi(Integer identifier, Season existingSeason) throws IOException, ImportProcessException, ImporterNotConfiguredException
	{
		final TheTvdb tvdb = createApiClient();

		final List<Episode> episodes = getEpisodes(tvdb, identifier, existingSeason.getNumber());
		for(Episode episode : episodes)
		{
			final de.thecodelabs.pockettracker.episode.model.Episode episodeFromApi = episodeConverter.toEpisode(episode, existingSeason);
			final Optional<de.thecodelabs.pockettracker.episode.model.Episode> existingEpisodeOptional = existingSeason.getEpisodeByNumber(episodeFromApi.getNumber());
			if(existingEpisodeOptional.isEmpty())
			{
				LOGGER.debug("Adding new episode {} to season {} of show \"{}\"", episodeFromApi.getNumber(), existingSeason.getNumber(), existingSeason.getShow().getName());
				existingSeason.addEpisode(episodeFromApi);
			}
			else
			{
				final de.thecodelabs.pockettracker.episode.model.Episode existingEpisode = existingEpisodeOptional.get();
				existingEpisode.setName(episodeFromApi.getName());
				existingEpisode.setDescription(episodeFromApi.getDescription());
				existingEpisode.setFirstAired(episodeFromApi.getFirstAired());

				LOGGER.debug("Updated existing episode {} from season {} of show \"{}\"", episodeFromApi.getNumber(), existingSeason.getNumber(), existingSeason.getShow().getName());
			}
		}

		return existingSeason;
	}

	@Override
	public List<MovieSearchItem> searchForMovie(String idToSearch) throws ImporterNotConfiguredException, ImportProcessException
	{
		final Optional<MovieSearchItem> searchItemOptional = createMovieApiClient().searchForMovie(idToSearch);
		return searchItemOptional.map(List::of).orElseGet(List::of);
	}

	@Override
	public Movie createMovie(String identifier) throws ImporterNotConfiguredException, IOException, ImportProcessException
	{
		final Optional<MovieSearchItem> searchItemOptional = createMovieApiClient().searchForMovie(identifier);
		if(searchItemOptional.isEmpty())
		{
			throw new ImportProcessException("No movie found for identifier " + identifier);
		}

		final MovieSearchItem searchItem = searchItemOptional.get();

		final Movie movie = new Movie(searchItem.getName(), searchItem.getDescription(), searchItem.getParsedDate(), null, searchItem.getLengthInMinutes());
		movie.setApiIdentifiers(List.of(new APIIdentifier(API_TYPE, identifier, movie)));
		return movie;
	}

	@Override
	public List<String> getMoviePosterImageUrls(Integer identifier) throws ImportProcessException, IOException, ImporterNotConfiguredException
	{
		return createMovieApiClient().getArtworkUrls(identifier);
	}

	@Override
	public Movie updateMovie(String identifier, Movie existingMovie) throws IOException, ImportProcessException, ImporterNotConfiguredException
	{
		final Movie movieFromApi = createMovie(identifier);

		existingMovie.setName(movieFromApi.getName());
		existingMovie.setDescription(movieFromApi.getDescription());
		existingMovie.setLengthInMinutes(movieFromApi.getLengthInMinutes());
		existingMovie.setReleaseDate(movieFromApi.getReleaseDate());
		LOGGER.debug("Updated existing movie {} from API", existingMovie.getId());

		return existingMovie;
	}
}
