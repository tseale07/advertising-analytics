package com.advertising.analytics.api;

import com.advertising.analytics.entities.Provider;
import com.advertising.analytics.services.ProviderService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/providers")
public class ProviderController {

    private ProviderService providerService;

    @Autowired
    ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @PostMapping("/")
    public Provider createProvider(@Valid @RequestBody Provider providerData) {
        return providerService.createProvider(providerData.getName());
    }

    @PostMapping("/import")
    public void importProductCsv(@RequestParam("filePath") String filePath) throws FileNotFoundException, IOException {
        providerService.importProvidersFromCsv(filePath);
    }

    @GetMapping("/")
    public List<Provider> getAllProviders() {
        return providerService.getAllProviders();
    }

    @GetMapping("/{id}")
    public Provider getProvider(@PathVariable(value = "id") Long id) {
        return providerService.getProvider(id)
            .orElseThrow(() -> new ResourceNotFoundException("Provider", "id", id));
    }

    @PutMapping("/{id}")
    public Provider updateProvider(@PathVariable(value = "id") Long id,
                                   @Valid @RequestBody Provider providerData) {
        Provider provider = providerService.getProvider(id)
            .orElseThrow(() -> new ResourceNotFoundException("Provider", "id", id));

        return providerService.updateProviderName(provider, providerData.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteProvider(@PathVariable(value = "id") Long id) {
        Provider provider = providerService.getProvider(id)
            .orElseThrow(() -> new ResourceNotFoundException("Provider", "id", id));

        providerService.deleteProvider(provider);
    }
}
