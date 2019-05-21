package com.advertising.analytics.services;

import com.advertising.analytics.entities.Product;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct();

    Product createProduct(String name);

    Product findOrCreate(String name);

    Optional<Product> getProduct(long id);

    List<Product> getAllProducts();

    Product updateProductName(Product product, String name);

    void deleteProduct(Product product);

    void importProductsFromCsv(File productsCsv) throws FileNotFoundException, IOException;

    void importProductsFromCsv(String productsCsvFilePath) throws FileNotFoundException, IOException;

}
