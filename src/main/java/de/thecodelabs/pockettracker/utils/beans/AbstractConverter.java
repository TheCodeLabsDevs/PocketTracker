package de.thecodelabs.pockettracker.utils.beans;

import java.util.ArrayList;
import java.util.List;

public interface AbstractConverter<B, E>
{
	B toBean(E entity);

	E toEntity(B bean);

	default List<B> toBeans(Iterable<? extends E> entities)
	{
		if(entities == null)
		{
			return new ArrayList<>();
		}

		List<B> beans = new ArrayList<>();
		for(E e : entities)
		{
			final B bean = toBean(e);
			if(bean != null)
			{
				beans.add(bean);
			}
		}
		return beans;
	}

	default List<E> toEntities(Iterable<? extends B> beans)
	{
		if(beans == null)
		{
			return new ArrayList<>();
		}

		List<E> entities = new ArrayList<>();

		for(B b : beans)
		{
			final E entity = toEntity(b);
			if(entity != null)
			{
				entities.add(entity);
			}
		}

		return entities;
	}
}
