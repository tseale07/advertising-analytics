package com.advertising.analytics.repository;

import com.advertising.analytics.entities.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Provider findByName(String name);
}
