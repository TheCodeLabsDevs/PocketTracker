package de.thecodelabs.pockettracker.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;

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
	public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException
	{
		if(!s.startsWith("/"))
		{
			s = "/" + s;
		}

		final String url = StringUtils.join(webConfigurationProperties.getBaseUrl(), contextPath, webConfigurationProperties.getApiResourcesUrl(), s);
		jsonGenerator.writeString(url);
	}
}
