package de.thecodelabs.pockettracker.backup.converter;

import de.thecodelabs.pockettracker.backup.model.BackupUserModel;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserConverter implements AbstractConverter<BackupUserModel, User>
{
	@Override
	public BackupUserModel toBean(User entity)
	{
		BackupUserModel model = new BackupUserModel();
		BeanUtils.merge(entity, model);

		model.setShows(entity.getShows().stream().map(Show::getId).collect(Collectors.toList()));

		return model;
	}

	@Override
	public User toEntity(BackupUserModel bean)
	{
		User entity = new User();
		BeanUtils.merge(bean, entity);
		return entity;
	}
}
