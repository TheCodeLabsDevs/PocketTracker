package de.thecodelabs.pockettracker.backup.converter;

import de.thecodelabs.pockettracker.backup.model.BackupAPIIdentifierModel;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class APIIdentifierConverter implements AbstractConverter<BackupAPIIdentifierModel, APIIdentifier>
{
	@Override
	public BackupAPIIdentifierModel toBean(APIIdentifier entity)
	{
		BackupAPIIdentifierModel model = new BackupAPIIdentifierModel();
		BeanUtils.merge(entity, model);
		return model;
	}

	@Override
	public APIIdentifier toEntity(BackupAPIIdentifierModel bean)
	{
		APIIdentifier entity = new APIIdentifier();
		BeanUtils.merge(bean, entity);
		return entity;
	}
}
