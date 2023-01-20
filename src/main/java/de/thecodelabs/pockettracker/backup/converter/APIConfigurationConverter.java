package de.thecodelabs.pockettracker.backup.converter;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.backup.model.BackupAPIConfigurationModel;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class APIConfigurationConverter implements AbstractConverter<BackupAPIConfigurationModel, APIConfiguration>
{
	@Override
	public BackupAPIConfigurationModel toBean(APIConfiguration entity)
	{
		BackupAPIConfigurationModel model = new BackupAPIConfigurationModel();
		BeanUtils.merge(entity, model);
		return model;
	}

	@Override
	public APIConfiguration toEntity(BackupAPIConfigurationModel bean)
	{
		APIConfiguration entity = new APIConfiguration();
		BeanUtils.merge(bean, entity);
		return entity;
	}
}
