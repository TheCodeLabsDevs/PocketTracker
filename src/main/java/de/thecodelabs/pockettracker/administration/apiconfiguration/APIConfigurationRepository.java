package de.thecodelabs.pockettracker.administration.apiconfiguration;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface APIConfigurationRepository extends JpaRepository<APIConfiguration, Integer>
{
	List<APIConfiguration> findAllByOrderByNameAsc();
}
