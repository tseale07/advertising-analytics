package com.advertising.analytics.services;

import com.advertising.analytics.entities.Product;
import com.advertising.analytics.entities.ProductData;
import com.advertising.analytics.entities.Provider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface ProductDataService {

    ProductData createData();

    ProductData createData(Product product, Provider provider, long clicks, Calendar dateRecorded);

    Optional<ProductData> getData(long id);

    List<ProductData> getAllData();

    ProductData updateData(ProductData productData, Product product, Provider provider, long clicks, Calendar dateRecorded);

    ProductData updateDataProduct(ProductData productData, Product product);

    ProductData updateDataProvider(ProductData productData, Provider provider);

    ProductData updateDataClicks(ProductData productData, long clicks);

    ProductData updateDataDateRecorded(ProductData productData, Calendar dateRecorded);

    void deleteData(ProductData productData);

    void importProductDataFromCsv(File productDataCsv) throws FileNotFoundException, IOException;

    void importProductDataFromCsv(String productDataCsvFilePath) throws FileNotFoundException, IOException;

}
