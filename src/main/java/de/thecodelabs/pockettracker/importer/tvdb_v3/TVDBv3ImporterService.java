package de.thecodelabs.pockettracker.importer.tvdb_v3;

import com.uwetrottmann.thetvdb.TheTvdb;
import com.uwetrottmann.thetvdb.entities.Series;
import com.uwetrottmann.thetvdb.entities.SeriesResponse;
import de.thecodelabs.pockettracker.administration.apiconfiguration.APIConfigurationService;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.authentication.GeneralConfigurationProperties;
import de.thecodelabs.pockettracker.importer.ShowImporterService;
import de.thecodelabs.pockettracker.importer.factory.ImporteNotConfiguredException;
import de.thecodelabs.pockettracker.importer.factory.ImporterType;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.show.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@ImporterType(APIType.TVDB_V3)
public class TVDBv3ImporterService implements ShowImporterService
{
	public static final APIType API_TYPE = APIType.TVDB_V3;

	private final APIConfigurationService apiConfigurationService;
	private final GeneralConfigurationProperties generalConfigurationProperties;

	private final SeriesToShowConverter showConverter;

	@Autowired
	public TVDBv3ImporterService(APIConfigurationService apiConfigurationService, GeneralConfigurationProperties generalConfigurationProperties, SeriesToShowConverter showConverter)
	{
		this.apiConfigurationService = apiConfigurationService;
		this.generalConfigurationProperties = generalConfigurationProperties;
		this.showConverter = showConverter;
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
	public Show createShow(String identifier) throws ImporteNotConfiguredException, IOException
	{
		final TheTvdb tvdb = createApiClient();
		final Response<SeriesResponse> response = tvdb.series().series(Integer.parseInt(identifier), generalConfigurationProperties.getLanguage()).execute();
		final SeriesResponse body = response.body();
		if(body == null)
		{
			throw new RuntimeException("No response from TVDB"); // TODO
		}
		final Series series = body.data;
		if(series == null)
		{
			throw new RuntimeException("No response from TVDB 2"); // TODO
		}

		final Show show = showConverter.toShow(series);
		show.setApiIdentifiers(List.of(new APIIdentifier(API_TYPE, identifier, show)));

		return show;
	}
}
