package de.thecodelabs.pockettracker.utils.beans;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * @author tobias ullerich
 */
public final class BeanUtils
{
	private BeanUtils()
	{
	}

	public static <T> void merge(T src, T trg)
	{
		BeanWrapper source = new BeanWrapperImpl(src);
		BeanWrapper target = new BeanWrapperImpl(trg);

		for(PropertyDescriptor descriptor : source.getPropertyDescriptors())
		{
			if(descriptor.getWriteMethod() != null && target.isWritableProperty(descriptor.getName()))
			{
				final Object propertyValue = source.getPropertyValue(descriptor.getName());
				if(propertyValue != null)
				{
					mergeField(src, target, descriptor, propertyValue);
				}
			}
		}
	}

	private static <T> void mergeField(T src, BeanWrapper target, PropertyDescriptor descriptor, Object propertyValue)
	{
		Class<?> type = propertyValue.getClass();
		try
		{
			if(src.getClass().getDeclaredField(descriptor.getName()).isAnnotationPresent(MergeIgnore.class))
			{
				return;
			}
		}
		catch(NoSuchFieldException ignored)
		{
			return;
		}

		if(type.isEnum() ||
				type == String.class ||
				type == LocalDate.class ||
				type == LocalDateTime.class ||
				type == LocalTime.class ||
				type == UUID.class ||
				ClassUtils.isPrimitiveOrWrapper(type))
		{
			target.setPropertyValue(descriptor.getName(), propertyValue);
		}
		else if(target.getPropertyValue(descriptor.getName()) != null && type.getCanonicalName().contains("de.thecodelabs.pockettracker"))
		{
			merge(propertyValue, target.getPropertyValue(descriptor.getName()));
		}
	}

	public static String getProperty(Object src, String name)
	{
		BeanWrapper source = new BeanWrapperImpl(src);
		final Object propertyValue = source.getPropertyValue(name);
		if(propertyValue == null)
		{
			return null;
		}

		if(propertyValue instanceof String)
		{
			return (String) propertyValue;
		}
		return propertyValue.toString();
	}

	public static <T> void fixBoolean(T any)
	{
		if(any == null)
		{
			return;
		}

		BeanWrapper bean = new BeanWrapperImpl(any);

		for(PropertyDescriptor descriptor : bean.getPropertyDescriptors())
		{
			if(descriptor.getWriteMethod() != null)
			{
				final Object propertyValue = bean.getPropertyValue(descriptor.getName());
				final Object propertyType = bean.getPropertyType(descriptor.getName());

				if(propertyType == Boolean.class && propertyValue == null)
				{
					bean.setPropertyValue(descriptor.getName(), Boolean.FALSE);
				}
			}
		}
	}

	public static <T> void setDefaultValues(T any)
	{
		if(any == null)
		{
			return;
		}

		BeanWrapper bean = new BeanWrapperImpl(any);

		for(PropertyDescriptor descriptor : bean.getPropertyDescriptors())
		{
			if(descriptor.getWriteMethod() != null)
			{
				final Object propertyValue = bean.getPropertyValue(descriptor.getName());
				final Object propertyType = bean.getPropertyType(descriptor.getName());

				if(propertyType == String.class && propertyValue == null)
				{
					bean.setPropertyValue(descriptor.getName(), "");
				}
				else if(propertyType == Boolean.class && propertyValue == null)
				{
					bean.setPropertyValue(descriptor.getName(), Boolean.FALSE);
				}
				else if(propertyType == Integer.class && propertyValue == null)
				{
					bean.setPropertyValue(descriptor.getName(), 0);
				}
			}
		}
	}
}

