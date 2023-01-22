package de.thecodelabs.pockettracker.importer.factory;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.importer.ShowImporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowImporterServiceFactory
{
	private final List<ShowImporterService> showImporterServices;

	@Autowired
	public ShowImporterServiceFactory(List<ShowImporterService> showImporterServices)
	{
		this.showImporterServices = showImporterServices;
	}

	public ShowImporterService getImporter(APIType type)
	{
		final Optional<ShowImporterService> serviceOptional = showImporterServices.stream()
				.filter(item -> {
					final ImporterType annotation = AnnotationUtils.findAnnotation(item.getClass(), ImporterType.class);
					if(annotation == null)
					{
						return false;
					}
					return annotation.value() == type;
				})
				.findFirst();

		return serviceOptional.orElseThrow(() -> new IllegalArgumentException("No service implementation found for type " + type));
	}
}
