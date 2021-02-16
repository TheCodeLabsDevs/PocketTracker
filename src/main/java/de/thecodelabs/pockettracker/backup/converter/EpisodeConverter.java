package de.thecodelabs.pockettracker.backup.converter;

import de.thecodelabs.pockettracker.backup.model.BackupEpisodeModel;
import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class EpisodeConverter implements AbstractConverter<BackupEpisodeModel, Episode>
{
	@Override
	public BackupEpisodeModel toBean(Episode entity)
	{
		BackupEpisodeModel model = new BackupEpisodeModel();
		BeanUtils.merge(entity, model);
		return model;
	}

	@Override
	public Episode toEntity(BackupEpisodeModel bean)
	{
		Episode entity = new Episode();
		BeanUtils.merge(bean, entity);
		return entity;
	}
}
