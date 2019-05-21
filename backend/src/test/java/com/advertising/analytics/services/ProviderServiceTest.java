package com.advertising.analytics.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.advertising.analytics.entities.Provider;
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
public class ProviderServiceTest {

    @Autowired
    ProviderService providerService;

    @Test
    public void testCreate() {
        Provider provider = providerService.createProvider();
        assertThat(provider.getDateCreated()).isNotNull();
        assertThat(provider.getDateUpdated()).isNotNull();
        assertThat(providerService.getProvider(provider.getId()).get().getId()).isEqualTo(provider.getId());

        Provider provider2 = providerService.createProvider(UUID.randomUUID().toString());
        assertThat(providerService.getProvider(provider2.getId()).get().getId()).isEqualTo(provider2.getId());
    }

    @Test
    public void testFindOrCreate() {
        String name = UUID.randomUUID().toString();

        Provider product = providerService.findOrCreate(name);
        assertThat(product.getName()).isEqualTo(name);

        Provider product2 = providerService.findOrCreate(name);
        assertThat(product2.getName()).isEqualTo(name);
        assertThat(product2.getId()).isEqualTo(product.getId());
    }

    @Test
    public void testUpdate() {
        String originalName = UUID.randomUUID().toString();
        String updatedName = UUID.randomUUID().toString();

        Provider provider = providerService.createProvider(originalName);
        assertThat(provider.getName()).isEqualTo(originalName);

        provider = providerService.updateProviderName(provider, updatedName);
        assertThat(provider.getName()).isEqualTo(updatedName);
    }

    @Test
    public void testGetAll() {
        providerService.createProvider();
        providerService.createProvider();
        providerService.createProvider();

        assertThat(providerService.getAllProviders().size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    public void testDelete() {
        Provider provider = providerService.createProvider();
        assertThat(providerService.getProvider(provider.getId()).isPresent()).isEqualTo(true);

        providerService.deleteProvider(provider);
        assertThat(providerService.getProvider(provider.getId()).isPresent()).isEqualTo(false);
    }

    @Test
    public void testImportProvidersFromCsv() throws FileNotFoundException, IOException {
        providerService.importProvidersFromCsv("src/test/resources/input/Advertiser Sources.csv");

        List<String> providerNames = new ArrayList<>();
        for (Provider provider : providerService.getAllProviders()) {
            providerNames.add(provider.getName());
        }
        assertThat(providerNames).contains("Facebook", "Google", "Amazon", "Twitter", "LinkedIn");
        assertThat(providerNames).doesNotContain("Source");
    }
}
