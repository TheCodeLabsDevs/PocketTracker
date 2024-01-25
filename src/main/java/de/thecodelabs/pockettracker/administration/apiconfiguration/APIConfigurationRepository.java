package de.thecodelabs.pockettracker.administration.apiconfiguration;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface APIConfigurationRepository extends JpaRepository<APIConfiguration, UUID>
{
	List<APIConfiguration> findAllByOrderByTypeAsc();

	Optional<APIConfiguration> findByType(APIType type);
}
