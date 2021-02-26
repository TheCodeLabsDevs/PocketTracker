package de.thecodelabs.pockettracker.backup.converter.user;

import de.thecodelabs.pockettracker.backup.model.user.BackupUserSettingsModel;
import de.thecodelabs.pockettracker.user.model.UserSettings;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsConverter implements AbstractConverter<BackupUserSettingsModel, UserSettings>
{
	@Override
	public BackupUserSettingsModel toBean(UserSettings entity)
	{
		BackupUserSettingsModel model = new BackupUserSettingsModel();
		BeanUtils.merge(entity, model);
		return model;
	}

	@Override
	public UserSettings toEntity(BackupUserSettingsModel bean)
	{
		UserSettings entity = new UserSettings();
		BeanUtils.merge(bean, entity);
		return entity;
	}
}
