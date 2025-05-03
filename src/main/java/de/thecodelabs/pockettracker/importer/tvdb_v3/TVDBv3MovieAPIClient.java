package de.thecodelabs.pockettracker.importer.tvdb_v3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.authentication.GeneralConfigurationProperties;
import de.thecodelabs.pockettracker.importer.ImportProcessException;
import de.thecodelabs.pockettracker.importer.model.MovieSearchItem;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class TVDBv3MovieAPIClient
{
	private static final String BASE_URL = "https://api.thetvdb.com";

	private final APIConfiguration apiConfiguration;
	private final GeneralConfigurationProperties generalConfigurationProperties;

	private final ObjectMapper mapper = new ObjectMapper();

	private String getToken() throws ImportProcessException
	{
		try
		{
			final URL loginUrl = URI.create(BASE_URL + "/login").toURL();
			final HttpURLConnection connection = (HttpURLConnection) loginUrl.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Accept-Language", generalConfigurationProperties.getLanguage());
			connection.setDoOutput(true);

			final String loginJson = "{\"apikey\":\"" + apiConfiguration.getToken() + "\"}";
			try(OutputStream os = connection.getOutputStream())
			{
				byte[] input = loginJson.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			int loginStatus = connection.getResponseCode();
			if(loginStatus != 200)
			{
				throw new ImportProcessException("Could not login to TVDBv3 API");
			}

			final JsonNode loginResponse = mapper.readTree(connection.getInputStream());
			return loginResponse.get("token").asText();
		}
		catch(final IOException e)
		{
			throw new ImportProcessException("Could not login to TVDBv3 API");
		}
	}

	public Optional<MovieSearchItem> searchForMovie(String idToSearch) throws ImportProcessException
	{
		try
		{
			final String token = getToken();
			final URL url = URI.create(BASE_URL + "/movies/" + idToSearch).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Accept-Language", generalConfigurationProperties.getLanguage());

			final TVDBv3SupportedLanguage languageFromSettings = TVDBv3SupportedLanguage.fromKey(generalConfigurationProperties.getLanguage());

			final JsonNode response = mapper.readTree(connection.getInputStream());
			if(response.has("error"))
			{
				if(response.get("error").asText().contains("not found"))
				{
					return Optional.empty();
				}

				throw new ImportProcessException("Error searching for movie " + idToSearch);
			}

			final JsonNode data = response.get("data");

			Optional<JsonNode> translationOptional = getTranslationByLanguageLongKey(data, languageFromSettings);
			if(translationOptional.isEmpty())
			{
				translationOptional = getTranslationByLanguageLongKey(data, TVDBv3SupportedLanguage.ENGLISH);
			}

			String name = null;
			String description = null;
			if(translationOptional.isPresent())
			{
				final JsonNode translation = translationOptional.get();
				name = translation.get("name").asText();
				description = translation.get("overview").asText();
			}

			return Optional.of(new MovieSearchItem(name, description, getReleaseDate(data), data.get("id").asInt(), data.get("runtime").asInt()));
		}
		catch(IOException e)
		{
			throw new ImportProcessException("Error searching for movie " + idToSearch);
		}
	}

	private Optional<JsonNode> getTranslationByLanguageLongKey(JsonNode data, TVDBv3SupportedLanguage language)
	{
		return StreamSupport.stream(data.get("translations").spliterator(), false)
				.filter(item -> item.get("language_code").asText().equals(language.getLongKey()))
				.findFirst();
	}

	private String getReleaseDate(JsonNode data)
	{
		return StreamSupport.stream(data.get("release_dates").spliterator(), false)
				.filter(item -> item.get("country").asText().equals("global"))
				.map(item -> item.get("date").asText())
				.findFirst().orElse(null);
	}
}
