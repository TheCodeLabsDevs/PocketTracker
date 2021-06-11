package de.thecodelabs.pockettracker.backup.converter.user;

import de.thecodelabs.pockettracker.backup.converter.AddedShowConverter;
import de.thecodelabs.pockettracker.backup.converter.WatchedEpisodeConverter;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserGitlabAuthentication;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserInternalAuthentication;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserModel;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.authentication.GitlabAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserConverter implements AbstractConverter<BackupUserModel, User>
{
	private final UserTokenConverter userTokenConverter;
	private final UserInternalAuthenticationConverter internalAuthenticationConverter;
	private final UserGitlabAuthenticationConverter gitlabAuthenticationConverter;
	private final UserSettingsConverter userSettingsConverter;

	private final AddedShowConverter addedShowConverter;
	private final WatchedEpisodeConverter watchedEpisodeConverter;

	@Autowired
	public UserConverter(UserTokenConverter userTokenConverter, UserInternalAuthenticationConverter internalAuthenticationConverter,
						 UserGitlabAuthenticationConverter gitlabAuthenticationConverter, UserSettingsConverter userSettingsConverter,
						 AddedShowConverter addedShowConverter, WatchedEpisodeConverter watchedEpisodeConverter)
	{
		this.userTokenConverter = userTokenConverter;
		this.internalAuthenticationConverter = internalAuthenticationConverter;
		this.gitlabAuthenticationConverter = gitlabAuthenticationConverter;
		this.userSettingsConverter = userSettingsConverter;
		this.addedShowConverter = addedShowConverter;
		this.watchedEpisodeConverter = watchedEpisodeConverter;
	}

	@Override
	public BackupUserModel toBean(User entity)
	{
		BackupUserModel model = new BackupUserModel();
		BeanUtils.merge(entity, model);

		model.setAuthentications(entity.getAuthentications().stream().map(e -> {
			if(e instanceof InternalAuthentication)
			{
				return internalAuthenticationConverter.toBean((InternalAuthentication) e);
			}
			else if(e instanceof GitlabAuthentication)
			{
				return gitlabAuthenticationConverter.toBean((GitlabAuthentication) e);
			}
			else
			{
				throw new IllegalArgumentException("Authentication Type: " + e.getClass() + " is not supported");
			}
		}).collect(Collectors.toList()));
		model.setTokens(userTokenConverter.toBeans(entity.getTokens()));

		if(entity.getSettings() != null)
		{
			model.setSettings(userSettingsConverter.toBean(entity.getSettings()));
		}

		model.setShows(addedShowConverter.toBeans(entity.getShows()));
		model.setWatchedEpisodes(watchedEpisodeConverter.toBeans(entity.getWatchedEpisodes()));

		return model;
	}

	@Override
	public User toEntity(BackupUserModel bean)
	{
		User entity = new User();
		BeanUtils.merge(bean, entity);

		entity.setAuthentications(bean.getAuthentications().stream().map(model -> {
			if(model instanceof BackupUserInternalAuthentication)
			{
				return internalAuthenticationConverter.toEntity((BackupUserInternalAuthentication) model);
			}
			else if(model instanceof BackupUserGitlabAuthentication)
			{
				return gitlabAuthenticationConverter.toEntity((BackupUserGitlabAuthentication) model);
			}
			else
			{
				throw new IllegalArgumentException("Authentication Type: " + model.getClass() + " is not supported");
			}
		}).collect(Collectors.toList()));
		entity.getAuthentications().forEach(userAuthentication -> userAuthentication.setUser(entity));

		entity.setTokens(userTokenConverter.toEntities(bean.getTokens()));
		entity.getTokens().forEach(token -> token.setUser(entity));

		if(bean.getSettings() != null)
		{
			entity.setSettings(userSettingsConverter.toEntity(bean.getSettings()));
		}

		return entity;
	}
}
