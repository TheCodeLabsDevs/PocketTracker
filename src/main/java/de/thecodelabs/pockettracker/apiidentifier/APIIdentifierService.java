package de.thecodelabs.pockettracker.apiidentifier;

import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class APIIdentifierService
{
	private final APIIdentifierRepository repository;

	public Optional<APIIdentifier> getIdentifierById(UUID id)
	{
		return repository.findById(id);
	}

	public APIIdentifier createIdentifier(APIIdentifier apiIdentifier)
	{
		return repository.save(apiIdentifier);
	}

	@Transactional
	public void deleteIdentifier(APIIdentifier apiIdentifier)
	{
		repository.delete(apiIdentifier);
	}
}
