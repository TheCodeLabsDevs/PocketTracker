package de.thecodelabs.pockettracker.administration.apiconfiguration;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class APIConfigurationService
{
	private final APIConfigurationRepository repository;

	@Autowired
	public APIConfigurationService(APIConfigurationRepository repository)
	{
		this.repository = repository;
	}

	public List<APIConfiguration> getAllConfigurations()
	{
		return repository.findAllByOrderByTypeAsc();
	}

	public Optional<APIConfiguration> getConfigurationById(Integer id)
	{
		return repository.findById(id);
	}

	public Optional<APIConfiguration> getConfigurationByType(APIType type)
	{
		return repository.findByType(type);
	}

	public void createConfiguration(APIConfiguration apiConfiguration)
	{
		repository.save(apiConfiguration);
	}

	@Transactional
	public void deleteConfiguration(APIConfiguration apiConfiguration)
	{
		repository.delete(apiConfiguration);
	}
}
