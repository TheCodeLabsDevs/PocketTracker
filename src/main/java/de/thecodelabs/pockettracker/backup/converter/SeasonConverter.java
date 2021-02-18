package de.thecodelabs.pockettracker.backup.converter;

import de.thecodelabs.pockettracker.backup.model.BackupSeasonModel;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeasonConverter implements AbstractConverter<BackupSeasonModel, Season>
{
	private final EpisodeConverter episodeConverter;

	@Autowired
	public SeasonConverter(EpisodeConverter episodeConverter)
	{
		this.episodeConverter = episodeConverter;
	}

	@Override
	public BackupSeasonModel toBean(Season entity)
	{
		BackupSeasonModel model = new BackupSeasonModel();
		BeanUtils.merge(entity, model);

		model.setEpisodes(episodeConverter.toBeans(entity.getEpisodes()));

		return model;
	}

	@Override
	public Season toEntity(BackupSeasonModel bean)
	{
		Season entity = new Season();
		BeanUtils.merge(bean, entity);

		return entity;
	}
}
