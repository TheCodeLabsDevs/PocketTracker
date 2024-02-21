package de.thecodelabs.pockettracker.apiidentifier;

import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface APIIdentifierRepository extends JpaRepository<APIIdentifier, UUID>
{
}
