package de.thecodelabs.pockettracker.backup.converter.user;

import de.thecodelabs.pockettracker.backup.model.user.BackupUserGitlabAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.GitlabAuthentication;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserGitlabAuthenticationConverter implements AbstractConverter<BackupUserGitlabAuthentication, GitlabAuthentication>
{
	@Override
	public BackupUserGitlabAuthentication toBean(GitlabAuthentication entity)
	{
		BackupUserGitlabAuthentication model = new BackupUserGitlabAuthentication();
		BeanUtils.merge(entity, model);
		model.setId(entity.getId());
		return model;
	}

	@Override
	public GitlabAuthentication toEntity(BackupUserGitlabAuthentication bean)
	{
		GitlabAuthentication entity = new GitlabAuthentication();
		BeanUtils.merge(bean, entity);
		entity.setId(bean.getId());
		return entity;
	}
}
