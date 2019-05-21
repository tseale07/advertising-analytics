package com.advertising.analytics.services;

import com.advertising.analytics.entities.Product;
import com.advertising.analytics.repository.ProductRepository;
import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Autowired
    ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product createProduct() {
        Product product = new Product();
        repository.save(product);
        return product;
    }

    @Override
    public Product createProduct(String name) {
        Product product = new Product(name);
        repository.save(product);
        return product;
    }

    @Override
    public Product findOrCreate(String name) {
        Product product = repository.findByName(name);
        if (product == null) {
            product = createProduct(name);
        }
        return product;
    }

    @Override
    public Optional<Product> getProduct(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public Product updateProductName(Product product, String name) {
        product.setName(name);
        repository.save(product);
        return product;
    }

    @Override
    public void deleteProduct(Product product) {
        repository.delete(product);
    }

    @Override
    public void importProductsFromCsv(File productsCsv) throws FileNotFoundException, IOException {
        if (!productsCsv.exists()) {
            throw new FileNotFoundException(String.format("File: %s does not exist.", productsCsv.getAbsolutePath()));
        }

        CsvReader csvData = new CsvReader(new FileInputStream(productsCsv), ',', Charset.forName("UTF-8"));
        csvData.readHeaders();
        String[] headers = csvData.getHeaders();

        while (csvData.readRecord()) {
            if (csvData.getColumnCount() != 1) {
                throw new IOException(String.format("CSV Record: %s has malformed data. File: %s.", csvData.getCurrentRecord(), productsCsv.getAbsolutePath()));
            }

            String productName = csvData.get(0);
            createProduct(productName);
        }
    }

    public void importProductsFromCsv(String productsCsvFilePath) throws FileNotFoundException, IOException {
        importProductsFromCsv(new File(productsCsvFilePath));
    }
}
