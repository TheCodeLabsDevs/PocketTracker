package de.thecodelabs.pockettracker.backup.converter;

import de.thecodelabs.pockettracker.backup.model.BackupAddedMovieModel;
import de.thecodelabs.pockettracker.backup.model.BackupAddedShowModel;
import de.thecodelabs.pockettracker.user.model.AddedMovie;
import de.thecodelabs.pockettracker.user.model.AddedShow;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class AddedMovieConverter implements AbstractConverter<BackupAddedMovieModel, AddedMovie>
{
	@Override
	public BackupAddedMovieModel toBean(AddedMovie entity)
	{
		BackupAddedMovieModel model = new BackupAddedMovieModel();
		BeanUtils.merge(entity, model);
		model.setMovieId(entity.getMovie().getId());
		model.setWatchedDate(entity.getWatchedDate());
		return model;
	}

	@Override
	public AddedMovie toEntity(BackupAddedMovieModel bean)
	{
		AddedMovie entity = new AddedMovie();
		BeanUtils.merge(bean, entity);
		return entity;
	}
}
