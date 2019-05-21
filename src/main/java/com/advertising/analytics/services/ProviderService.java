package com.advertising.analytics.services;

import com.advertising.analytics.entities.Provider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProviderService {

    Provider createProvider();

    Provider createProvider(String name);

    Provider findOrCreate(String name);

    Optional<Provider> getProvider(long id);

    List<Provider> getAllProviders();

    Provider updateProviderName(Provider provider, String name);

    void deleteProvider(Provider provider);

    void importProvidersFromCsv(File providersCsv) throws FileNotFoundException, IOException;

    void importProvidersFromCsv(String providersCsvFilePath) throws FileNotFoundException, IOException;

}
