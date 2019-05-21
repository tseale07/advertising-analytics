package com.advertising.analytics.api;

import com.advertising.analytics.entities.ProductData;
import com.advertising.analytics.services.ProductDataService;
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
@RequestMapping("/api/productData")
public class ProductDataController {

    private ProductDataService productDataService;

    @Autowired
    ProductDataController(ProductDataService productDataService) {
        this.productDataService = productDataService;
    }

    @PostMapping("/")
    public ProductData createProductData(@Valid @RequestBody ProductData productDataInfo) {
        return productDataService.createData(productDataInfo.getProduct(), productDataInfo.getProvider(), productDataInfo.getClicks(), productDataInfo.getDateRecorded());
    }

    @PostMapping("/import")
    public void importProductCsv(@RequestParam("filePath") String filePath) throws FileNotFoundException, IOException {
        productDataService.importProductDataFromCsv(filePath);
    }

    @GetMapping("/")
    public List<ProductData> getAllProductData() {
        return productDataService.getAllData();
    }

    @GetMapping("/{id}")
    public ProductData getProductData(@PathVariable(value = "id") Long id) {
        return productDataService.getData(id)
            .orElseThrow(() -> new ResourceNotFoundException("ProductData", "id", id));
    }

    @PutMapping("/{id}")
    public ProductData updateProductData(@PathVariable(value = "id") Long id,
                                   @Valid @RequestBody ProductData productDataInfo) {
        ProductData productData = productDataService.getData(id)
            .orElseThrow(() -> new ResourceNotFoundException("ProductData", "id", id));

        return productDataService.updateData(productData, productDataInfo.getProduct(), productDataInfo.getProvider(), productDataInfo.getClicks(), productDataInfo.getDateRecorded());
    }

    @DeleteMapping("/{id}")
    public void deleteProductData(@PathVariable(value = "id") Long id) {
        ProductData productData = productDataService.getData(id)
            .orElseThrow(() -> new ResourceNotFoundException("ProductData", "id", id));

        productDataService.deleteData(productData);
    }
}
