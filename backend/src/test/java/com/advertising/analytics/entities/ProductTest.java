package com.advertising.analytics.entities;

import static org.assertj.core.api.Assertions.assertThat;

import com.advertising.analytics.services.ProductService;
import java.util.Date;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductTest {

    @Test
    public void test() {
        Product product = new Product();
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isNull();
        assertThat(product.getDateCreated()).isNull();
        assertThat(product.getDateUpdated()).isNull();

        product.setName("test");
        assertThat(product.getName()).isEqualTo("test");

        Product product2 = new Product("test2");
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isNotNull();
        assertThat(product.getDateCreated()).isNull();
        assertThat(product.getDateUpdated()).isNull();
        assertThat(product2.getName()).isEqualTo("test2");
    }
}
