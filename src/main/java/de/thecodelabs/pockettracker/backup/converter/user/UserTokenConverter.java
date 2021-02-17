package de.thecodelabs.pockettracker.backup.converter.user;

import de.thecodelabs.pockettracker.backup.model.user.BackupUserTokenModel;
import de.thecodelabs.pockettracker.user.model.authentication.ApiTokenAuthentication;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserTokenConverter implements AbstractConverter<BackupUserTokenModel, ApiTokenAuthentication>
{
	@Override
	public BackupUserTokenModel toBean(ApiTokenAuthentication entity)
	{
		BackupUserTokenModel model = new BackupUserTokenModel();
		BeanUtils.merge(entity, model);
		return model;
	}

	@Override
	public ApiTokenAuthentication toEntity(BackupUserTokenModel bean)
	{
		ApiTokenAuthentication entity = new ApiTokenAuthentication();
		BeanUtils.merge(bean, entity);
		return entity;
	}
}
