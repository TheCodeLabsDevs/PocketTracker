package de.thecodelabs.pockettracker.backup.converter;

import de.thecodelabs.pockettracker.backup.model.BackupWatchedEpisodeModel;
import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class WatchedEpisodeConverter implements AbstractConverter<BackupWatchedEpisodeModel, WatchedEpisode>
{
	@Override
	public BackupWatchedEpisodeModel toBean(WatchedEpisode entity)
	{
		BackupWatchedEpisodeModel model = new BackupWatchedEpisodeModel();
		BeanUtils.merge(entity, model);
		model.setEpisodeId(entity.getEpisode().getId());
		return model;
	}

	@Override
	public WatchedEpisode toEntity(BackupWatchedEpisodeModel bean)
	{
		WatchedEpisode entity = new WatchedEpisode();
		BeanUtils.merge(bean, entity);
		return entity;
	}
}
