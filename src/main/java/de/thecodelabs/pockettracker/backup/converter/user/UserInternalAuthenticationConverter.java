package de.thecodelabs.pockettracker.backup.converter.user;

import de.thecodelabs.pockettracker.backup.model.user.BackupUserInternalAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserInternalAuthenticationConverter implements AbstractConverter<BackupUserInternalAuthentication, InternalAuthentication>
{
	@Override
	public BackupUserInternalAuthentication toBean(InternalAuthentication entity)
	{
		BackupUserInternalAuthentication model = new BackupUserInternalAuthentication();
		BeanUtils.merge(entity, model);
		model.setId(entity.getId());
		return model;
	}

	@Override
	public InternalAuthentication toEntity(BackupUserInternalAuthentication bean)
	{
		InternalAuthentication entity = new InternalAuthentication();
		BeanUtils.merge(bean, entity);
		entity.setId(bean.getId());
		return entity;
	}
}
