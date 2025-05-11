package de.thecodelabs.pockettracker.movie.controller;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.apiidentifier.APIIdentifierService;
import de.thecodelabs.pockettracker.exceptions.InternalServerException;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.importer.ImportProcessException;
import de.thecodelabs.pockettracker.importer.factory.ImporterNotConfiguredException;
import de.thecodelabs.pockettracker.importer.factory.MovieImporterServiceFactory;
import de.thecodelabs.pockettracker.importer.model.MovieSearchItem;
import de.thecodelabs.pockettracker.importer.model.MovieSearchRequest;
import de.thecodelabs.pockettracker.mediaitem.MediaItemImageType;
import de.thecodelabs.pockettracker.movie.MovieService;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.movie.model.UpdateMoveFromApiDialogModel;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.user.service.UserService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.MessageFormat;
import java.util.*;

@Controller
@RequestMapping("/movie")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
@RequiredArgsConstructor
public class MovieAdministrationController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MovieAdministrationController.class);

	private final UserService userService;
	private final MovieService service;
	private final APIIdentifierService apiIdentifierService;

	private final MovieImporterServiceFactory movieImporterServiceFactory;
	private final MovieService movieService;

	@GetMapping("/create")
	public String createPage(WebRequest request, Model model)
	{
		final Object oldData = WebRequestUtils.popValidationData(request);
		if(oldData instanceof Movie)
		{
			model.addAttribute("movie", oldData);
		}
		else
		{
			model.addAttribute("movie", new Movie());
		}
		model.addAttribute("back_url", "/movies");

		return "administration/movie/edit";
	}

	@PostMapping("/create")
	@Transactional
	public String createPost(WebRequest request, @Validated @ModelAttribute("movie") Movie movie, BindingResult validation)
	{
		if(isModelInvalid(request, movie, validation))
		{
			return "redirect:/movie/create";
		}

		final Movie createdMovie = service.createItem(movie);

		return "redirect:/movie/" + createdMovie.getId() + "/edit";
	}

	@PostMapping("/searchApi")
	public String searchMovieInApiPost(@ModelAttribute MovieSearchRequest searchRequest, Model model)
	{
		try
		{
			final List<MovieSearchItem> items = movieImporterServiceFactory.getImporter(searchRequest.apiIdentifierType())
					.searchForMovie(searchRequest.search());
			model.addAttribute("items", items);
			model.addAttribute("type", searchRequest.apiIdentifierType());
			model.addAttribute("targetUrl", searchRequest.targetUrl());
			return "administration/movie/api/searchResult";
		}
		catch(ImporterNotConfiguredException | IOException e)
		{
			throw new InternalServerException("Cannot execute api search", e);
		}
		catch(ImportProcessException e)
		{
			throw new InternalServerException("Invalid search response", e);
		}
	}

	@PostMapping("/createFromApi")
	@Transactional
	public String createFromApiPost(WebRequest request, @ModelAttribute("newApiIdentifier") @Validated APIIdentifier apiIdentifier, BindingResult validation)
	{
		if(isModelInvalid(request, apiIdentifier, validation))
		{
			return "redirect:/movies";
		}

		try
		{
			final Movie importedMovie = movieImporterServiceFactory.getImporter(apiIdentifier.getType()).createMovie(apiIdentifier.getIdentifier());
			final Movie createdMovie = service.createItem(importedMovie);

			return "redirect:/movie/" + createdMovie.getId() + "/edit";
		}
		catch(ImporterNotConfiguredException | IOException e)
		{
			throw new InternalServerException("Cannot import movie", e);
		}
		catch(ImportProcessException e)
		{
			throw new InternalServerException("Display error in UI", e); // TODO: Show error in ui
		}
	}

	@GetMapping("/{id}/edit")
	public String editPage(WebRequest request, @PathVariable UUID id, Model model)
	{
		final Optional<Movie> movieOptional = service.getById(id);
		if(movieOptional.isEmpty())
		{
			throw new NotFoundException("Movie for id " + id + " not found");
		}

		final Object oldData = WebRequestUtils.popValidationData(request);
		final Movie movie;
		if(oldData instanceof Movie oldMovie)
		{
			movie = oldMovie;
		}
		else
		{
			movie = movieOptional.get();
		}
		model.addAttribute("movie", movie);

		model.addAttribute("back_url", "/movie/" + id);

		return "administration/movie/edit";
	}

	@PostMapping("/{id}/edit")
	@Transactional
	public String editPost(WebRequest request, @PathVariable UUID id, @Validated @ModelAttribute("movie") Movie movie, BindingResult validation)
	{
		if(isModelInvalid(request, movie, validation))
		{
			return "redirect:/movie/" + id + "/edit";
		}

		final Optional<Movie> managedMovieOptional = service.getById(id);
		if(managedMovieOptional.isEmpty())
		{
			throw new NotFoundException("Movie for id " + id + " not found");
		}
		final Movie managedShow = managedMovieOptional.get();
		BeanUtils.merge(movie, managedShow);

		return "redirect:/movie/" + id;
	}

	@PostMapping("/{id}/edit/{type}")
	public String updateImage(WebRequest request, @PathVariable UUID id, @PathVariable MediaItemImageType type,
							  @RequestParam("image") MultipartFile multipartFile)
	{
		Optional<Movie> movieOptional = service.getById(id);
		if(movieOptional.isEmpty())
		{
			return "redirect:/movie/" + id + "/edit";
		}
		final Movie movie = movieOptional.get();

		if(multipartFile == null || multipartFile.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast("toast.image.null", BootstrapColor.WARNING));
			service.deleteImage(type, movie);
			return "redirect:/movie/" + id + "/edit";
		}

		try
		{
			service.changeImage(type, movie, Optional.ofNullable(multipartFile.getOriginalFilename()).orElse(movie.getName()), multipartFile.getInputStream());
			WebRequestUtils.putToast(request, new Toast("toast.image.saved", BootstrapColor.SUCCESS));
		}
		catch(IOException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.image.error", BootstrapColor.WARNING));
			LOGGER.error("Fail to change banner image", e);
		}
		return "redirect:/movie/" + id + "/edit";
	}

	@PostMapping("/{id}/delete")
	@Transactional
	public String deletePost(WebRequest request, @PathVariable UUID id)
	{
		final Optional<Movie> managedMovieOptional = service.getById(id);
		if(managedMovieOptional.isEmpty())
		{
			throw new NotFoundException("Movie for id " + id + " not found");
		}
		final Movie managedMovie = managedMovieOptional.get();
		final String showName = managedMovie.getName();

		userService.deleteWatchedMovie(managedMovie);
		service.deleteItem(managedMovie);

		WebRequestUtils.putToast(request, new Toast("toast.movie.delete", BootstrapColor.SUCCESS, showName));
		return "redirect:/movies";
	}

	@PostMapping("/{showId}/apiIdentifier/add")
	public String addApiIdentifier(WebRequest request, @PathVariable UUID showId,
								   @ModelAttribute("newApiIdentifier") @Validated APIIdentifier apiIdentifier, BindingResult validation)
	{
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, apiIdentifier);
			return "redirect:/movie/" + showId + "/edit";
		}

		try
		{
			service.addApiIdentifier(showId, apiIdentifier);
			WebRequestUtils.putToast(request, new Toast("toast.api.identifier.added", BootstrapColor.SUCCESS, apiIdentifier.getType()));
		}
		catch(IllegalArgumentException e)
		{
			validation.addError(new FieldError("newApiIdentifier", "type", "", false, new String[]{"show.apiIdentifiers.warning.already.exists"}, new Object[]{apiIdentifier.getType()}, null));
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, apiIdentifier);
		}

		return "redirect:/movie/" + showId + "/edit";
	}

	@PostMapping("/{movieId}/apiIdentifier/delete/{id}")
	public String deleteApiIdentifier(WebRequest request, @PathVariable UUID movieId, @PathVariable UUID id)
	{
		final Optional<APIIdentifier> identifierOptional = apiIdentifierService.getIdentifierById(id);
		if(identifierOptional.isEmpty())
		{
			throw new NotFoundException("APIIdentifier for id " + id + " not found");
		}

		final APIIdentifier apiIdentifier = identifierOptional.get();
		apiIdentifierService.deleteIdentifier(apiIdentifier);

		WebRequestUtils.putToast(request, new Toast("toast.api.identifier.delete", BootstrapColor.SUCCESS, apiIdentifier.getType()));

		return "redirect:/movie/" + movieId + "/edit";
	}

	@GetMapping("/{id}/showImages/{type}")
	public String getAvailableImages(@PathVariable UUID id, @PathVariable MediaItemImageType type, Model model)
	{
		final Optional<Movie> movieOptional = service.getById(id);
		if(movieOptional.isEmpty())
		{
			throw new NotFoundException("Movie for id " + id + " not found");
		}

		final Movie movie = movieOptional.get();

		final Map<APIType, List<String>> urlsByApi = new EnumMap<>(APIType.class);
		for(APIIdentifier apiIdentifier : movie.getApiIdentifiers())
		{
			final List<String> posterUrls;
			try
			{
				posterUrls = movieImporterServiceFactory.getImporter(apiIdentifier.getType()).getMoviePosterImageUrls(Integer.parseInt(apiIdentifier.getIdentifier()));
				LOGGER.debug(MessageFormat.format("Found {0} image urls for movie \"{1}\"", posterUrls.size(), movie.getName()));
				urlsByApi.put(apiIdentifier.getType(), posterUrls);
			}
			catch(ImportProcessException | IOException | ImporterNotConfiguredException e)
			{
				LOGGER.error(MessageFormat.format("Error fetching images for show \"{0}\"", movie.getName()), e);
			}
		}

		model.addAttribute("movie", movie);
		model.addAttribute("urlsByApi", urlsByApi);

		return MessageFormat.format("administration/movie/api/{0}ImagesModal", type.name().toLowerCase());
	}

	@PostMapping("/{showId}/edit/imageFromApi/{type}")
	public String addImageFromApi(WebRequest request, @PathVariable UUID showId, @PathVariable MediaItemImageType type, @RequestParam String url)
	{
		final Optional<Movie> movieOptional = service.getById(showId);
		if(movieOptional.isEmpty())
		{
			return "redirect:/movie/" + showId + "/edit";
		}

		final Movie movie = movieOptional.get();

		service.deleteImage(type, movie);

		try(final InputStream dataStream = URI.create(url).toURL().openStream())
		{
			final String fileNameFromUrl = url.substring(url.lastIndexOf('/') + 1);
			final String fileName = MessageFormat.format("{0}_{1}", movie.getName(), fileNameFromUrl);

			service.changeImage(type, movie, fileName, dataStream);
			WebRequestUtils.putToast(request, new Toast("toast.image.saved", BootstrapColor.SUCCESS));
		}
		catch(IOException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.image.error", BootstrapColor.WARNING));
			LOGGER.error("Fail to change movie image", e);
		}
		return "redirect:/movie/" + showId + "/edit";
	}

	@GetMapping("/{id}/updateFromApiModal")
	public String updateFromApiModal(@PathVariable UUID id, Model model)
	{
		final Optional<Movie> movieOptional = movieService.getById(id);
		if(movieOptional.isEmpty())
		{
			throw new NotFoundException("Movie for id " + id + " not found");
		}

		final Movie movie = movieOptional.get();

		final List<APIType> apiTypes = movie.getApiIdentifiers().stream()
				.map(APIIdentifier::getType)
				.toList();

		model.addAttribute("movie", movie);
		model.addAttribute("apiTypes", apiTypes);

		return "administration/movie/updateMovieModal";
	}

	@Transactional
	@PostMapping("/{id}/updateFromApi")
	public String updateFromApi(WebRequest request, @PathVariable UUID id,
								@ModelAttribute("formUpdateMovieFromApi") @Validated UpdateMoveFromApiDialogModel model)
	{
		final Optional<Movie> movieOptional = movieService.getById(id);
		if(movieOptional.isEmpty())
		{
			throw new NotFoundException("Movie for id " + id + " not found");
		}

		final Movie movie = movieOptional.get();

		final Optional<APIIdentifier> apiIdentifierOptional = movie.getApiIdentifierByType(model.getApiType());
		if(apiIdentifierOptional.isEmpty())
		{
			throw new IllegalArgumentException("Model does not include api type");
		}

		final APIIdentifier apiIdentifier = apiIdentifierOptional.get();

		try
		{
			movieImporterServiceFactory.getImporter(apiIdentifier.getType()).updateMovie(apiIdentifier.getIdentifier(), movie);
		}
		catch(ImportProcessException | IOException | ImporterNotConfiguredException e)
		{
			throw new RuntimeException(e);
		}

		WebRequestUtils.putToast(request, new Toast("toast.movie.updated", BootstrapColor.SUCCESS));

		return "redirect:/movie/" + movie.getId() + "/edit";
	}

	/*
	 Utils
	 */

	private <T> boolean isModelInvalid(WebRequest request, T movie, BindingResult validation)
	{
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, movie);
			return true;
		}
		return false;
	}
}
