package de.thecodelabs.pockettracker.administration.apiconfiguration;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/administration/apiConfiguration")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
@RequiredArgsConstructor
public class APIConfigurationController
{
	private final APIConfigurationService apiConfigurationService;

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

	@GetMapping
	public String apis(Model model)
	{
		model.addAttribute(ModelAttributes.API_CONFIGURATIONS, apiConfigurationService.getAllConfigurations());
		model.addAttribute(ModelAttributes.NEW_CONFIGURATION, new APIConfiguration());
		return ReturnValues.API_OVERVIEW;
	}

	@PostMapping("/create")
	@Transactional
	public String create(WebRequest request, @Validated @ModelAttribute("newApiConfiguration") APIConfiguration apiConfiguration, BindingResult validation)
	{
		if(apiConfigurationService.getConfigurationByType(apiConfiguration.getType()).isPresent())
		{
			validation.addError(new FieldError("newApiConfiguration", "type", "", false, new String[]{"api.config.warning.already.exists"}, new Object[]{apiConfiguration.getType()}, null));
		}

		if(isApiConfigurationModelInvalid(request, apiConfiguration, validation))
		{
			return ReturnValues.REDIRECT_API_OVERVIEW;
		}

		apiConfigurationService.createConfiguration(apiConfiguration);

		return ReturnValues.REDIRECT_API_OVERVIEW;
	}

	@PostMapping("/{id}/edit")
	@Transactional
	public String edit(WebRequest request, @PathVariable UUID id, @Validated @ModelAttribute("apiConfiguration") APIConfiguration apiConfiguration, BindingResult validation)
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
	public String delete(WebRequest request, @PathVariable UUID id)
	{
		final Optional<APIConfiguration> apiConfigurationOptional = apiConfigurationService.getConfigurationById(id);
		if(apiConfigurationOptional.isEmpty())
		{
			throw new NotFoundException("API configuration for id " + id + " not found");
		}

		final APIConfiguration apiConfiguration = apiConfigurationOptional.get();
		apiConfigurationService.deleteConfiguration(apiConfiguration);

		WebRequestUtils.putToast(request, new Toast("toast.api.configuration.delete", BootstrapColor.SUCCESS, apiConfiguration.getType()));
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