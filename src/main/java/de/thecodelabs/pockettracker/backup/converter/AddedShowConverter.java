package de.thecodelabs.pockettracker.backup.converter;

import de.thecodelabs.pockettracker.backup.model.BackupAddedShowModel;
import de.thecodelabs.pockettracker.user.model.AddedShow;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class AddedShowConverter implements AbstractConverter<BackupAddedShowModel, AddedShow>
{
	@Override
	public BackupAddedShowModel toBean(AddedShow entity)
	{
		BackupAddedShowModel model = new BackupAddedShowModel();
		BeanUtils.merge(entity, model);
		model.setShowId(entity.getShow().getId());
		return model;
	}

	@Override
	public AddedShow toEntity(BackupAddedShowModel bean)
	{
		AddedShow entity = new AddedShow();
		BeanUtils.merge(bean, entity);
		return entity;
	}
}
