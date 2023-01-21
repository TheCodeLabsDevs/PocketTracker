package de.thecodelabs.pockettracker.administration.apiconfiguration;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfigurationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface APIConfigurationRepository extends JpaRepository<APIConfiguration, Integer>
{
	List<APIConfiguration> findAllByOrderByTypeAsc();

	Optional<APIConfiguration> findByType(APIConfigurationType type);
}
