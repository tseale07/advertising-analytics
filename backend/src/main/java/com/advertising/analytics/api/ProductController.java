package com.advertising.analytics.api;

import com.advertising.analytics.entities.Product;
import com.advertising.analytics.services.ProductService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public Product createProduct(@Valid @RequestBody Product productInfo) {
        return productService.createProduct(productInfo.getName());
    }

    @PostMapping("/import")
    public void importProductCsv(@RequestParam("filePath") String filePath) throws FileNotFoundException, IOException {
        productService.importProductsFromCsv(filePath);
    }

    @GetMapping("/")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable(value = "id") Long id) {
        return productService.getProduct(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable(value = "id") Long id,
                                 @Valid @RequestBody Product productInfo) {
        Product product = productService.getProduct(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        return productService.updateProductName(product, productInfo.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable(value = "id") Long id) {
        Product product = productService.getProduct(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productService.deleteProduct(product);
    }
}
