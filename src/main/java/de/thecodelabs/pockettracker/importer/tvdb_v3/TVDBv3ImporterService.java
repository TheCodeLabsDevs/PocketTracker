package de.thecodelabs.pockettracker.importer.tvdb_v3;

import com.uwetrottmann.thetvdb.TheTvdb;
import de.thecodelabs.pockettracker.administration.apiconfiguration.APIConfigurationService;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.importer.ShowImporterService;
import de.thecodelabs.pockettracker.importer.factory.ImporteNotConfiguredException;
import de.thecodelabs.pockettracker.importer.factory.ImporterType;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@ImporterType(APIType.TVDB_V3)
public class TVDBv3ImporterService implements ShowImporterService
{
	public static final APIType API_TYPE = APIType.TVDB_V3;

	private final APIConfigurationService apiConfigurationService;

	@Autowired
	public TVDBv3ImporterService(APIConfigurationService apiConfigurationService)
	{
		this.apiConfigurationService = apiConfigurationService;
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
	public Show createShow(String identifier) throws ImporteNotConfiguredException
	{
		final TheTvdb tvdb = createApiClient();
		final Show show = new Show("PLACEHOLDER", "", LocalDate.now(), null, null, ShowType.TV, false);
		show.setApiIdentifiers(List.of(new APIIdentifier(API_TYPE, identifier, show)));
		return show;
	}
}
