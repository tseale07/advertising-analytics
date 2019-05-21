package com.advertising.analytics.repository;

import com.advertising.analytics.entities.ProductData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDataRepository extends JpaRepository<ProductData, Long> {

}
