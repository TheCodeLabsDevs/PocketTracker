package de.thecodelabs.pockettracker.administration.apiconfiguration;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@Controller
@RequestMapping("/administration/apiConfiguration")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
public class APIConfigurationController
{
	private final APIConfigurationService apiConfigurationService;
	private final MessageSource messageSource;

	private static class ModelAttributes
	{
		public static final String API_CONFIGURATIONS = "apiConfigurations";
		public static final String NEW_CONFIGURATION = "newConfiguration";
	}

	private static class ReturnValues
	{
		public static final String API_OVERVIEW = "administration/apiConfiguration";
		public static final String REDIRECT_API_OVERVIEW = "redirect:/administration/apiConfiguration";
	}

	@Autowired
	public APIConfigurationController(APIConfigurationService apiConfigurationService, MessageSource messageSource)
	{
		this.apiConfigurationService = apiConfigurationService;
		this.messageSource = messageSource;
	}

	@GetMapping
	public String apis(Model model)
	{
		model.addAttribute(ModelAttributes.API_CONFIGURATIONS, apiConfigurationService.getAllConfigurations());
		model.addAttribute(ModelAttributes.NEW_CONFIGURATION, new APIConfiguration());
		return ReturnValues.API_OVERVIEW;
	}

	@PostMapping("/create")
	@Transactional
	public String create(WebRequest request, @Validated @ModelAttribute("apiConfiguration") APIConfiguration apiConfiguration, BindingResult validation)
	{
		if(isApiConfigurationModelInvalid(request, apiConfiguration, validation))
		{
			return ReturnValues.REDIRECT_API_OVERVIEW;
		}

		apiConfigurationService.createConfiguration(apiConfiguration);

		return ReturnValues.REDIRECT_API_OVERVIEW;
	}

	@PostMapping("/{id}/edit")
	@Transactional
	public String edit(WebRequest request, @PathVariable Integer id, @Validated @ModelAttribute("apiConfiguration") APIConfiguration apiConfiguration, BindingResult validation)
	{
		if(isApiConfigurationModelInvalid(request, apiConfiguration, validation))
		{
			return ReturnValues.REDIRECT_API_OVERVIEW;
		}

		final Optional<APIConfiguration> apiConfigurationOptional = apiConfigurationService.getConfigurationById(id);
		if(apiConfigurationOptional.isEmpty())
		{
			throw new NotFoundException("API configuration for id " + id + " not found");
		}

		final APIConfiguration managedApiConfiguration = apiConfigurationOptional.get();
		BeanUtils.merge(apiConfiguration, managedApiConfiguration);

		return ReturnValues.REDIRECT_API_OVERVIEW;
	}

	@PostMapping("/{id}/delete")
	@Transactional
	public String delete(WebRequest request, @PathVariable Integer id)
	{
		final Optional<APIConfiguration> apiConfigurationOptional = apiConfigurationService.getConfigurationById(id);
		if(apiConfigurationOptional.isEmpty())
		{
			throw new NotFoundException("API configuration for id " + id + " not found");
		}

		final APIConfiguration apiConfiguration = apiConfigurationOptional.get();
		apiConfigurationService.deleteConfiguration(apiConfiguration);

		WebRequestUtils.putToast(request, new Toast("toast.api.configuration.delete", BootstrapColor.SUCCESS, messageSource.getMessage(apiConfiguration.getType().getCodes()[apiConfiguration.getType().ordinal()], new Object[]{}, LocaleContextHolder.getLocale())));
		return ReturnValues.REDIRECT_API_OVERVIEW;
	}

	private boolean isApiConfigurationModelInvalid(WebRequest request, APIConfiguration apiConfiguration, BindingResult validation)
	{
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, apiConfiguration);
			return true;
		}
		return false;
	}
}