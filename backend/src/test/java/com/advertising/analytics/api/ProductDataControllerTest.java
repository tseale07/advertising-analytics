package com.advertising.analytics.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.advertising.analytics.entities.Product;
import com.advertising.analytics.entities.ProductData;
import com.advertising.analytics.entities.Provider;
import com.advertising.analytics.services.ProductDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class ProductDataControllerTest {

    private MockMvc mvc;
    private JacksonTester<ProductData> productDataJson;
    private JacksonTester<List<ProductData>> productDatasJson;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testGetProductData() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        ProductData productData = new ProductData(new Provider(), new Product(), 1L, Calendar.getInstance());
        when(testHarness.productDataService.getData(any(long.class))).thenReturn(Optional.of(productData));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/productData/1")
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(productDataJson.write(productData).getJson());
    }

    @Test
    public void testGetProductData_doesNotExist() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        when(testHarness.productDataService.getData(any(long.class))).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/products/1")
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testGetAllProductDatas() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        List<ProductData> productDataList = new ArrayList<>();
        productDataList.add(new ProductData());
        productDataList.add(new ProductData());

        when(testHarness.productDataService.getAllData()).thenReturn(productDataList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/productData/")
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(productDatasJson.write(productDataList).getJson());
    }

    @Test
    public void testCreateProductData() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        ProductData productData = new ProductData(new Provider(UUID.randomUUID().toString()), new Product(UUID.randomUUID().toString()), 1L, Calendar.getInstance());
        when(testHarness.productDataService.createData(any(Product.class), any(Provider.class), any(long.class), any(Calendar.class))).thenReturn(productData);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/productData/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(productDataJson.write(productData).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(productDataJson.write(productData).getJson());
    }

    @Test
    public void testImportProductData_badRequest() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        ProductData productData = new ProductData(new Provider(), new Product(), 1L, null);
        doNothing().when(testHarness.productDataService).importProductDataFromCsv(any(String.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/productData/import")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(productDataJson.write(productData).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testUpdateProductData() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        ProductData productData = new ProductData(new Provider(UUID.randomUUID().toString()), new Product(UUID.randomUUID().toString()), 1L, Calendar.getInstance());
        ProductData updatedProductData = new ProductData(new Provider(UUID.randomUUID().toString()), new Product(UUID.randomUUID().toString()), 2L, Calendar.getInstance());
        when(testHarness.productDataService.getData(any(long.class))).thenReturn(Optional.of(productData));
        when(testHarness.productDataService.updateData(any(ProductData.class), any(Product.class), any(Provider.class), any(long.class), any(Calendar.class))).thenReturn(updatedProductData);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/productData/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(productDataJson.write(productData).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(productDataJson.write(updatedProductData).getJson());
    }

    @Test
    public void testDeleteProductData() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        ProductData productData = new ProductData(new Provider(), new Product(), 1L, null);
        when(testHarness.productDataService.getData(any(long.class))).thenReturn(Optional.of(productData));
        doNothing().when(testHarness.productDataService).deleteData(productData);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/productData/1")
            .content(productDataJson.write(productData).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private class TestHarness {

        public ProductDataController controller;
        public ProductDataService productDataService;

        TestHarness() {
            productDataService = mock(ProductDataService.class);
            controller = new ProductDataController(productDataService);
        }
    }
}
