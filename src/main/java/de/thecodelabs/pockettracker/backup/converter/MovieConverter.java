package de.thecodelabs.pockettracker.backup.converter;

import de.thecodelabs.pockettracker.backup.model.BackupMovieModel;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MovieConverter implements AbstractConverter<BackupMovieModel, Movie>
{
	private final APIIdentifierConverter apiIdentifierConverter;

	@Override
	public BackupMovieModel toBean(Movie entity)
	{
		BackupMovieModel model = new BackupMovieModel();
		BeanUtils.merge(entity, model);

		model.setApiIdentifiers(apiIdentifierConverter.toBeans(entity.getApiIdentifiers()));

		return model;
	}

	@Override
	public Movie toEntity(BackupMovieModel bean)
	{
		Movie entity = new Movie();
		BeanUtils.merge(bean, entity);

		entity.setApiIdentifiers(apiIdentifierConverter.toEntities(bean.getApiIdentifiers()));
		entity.getApiIdentifiers().forEach(identifier -> identifier.setMovie(entity));

		return entity;
	}
}
