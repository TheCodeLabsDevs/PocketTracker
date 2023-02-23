package de.thecodelabs.pockettracker.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentityGenerator;

@SuppressWarnings("unused")
public class CustomIdGenerator extends IdentityGenerator implements IdentifierGenerator
{
	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException
	{
		final Object id = session.getEntityPersister(null, object).getIdentifier(object, session);
		return id != null ? id : super.generate(session, object);
	}
}
