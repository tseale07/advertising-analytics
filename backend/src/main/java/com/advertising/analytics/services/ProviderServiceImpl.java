package com.advertising.analytics.services;

import com.advertising.analytics.entities.Provider;
import com.advertising.analytics.repository.ProviderRepository;
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
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository repository;

    @Autowired
    ProviderServiceImpl(ProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Provider createProvider() {
        Provider provider = new Provider();
        repository.save(provider);
        return provider;
    }

    @Override
    public Provider createProvider(String name) {
        Provider provider = new Provider(name);
        repository.save(provider);
        return provider;
    }

    @Override
    public Provider findOrCreate(String name) {
        Provider provider = repository.findByName(name);
        if (provider == null) {
            provider = createProvider(name);
        }
        return provider;
    }

    @Override
    public Optional<Provider> getProvider(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Provider> getAllProviders() {
        return repository.findAll();
    }

    @Override
    public Provider updateProviderName(Provider provider, String name) {
        provider.setName(name);
        repository.save(provider);
        return provider;
    }

    @Override
    public void deleteProvider(Provider provider) {
        repository.delete(provider);
    }

    @Override
    public void importProvidersFromCsv(File providersCsv) throws FileNotFoundException, IOException {
        if (!providersCsv.exists()) {
            throw new FileNotFoundException(String.format("File: %s does not exist.", providersCsv.getAbsolutePath()));
        }

        CsvReader csvData = new CsvReader(new FileInputStream(providersCsv), ',', Charset.forName("UTF-8"));
        csvData.readHeaders();
        String[] headers = csvData.getHeaders();

        while (csvData.readRecord()) {
            if (csvData.getColumnCount() != 1) {
                throw new IOException(String.format("CSV Record: %s has malformed data. File: %s.", csvData.getCurrentRecord(), providersCsv.getAbsolutePath()));
            }

            String providerName = csvData.get(0);
            createProvider(providerName);
        }
    }

    public void importProvidersFromCsv(String providersCsvFilePath) throws FileNotFoundException, IOException {
        importProvidersFromCsv(new File(providersCsvFilePath));
    }
}
