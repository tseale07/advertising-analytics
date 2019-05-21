package com.advertising.analytics.repository;

import com.advertising.analytics.entities.Provider;
import javax.persistence.EntityManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public abstract class ProviderRepositoryImpl implements ProviderRepository {

    private final EntityManager entityManager;

    public ProviderRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Provider findByName(String name) {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Provider.class);
        criteria.add(Restrictions.eq("name", name));
        return (Provider) criteria.uniqueResult();
    }
}
