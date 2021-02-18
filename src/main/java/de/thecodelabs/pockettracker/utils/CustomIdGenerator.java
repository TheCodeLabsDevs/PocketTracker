package de.thecodelabs.pockettracker.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

public class CustomIdGenerator extends IdentityGenerator implements IdentifierGenerator
{
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException
	{
		Serializable id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);
		return id != null ? id : super.generate(session, object);
	}
}
