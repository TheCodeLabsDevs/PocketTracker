package de.thecodelabs.pockettracker.apiidentifier;

import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class APIIdentifierService
{
	private final APIIdentifierRepository repository;

	@Autowired
	public APIIdentifierService(APIIdentifierRepository repository)
	{
		this.repository = repository;
	}

	public Optional<APIIdentifier> getIdentifierById(Integer id)
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
