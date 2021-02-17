package de.thecodelabs.pockettracker.backup.converter.user;

import de.thecodelabs.pockettracker.backup.converter.WatchedEpisodeConverter;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserModel;
import de.thecodelabs.pockettracker.show.model.Show;
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

	private final WatchedEpisodeConverter watchedEpisodeConverter;

	@Autowired
	public UserConverter(UserTokenConverter userTokenConverter, UserInternalAuthenticationConverter internalAuthenticationConverter,
						 UserGitlabAuthenticationConverter gitlabAuthenticationConverter, WatchedEpisodeConverter watchedEpisodeConverter)
	{
		this.userTokenConverter = userTokenConverter;
		this.internalAuthenticationConverter = internalAuthenticationConverter;
		this.gitlabAuthenticationConverter = gitlabAuthenticationConverter;
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
		model.setShows(entity.getShows().stream().map(Show::getId).collect(Collectors.toList()));
		model.setWatchedEpisodes(watchedEpisodeConverter.toBeans(entity.getWatchedEpisodes()));

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