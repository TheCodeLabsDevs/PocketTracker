package de.thecodelabs.pockettracker.importer.factory;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImporterType
{
	APIType value();
}
