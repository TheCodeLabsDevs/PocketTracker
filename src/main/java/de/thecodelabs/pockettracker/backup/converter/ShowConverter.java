package de.thecodelabs.pockettracker.backup.converter;

import de.thecodelabs.pockettracker.backup.model.BackupShowModel;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowConverter implements AbstractConverter<BackupShowModel, Show>
{
	private final SeasonConverter seasonConverter;

	@Autowired
	public ShowConverter(SeasonConverter seasonConverter)
	{
		this.seasonConverter = seasonConverter;
	}

	@Override
	public BackupShowModel toBean(Show entity)
	{
		BackupShowModel model = new BackupShowModel();
		BeanUtils.merge(entity, model);

		model.setSeasons(seasonConverter.toBeans(entity.getSeasons()));

		return model;
	}

	@Override
	public Show toEntity(BackupShowModel bean)
	{
		Show entity = new Show();
		BeanUtils.merge(bean, entity);
		return entity;
	}
}
