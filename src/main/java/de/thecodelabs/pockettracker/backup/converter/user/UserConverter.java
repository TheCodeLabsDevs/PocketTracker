package de.thecodelabs.pockettracker.backup.converter.user;

import de.thecodelabs.pockettracker.backup.converter.AddedMovieConverter;
import de.thecodelabs.pockettracker.backup.converter.HiddenShowConverter;
import de.thecodelabs.pockettracker.backup.converter.WatchedEpisodeConverter;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserGitlabAuthentication;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserInternalAuthentication;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserModel;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.authentication.GitlabAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import de.thecodelabs.pockettracker.utils.beans.AbstractConverter;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConverter implements AbstractConverter<BackupUserModel, User>
{
	private final UserTokenConverter userTokenConverter;
	private final UserInternalAuthenticationConverter internalAuthenticationConverter;
	private final UserGitlabAuthenticationConverter gitlabAuthenticationConverter;
	private final UserSettingsConverter userSettingsConverter;

	private final HiddenShowConverter hiddenShowConverter;
	private final AddedMovieConverter addedMovieConverter;
	private final WatchedEpisodeConverter watchedEpisodeConverter;

	@Override
	public BackupUserModel toBean(User entity)
	{
		BackupUserModel model = new BackupUserModel();
		BeanUtils.merge(entity, model);

		model.setAuthentications(entity.getAuthentications().stream().map(e -> {
			if(e instanceof InternalAuthentication authentication)
			{
				return internalAuthenticationConverter.toBean(authentication);
			}
			else if(e instanceof GitlabAuthentication authentication)
			{
				return gitlabAuthenticationConverter.toBean(authentication);
			}
			else
			{
				throw new IllegalArgumentException("Authentication Type: " + e.getClass() + " is not supported");
			}
		}).toList());
		model.setTokens(userTokenConverter.toBeans(entity.getTokens()));

		if(entity.getSettings() != null)
		{
			model.setSettings(userSettingsConverter.toBean(entity.getSettings()));
		}

		model.setHiddenShows(hiddenShowConverter.toBeans(entity.getHiddenShows()));
		model.setMovies(addedMovieConverter.toBeans(entity.getMovies()));
		model.setWatchedEpisodes(watchedEpisodeConverter.toBeans(entity.getWatchedEpisodes()));

		return model;
	}

	@Override
	public User toEntity(BackupUserModel bean)
	{
		User entity = new User();
		BeanUtils.merge(bean, entity);

		entity.setAuthentications(bean.getAuthentications().stream().map(model -> {
			if(model instanceof BackupUserInternalAuthentication authentication)
			{
				return internalAuthenticationConverter.toEntity(authentication);
			}
			else if(model instanceof BackupUserGitlabAuthentication authentication)
			{
				return gitlabAuthenticationConverter.toEntity(authentication);
			}
			else
			{
				throw new IllegalArgumentException("Authentication Type: " + model.getClass() + " is not supported");
			}
		}).toList());
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
