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
	private final APIIdentifierConverter apiIdentifierConverter;

	@Autowired
	public ShowConverter(SeasonConverter seasonConverter, APIIdentifierConverter apiIdentifierConverter)
	{
		this.seasonConverter = seasonConverter;
		this.apiIdentifierConverter = apiIdentifierConverter;
	}

	@Override
	public BackupShowModel toBean(Show entity)
	{
		BackupShowModel model = new BackupShowModel();
		BeanUtils.merge(entity, model);

		model.setSeasons(seasonConverter.toBeans(entity.getSeasons()));
		model.setApiIdentifiers(apiIdentifierConverter.toBeans(entity.getApiIdentifiers()));

		return model;
	}

	@Override
	public Show toEntity(BackupShowModel bean)
	{
		Show entity = new Show();
		BeanUtils.merge(bean, entity);

		entity.setSeasons(seasonConverter.toEntities(bean.getSeasons()));
		entity.getSeasons().forEach(season -> season.setShow(entity));

		entity.setApiIdentifiers(apiIdentifierConverter.toEntities(bean.getApiIdentifiers()));
		entity.getApiIdentifiers().forEach(identifier -> identifier.setShow(entity));

		return entity;
	}
}
