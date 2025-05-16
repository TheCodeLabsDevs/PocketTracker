package de.thecodelabs.pockettracker.mediaitem;

import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.utils.Helpers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
public abstract class BaseMediaItemService<T extends MediaItem>
{
	protected final BaseMediaItemRepository<T> repository;
	protected final WebConfigurationProperties webConfigurationProperties;

	public List<T> getAll(String name)
	{
		final List<T> items;
		if(name == null || name.isEmpty())
		{
			items = repository.findAllByOrderByNameAsc();
		}
		else
		{
			items = repository.findAllByNameContainingIgnoreCaseOrderByNameAsc(name);
		}
		items.forEach(this::prepareItem);

		return sort(items);
	}

	protected List<T> sort(List<T> items)
	{
		return items.stream().sorted(Comparator.comparing(s -> s.getName().toLowerCase())).toList();
	}

	public abstract List<T> getAllFavoriteByUser(String name, User user);

	public Optional<T> getById(UUID id)
	{
		final Optional<T> itemOptional = repository.findById(id);
		itemOptional.ifPresent(this::prepareItem);
		return itemOptional;
	}

	protected abstract void prepareItem(T item);

	public T createItem(T item)
	{
		return repository.save(item);
	}

	public void createAll(List<T> items)
	{
		repository.saveAll(items);
	}

	@Transactional
	public void changeImage(MediaItemImageType mediaImageType, MediaItem item, String fileName, InputStream dataStream) throws IOException
	{
		deleteImage(mediaImageType, item);

		final Path basePath = Paths.get(webConfigurationProperties.getImageResourcePathForOS());

		final String escapedFileName = Helpers.replaceNonAlphaNumericCharacters(fileName, "_");
		StringBuilder filenameBuilder = new StringBuilder(escapedFileName);
		Path destinationPath;

		while(Files.exists(destinationPath = basePath.resolve(mediaImageType.getPathName()).resolve(filenameBuilder.toString())))
		{
			filenameBuilder.insert(0, "_");
		}

		if(Files.notExists(destinationPath.getParent()))
		{
			Files.createDirectories(destinationPath.getParent());
		}

		item.setImagePath(mediaImageType, basePath.relativize(destinationPath).toString());
		FileCopyUtils.copy(dataStream, Files.newOutputStream(destinationPath));
	}

	@Transactional
	public void deleteImage(MediaItemImageType mediaImageType, MediaItem item)
	{
		if(item.getImagePath(mediaImageType) == null)
		{
			return;
		}

		final Path basePath = Paths.get(webConfigurationProperties.getImageResourcePathForOS());
		final Path bannerPath = basePath.resolve(item.getImagePath(mediaImageType));

		try
		{
			Files.deleteIfExists(bannerPath);
			item.setImagePath(mediaImageType, null);
		}
		catch(IOException e)
		{
			log.error("Failed to delete image", e);
		}
	}

	@Transactional
	public void deleteItem(T item)
	{
		repository.delete(item);
	}

	@Transactional
	public abstract void addApiIdentifier(UUID itemId, APIIdentifier apiIdentifier);
}
