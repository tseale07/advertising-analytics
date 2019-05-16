package com.advertising.analytics.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.advertising.analytics.entities.Product;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    public void testCreate() {
        Product product = productService.createProduct();
        assertThat(product.getDateCreated()).isNotNull();
        assertThat(product.getDateUpdated()).isNotNull();
        assertThat(productService.getProduct(product.getId()).get().getId()).isEqualTo(product.getId());

        Product product2 = productService.createProduct(UUID.randomUUID().toString());
        assertThat(productService.getProduct(product2.getId()).get().getId()).isEqualTo(product2.getId());
    }

    @Test
    public void testFindOrCreate() {
        String name = UUID.randomUUID().toString();

        Product product = productService.findOrCreate(name);
        assertThat(product.getName()).isEqualTo(name);

        Product product2 = productService.findOrCreate(name);
        assertThat(product2.getName()).isEqualTo(name);
        assertThat(product2.getId()).isEqualTo(product.getId());
    }

    @Test
    public void testUpdate() {
        String originalName = UUID.randomUUID().toString();
        String updatedName = UUID.randomUUID().toString();

        Product product = productService.createProduct(originalName);
        assertThat(product.getName()).isEqualTo(originalName);

        product = productService.updateProductName(product, updatedName);
        assertThat(product.getName()).isEqualTo(updatedName);
    }

    @Test
    public void testGetAll() {
        productService.createProduct();
        productService.createProduct();
        productService.createProduct();

        assertThat(productService.getAllProducts().size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    public void testDelete() {
        Product product = productService.createProduct();
        assertThat(productService.getProduct(product.getId()).isPresent()).isEqualTo(true);

        productService.deleteProduct(product);
        assertThat(productService.getProduct(product.getId()).isPresent()).isEqualTo(false);
    }

    @Test
    public void testImportProductsFromCsv() throws FileNotFoundException, IOException {
        productService.importProductsFromCsv("src/test/resources/input/Product List.csv");

        List<String> productNames = new ArrayList<>();
        for (Product product : productService.getAllProducts()) {
            productNames.add(product.getName());
        }
        assertThat(productNames).contains("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        assertThat(productNames).doesNotContain("product");
    }
}
