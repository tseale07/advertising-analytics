package com.advertising.analytics.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.advertising.analytics.entities.Provider;
import com.advertising.analytics.services.ProviderService;
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
public class ProviderControllerTest {

    private MockMvc mvc;
    private JacksonTester<Provider> providerJson;
    private JacksonTester<List<Provider>> providersJson;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testGetProvider() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        Provider provider = new Provider(UUID.randomUUID().toString());
        when(testHarness.providerService.getProvider(any(long.class))).thenReturn(Optional.of(provider));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/providers/1")
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(providerJson.write(provider).getJson());
    }

    @Test
    public void testGetProvider_doesNotExist() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        when(testHarness.providerService.getProvider(any(long.class))).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/products/1")
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testGetAllProviders() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        List<Provider> providerList = new ArrayList<>();
        providerList.add(new Provider(UUID.randomUUID().toString()));
        providerList.add(new Provider(UUID.randomUUID().toString()));

        when(testHarness.providerService.getAllProviders()).thenReturn(providerList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/providers/")
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(providersJson.write(providerList).getJson());
    }

    @Test
    public void testCreateProvider() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        Provider provider = new Provider(UUID.randomUUID().toString());
        when(testHarness.providerService.createProvider(any(String.class))).thenReturn(provider);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/providers/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(providerJson.write(provider).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(providerJson.write(provider).getJson());
    }

    @Test
    public void testImportProviders_badRequest() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        Provider provider = new Provider(UUID.randomUUID().toString());
        Provider updatedProvider = new Provider(UUID.randomUUID().toString());
        doNothing().when(testHarness.providerService).importProvidersFromCsv(any(String.class));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/providers/import")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(providerJson.write(provider).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testUpdateProvider() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        Provider provider = new Provider(UUID.randomUUID().toString());
        Provider updatedProvider = new Provider(UUID.randomUUID().toString());
        when(testHarness.providerService.getProvider(any(long.class))).thenReturn(Optional.of(provider));
        when(testHarness.providerService.updateProviderName(any(Provider.class), any(String.class))).thenReturn(updatedProvider);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/providers/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(providerJson.write(provider).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(providerJson.write(updatedProvider).getJson());
    }

    @Test
    public void testDeleteProvider() throws Exception {
        TestHarness testHarness = new TestHarness();
        mvc = MockMvcBuilders.standaloneSetup(testHarness.controller).build();

        Provider provider = new Provider(UUID.randomUUID().toString());
        when(testHarness.providerService.getProvider(any(long.class))).thenReturn(Optional.of(provider));
        doNothing().when(testHarness.providerService).deleteProvider(provider);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/providers/1")
            .content(providerJson.write(provider).getJson())
            .accept(MediaType.APPLICATION_JSON);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private class TestHarness {

        public ProviderController controller;
        public ProviderService providerService;

        TestHarness() {
            providerService = mock(ProviderService.class);
            controller = new ProviderController(providerService);
        }
    }
}
