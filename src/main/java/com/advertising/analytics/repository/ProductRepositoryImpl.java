package com.advertising.analytics.repository;

import com.advertising.analytics.entities.Product;
import javax.persistence.EntityManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public abstract class ProductRepositoryImpl implements ProductRepository {

    private final EntityManager entityManager;

    public ProductRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Product findByName(String name) {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Product.class);
        criteria.add(Restrictions.eq("name", name));
        return (Product) criteria.uniqueResult();
    }
}
