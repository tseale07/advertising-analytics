package com.advertising.analytics.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.advertising.analytics.entities.Product;
import com.advertising.analytics.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
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
public class ProductControllerTest {

    private MockMvc mvc;
    private JacksonTester<Product> productJson;
    private JacksonTester<List<Product>> productsJson;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testGetProduct() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        Product product = new Product(UUID.randomUUID().toString());
        when(testHarness.productService.getProduct(any(long.class))).thenReturn(Optional.of(product));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/products/1")
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(productJson.write(product).getJson());
    }

    @Test
    public void testGetProduct_doesNotExist() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        when(testHarness.productService.getProduct(any(long.class))).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/products/1")
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testGetAllProducts() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        List<Product> productList = new ArrayList<>();
        productList.add(new Product(UUID.randomUUID().toString()));
        productList.add(new Product(UUID.randomUUID().toString()));

        when(testHarness.productService.getAllProducts()).thenReturn(productList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/products/")
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(productsJson.write(productList).getJson());
    }

    @Test
    public void testCreateProduct() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        Product product = new Product(UUID.randomUUID().toString());
        when(testHarness.productService.createProduct(any(String.class))).thenReturn(product);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/products/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(productJson.write(product).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(productJson.write(product).getJson());
    }

    @Test
    public void testImportProduct_badRequet() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        Product product = new Product(UUID.randomUUID().toString());
        doNothing().when(testHarness.productService).importProductsFromCsv(any(String.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/products/import")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(productJson.write(product).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        Product product = new Product(UUID.randomUUID().toString());
        Product updatedProduct = new Product(UUID.randomUUID().toString());
        when(testHarness.productService.getProduct(any(long.class))).thenReturn(Optional.of(product));
        when(testHarness.productService.updateProductName(any(Product.class), any(String.class))).thenReturn(updatedProduct);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/products/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(productJson.write(product).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(productJson.write(updatedProduct).getJson());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        Product product = new Product(UUID.randomUUID().toString());
        when(testHarness.productService.getProduct(any(long.class))).thenReturn(Optional.of(product));
        doNothing().when(testHarness.productService).deleteProduct(product);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/products/1")
            .content(productJson.write(product).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private class TestHarness {

        public ProductController controller;
        public ProductService productService;

        TestHarness() {
            productService = mock(ProductService.class);
            controller = new ProductController(productService);
        }
    }
}
