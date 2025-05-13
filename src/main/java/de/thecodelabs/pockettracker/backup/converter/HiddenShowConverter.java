package de.thecodelabs.pockettracker.backup.converter;

import de.thecodelabs.pockettracker.backup.model.BackupHiddenShowModel;
import de.thecodelabs.pockettracker.user.model.HiddenShow;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class HiddenShowConverter implements AbstractConverter<BackupHiddenShowModel, HiddenShow>
{
	@Override
	public BackupHiddenShowModel toBean(HiddenShow entity)
	{
		BackupHiddenShowModel model = new BackupHiddenShowModel();
		BeanUtils.merge(entity, model);
		model.setShowId(entity.getShow().getId());
		return model;
	}

	@Override
	public HiddenShow toEntity(BackupHiddenShowModel bean)
	{
		HiddenShow entity = new HiddenShow();
		BeanUtils.merge(bean, entity);
		return entity;
	}
}
