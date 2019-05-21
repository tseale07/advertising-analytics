package com.advertising.analytics.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductDataTest {

    @Test
    public void test() {
        ProductData productData = new ProductData();
        assertThat(productData.getId()).isNotNull();
        assertThat(productData.getProvider()).isNull();
        assertThat(productData.getProduct()).isNull();
        assertThat(productData.getClicks()).isNotNull();
        assertThat(productData.getDateRecorded()).isNull();
        assertThat(productData.getDateCreated()).isNull();
        assertThat(productData.getDateUpdated()).isNull();
        assertThat(productData.getClicks()).isEqualTo(0L);

        Provider provider = new Provider();
        Product product = new Product();
        long clicks = 1L;
        Calendar dateRecorded = Calendar.getInstance();
        dateRecorded.set(2019, 3, 5);

        productData.setProduct(product);
        assertThat(productData.getProduct()).isEqualTo(product);
        productData.setProvider(provider);
        assertThat(productData.getProvider()).isEqualTo(provider);
        productData.setClicks(clicks);
        assertThat(productData.getClicks()).isEqualTo(clicks);
        productData.setDateRecorded(dateRecorded);
        assertThat(productData.getDateRecorded()).isEqualTo(dateRecorded);

        ProductData productData2 = new ProductData(provider, product, clicks, dateRecorded);
        assertThat(productData.getId()).isNotNull();
        assertThat(productData.getProvider()).isNotNull();
        assertThat(productData.getProduct()).isNotNull();
        assertThat(productData.getClicks()).isNotNull();
        assertThat(productData.getDateRecorded()).isNotNull();
        assertThat(productData.getDateCreated()).isNull();
        assertThat(productData.getDateUpdated()).isNull();
        assertThat(productData2.getProvider()).isEqualTo(provider);
        assertThat(productData2.getProduct()).isEqualTo(product);
        assertThat(productData2.getClicks()).isEqualTo(clicks);
    }
}
