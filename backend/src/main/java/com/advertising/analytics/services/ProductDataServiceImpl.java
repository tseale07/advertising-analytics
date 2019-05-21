package com.advertising.analytics.services;

import com.advertising.analytics.entities.Product;
import com.advertising.analytics.entities.ProductData;
import com.advertising.analytics.entities.Provider;
import com.advertising.analytics.repository.ProductDataRepository;
import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDataServiceImpl implements ProductDataService {

    private final ProductDataRepository repository;
    private final ProductService productService;
    private final ProviderService providerService;

    @Autowired
    ProductDataServiceImpl(ProductDataRepository repository,
                           ProductService productService,
                           ProviderService providerService) {
        this.repository = repository;
        this.productService = productService;
        this.providerService = providerService;
    }

    @Override
    public ProductData createData() {
        ProductData productData = new ProductData();
        repository.save(productData);
        return productData;
    }

    @Override
    public ProductData createData(Product product, Provider provider, long clicks, Calendar dateRecorded) {
        ProductData productData = new ProductData(provider, product, clicks, dateRecorded);
        repository.save(productData);
        return productData;
    }

    @Override
    public Optional<ProductData> getData(long id) {
        return repository.findById(id);
    }

    @Override
    public List<ProductData> getAllData() {
        return repository.findAll();
    }

    @Override
    public ProductData updateData(ProductData productData, Product product, Provider provider, long clicks, Calendar dateRecorded) {
        productData.setProduct(product);
        productData.setProvider(provider);
        productData.setClicks(clicks);
        productData.setDateRecorded(dateRecorded);
        repository.save(productData);
        return productData;
    }

    @Override
    public ProductData updateDataProduct(ProductData productData, Product product) {
        productData.setProduct(product);
        repository.save(productData);
        return productData;
    }

    @Override
    public ProductData updateDataProvider(ProductData productData, Provider provider) {
        productData.setProvider(provider);
        repository.save(productData);
        return productData;
    }

    @Override
    public ProductData updateDataClicks(ProductData productData, long clicks) {
        productData.setClicks(clicks);
        repository.save(productData);
        return productData;
    }

    @Override
    public ProductData updateDataDateRecorded(ProductData productData, Calendar dateRecorded) {
        productData.setDateRecorded(dateRecorded);
        repository.save(productData);
        return productData;
    }

    @Override
    public void deleteData(ProductData productData) {
        repository.delete(productData);
    }

    @Override
    public void importProductDataFromCsv(File productDataCsv) throws FileNotFoundException, IOException {
        if (!productDataCsv.exists()) {
            throw new FileNotFoundException(String.format("File: %s does not exist.", productDataCsv.getAbsolutePath()));
        }

        String[] fileNameParts = productDataCsv.getName().replace(".csv", "").split("_");
        if (fileNameParts.length != 4) {
            throw new IOException(String.format("File: %s has a malformed name. Expecting format: ProviderName_MM_DD_YYYY.", productDataCsv.getAbsolutePath()));
        }

        String providerName = fileNameParts[0];
        int month = Integer.parseInt(fileNameParts[1]);
        int day = Integer.parseInt(fileNameParts[2]);
        int year = Integer.parseInt(fileNameParts[3]);
        Calendar dateRecorded = Calendar.getInstance();
        dateRecorded.set(year, month - 1, day); // Calendar months are 0-11

        CsvReader csvData = new CsvReader(new FileInputStream(productDataCsv), ',', Charset.forName("UTF-8"));
        csvData.readHeaders();
        String[] headers = csvData.getHeaders();

        while (csvData.readRecord()) {
            if (csvData.getColumnCount() != 2) {
                throw new IOException(String.format("CSV Record: %s has malformed data. File: %s.", csvData.getCurrentRecord(), productDataCsv.getAbsolutePath()));
            }

            String productName = csvData.get(0);
            long clicks = Long.parseLong(csvData.get(1));

            Product product = productService.findOrCreate(productName);
            Provider provider = providerService.findOrCreate(providerName);
            createData(product, provider, clicks, dateRecorded);
        }
    }

    public void importProductDataFromCsv(String productDataCsvFilePath) throws FileNotFoundException, IOException {
        importProductDataFromCsv(new File(productDataCsvFilePath));
    }
}
