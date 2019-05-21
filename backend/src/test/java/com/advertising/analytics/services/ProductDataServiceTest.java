package com.advertising.analytics.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.advertising.analytics.entities.Product;
import com.advertising.analytics.entities.ProductData;
import com.advertising.analytics.entities.Provider;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductDataServiceTest {

    @Autowired
    ProductDataService productDataService;

    @Autowired
    ProductService productService;

    @Autowired
    ProviderService providerService;

    @Test
    public void testCreate() {
        ProductData productData = productDataService.createData();
        assertThat(productData.getDateCreated()).isNotNull();
        assertThat(productData.getDateUpdated()).isNotNull();
        assertThat(productDataService.getData(productData.getId()).get().getId()).isEqualTo(productData.getId());

        Product product = productService.createProduct();
        Provider provider = providerService.createProvider();

        ProductData productData2 = productDataService.createData(product, provider, 1L, null);
        assertThat(productDataService.getData(productData2.getId()).get().getId()).isEqualTo(productData2.getId());
    }

    @Test
    public void testUpdate() {
        Product originalProduct = productService.createProduct();
        Product updatedProduct = productService.createProduct();
        Provider originalProvider = providerService.createProvider();
        Provider updatedProvider = providerService.createProvider();
        long originalClicks = 1L;
        long updatedClicks = 2L;

        Calendar originalDateRecorded = Calendar.getInstance();
        originalDateRecorded.set(2019, 3, 5);

        Calendar updatedDateRecorded = Calendar.getInstance();
        updatedDateRecorded.set(2019, 3, 5);

        ProductData productData = productDataService.createData(originalProduct, originalProvider, originalClicks, originalDateRecorded);
        assertThat(productData.getProduct()).isEqualTo(originalProduct);
        assertThat(productData.getProvider()).isEqualTo(originalProvider);
        assertThat(productData.getClicks()).isEqualTo(originalClicks);
        assertThat(productData.getDateRecorded()).isEqualTo(originalDateRecorded);

        productData = productDataService.updateDataProduct(productData, updatedProduct);
        productData = productDataService.updateDataProvider(productData, updatedProvider);
        productData = productDataService.updateDataClicks(productData, updatedClicks);
        productData = productDataService.updateDataDateRecorded(productData, updatedDateRecorded);
        assertThat(productData.getProduct()).isEqualTo(updatedProduct);
        assertThat(productData.getProvider()).isEqualTo(updatedProvider);
        assertThat(productData.getClicks()).isEqualTo(updatedClicks);
        assertThat(productData.getDateRecorded()).isEqualTo(updatedDateRecorded);

        productData = productDataService.updateData(productData, updatedProduct, originalProvider, updatedClicks, updatedDateRecorded);
        assertThat(productData.getProduct()).isEqualTo(updatedProduct);
        assertThat(productData.getProvider()).isEqualTo(originalProvider);
        assertThat(productData.getClicks()).isEqualTo(updatedClicks);
        assertThat(productData.getDateRecorded()).isEqualTo(updatedDateRecorded);
    }

    @Test
    public void testGetAll() {
        productDataService.createData();
        productDataService.createData();
        productDataService.createData();

        assertThat(productDataService.getAllData().size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    public void testDelete() {
        ProductData productData = productDataService.createData();
        assertThat(productDataService.getData(productData.getId()).isPresent()).isEqualTo(true);

        productDataService.deleteData(productData);
        assertThat(productDataService.getData(productData.getId()).isPresent()).isEqualTo(false);
    }

    @Test
    public void testImportProductsFromCsv() throws FileNotFoundException, IOException {
        productDataService.importProductDataFromCsv("src/test/resources/input/Amazon_04_05_2019.csv");

        List<String> productNames = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));

        List<Long> expectedProductIds = new ArrayList<>();
        for (String name : productNames) {
            expectedProductIds.add(productService.findOrCreate(name).getId());
        }

        long providerId = providerService.findOrCreate("Amazon").getId();
        Calendar dateRecorded = Calendar.getInstance();
        dateRecorded.set(2019, 3, 5, 0, 0, 0); // Calendar months are 0-11

        int expectedTotalProviderData = 0;
        int expectedTotalDateRecordedData = 0;
        List<Long> actualProductIds = new ArrayList<>();
        for (ProductData data : productDataService.getAllData()) {
            actualProductIds.add(data.getProduct().getId());
            if (data.getProvider().getId() == providerId) {
                expectedTotalProviderData++;
            }

            // sloppy check since the DB truncates the time of the day.
            // 8640000 Millis = 24 hours, so within 1 day.
            if (dateRecorded.getTimeInMillis() - data.getDateRecorded().getTimeInMillis() < 8640000L) {
                expectedTotalDateRecordedData++;
            }
        }

        assertThat(actualProductIds).containsAll(expectedProductIds);
        assertThat(expectedTotalProviderData).isGreaterThanOrEqualTo(26);
        assertThat(expectedTotalDateRecordedData).isGreaterThanOrEqualTo(26);

    }
}
