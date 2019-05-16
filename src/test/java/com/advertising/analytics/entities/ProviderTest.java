package com.advertising.analytics.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProviderTest {

    @Test
    public void test() {
        Provider provider = new Provider();
        assertThat(provider.getId()).isNotNull();
        assertThat(provider.getName()).isNull();
        assertThat(provider.getDateCreated()).isNull();
        assertThat(provider.getDateUpdated()).isNull();

        provider.setName("test");
        assertThat(provider.getName()).isEqualTo("test");

        Provider provider2 = new Provider("test2");
        assertThat(provider.getId()).isNotNull();
        assertThat(provider.getName()).isNotNull();
        assertThat(provider.getDateCreated()).isNull();
        assertThat(provider.getDateUpdated()).isNull();
        assertThat(provider2.getName()).isEqualTo("test2");
    }
}
