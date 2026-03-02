package de.thecodelabs.pockettracker.utils.json;

import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

@Component
public class JsonResourcePathSerializer extends StdSerializer<String>
{
	private final WebConfigurationProperties webConfigurationProperties;
	private final String contextPath;

	public JsonResourcePathSerializer(
			WebConfigurationProperties webConfigurationProperties,
			@Value("${server.servlet.context-path:}") String contextPath)
	{
		super(String.class);
		this.webConfigurationProperties = webConfigurationProperties;
		this.contextPath = contextPath;
	}

	@Override
	public void serialize(String s, JsonGenerator jsonGenerator, SerializationContext provider) throws JacksonException
	{
		if(WebUtils.isApiRequest())
		{
			if(!s.startsWith("/"))
			{
				s = "/" + s;
			}

			final String url = StringUtils.join(webConfigurationProperties.getBaseUrl(), contextPath, webConfigurationProperties.getApiResourcesUrl(), s);
			jsonGenerator.writeString(url);
		}
		else
		{
			jsonGenerator.writeString(s);
		}
	}
}
