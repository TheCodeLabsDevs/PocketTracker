package de.thecodelabs.pockettracker.utils.json;

import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

public class JsonResourcePathSerializer extends StdSerializer<String>
{
	@SuppressWarnings("SpringJavaAutowiredMembersInspection")
	@Autowired
	private WebConfigurationProperties webConfigurationProperties;

	@Value("${server.servlet.context-path:}")
	private String contextPath;

	public JsonResourcePathSerializer()
	{
		super(String.class);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
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
